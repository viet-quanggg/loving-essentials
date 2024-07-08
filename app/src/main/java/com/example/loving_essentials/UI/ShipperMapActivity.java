package com.example.loving_essentials.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.loving_essentials.Domain.Entity.DTOs.OSRMResponse;
import com.example.loving_essentials.Domain.Services.IService.IOSRMService;
import com.example.loving_essentials.Domain.Services.Service.OSRMService;
import com.example.loving_essentials.R;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipperMapActivity extends AppCompatActivity {

    private MapView mapView;
    private IOSRMService osrmService;
    private GeoPoint storeLocation;
    private GeoPoint customerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_shipper);

        double storeLat = 10.84236;
        double storeLon = 106.80969;
        double customerLat = getIntent().getDoubleExtra("customer_lat", 0);
        double customerLon = getIntent().getDoubleExtra("customer_lon", 0);

        storeLocation = new GeoPoint(storeLat, storeLon);
        customerLocation = new GeoPoint(customerLat, customerLon);

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(storeLocation);

        // Add markers
        addMarker(storeLocation, "Store");
        addMarker(customerLocation, "Customer");

        // Draw route
        osrmService = OSRMService.getOSRMService();
        getDirectionsAndDrawRoute(storeLocation, customerLocation);
    }

    private void addMarker(GeoPoint location, String title) {
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle(title);
        mapView.getOverlays().add(marker);
    }

    private void getDirectionsAndDrawRoute(GeoPoint origin, GeoPoint destination) {
        String coordinates = origin.getLongitude() + "," + origin.getLatitude() + ";" + destination.getLongitude() + "," + destination.getLatitude();
        Call<OSRMResponse> call = osrmService.getRoute(coordinates, "full", "geojson");
        call.enqueue(new Callback<OSRMResponse>() {
            @Override
            public void onResponse(Call<OSRMResponse> call, Response<OSRMResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().routes.isEmpty()) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("OSRM", "API Response: " + new Gson().toJson(response.body()));
                    }

                    // Draw the route on the map
                    List<List<Double>> encodedPolyline = response.body().routes.get(0).geometry.coordinates;
                    List<GeoPoint> decodedPath = decodePolyline(encodedPolyline);

                    // Draw the route on the map
                    Polyline roadOverlay = new Polyline();
                    roadOverlay.setPoints(decodedPath);
                    roadOverlay.setColor(Color.BLUE);
                    mapView.getOverlays().add(roadOverlay);

                    // Calculate total distance of the route
                    double totalDistance = calculateTotalDistance(response.body().routes.get(0).legs);
                    String formattedDistance = String.format("%.1f", totalDistance);
                    Toast.makeText(ShipperMapActivity.this, "Driving distance between store and customer: " + formattedDistance + " kilometers", Toast.LENGTH_SHORT).show();

                    mapView.invalidate(); // Refresh the map
                } else {
                    Toast.makeText(ShipperMapActivity.this, "Failed to get directions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OSRMResponse> call, Throwable t) {
                Toast.makeText(ShipperMapActivity.this, "Directions API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateTotalDistance(List<OSRMResponse.Leg> legs) {
        double totalDistanceInMeters = 0;
        for (OSRMResponse.Leg leg : legs) {
            totalDistanceInMeters += leg.distance;
        }
        return totalDistanceInMeters / 1000.0;
    }

    private List<GeoPoint> decodePolyline(List<List<Double>> encodedPath) {
        List<GeoPoint> path = new ArrayList<>();
        for (List<Double> point : encodedPath) {
            double lon = point.get(0);
            double lat = point.get(1);
            path.add(new GeoPoint(lat, lon));
        }
        return path;
    }
}