package com.example.loving_essentials.UI.UserView.AddressView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.caverock.androidsvg.BuildConfig;
import com.example.loving_essentials.Domain.Entity.DTOs.Request.Address.AddressRequestDTO;
import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.IService.INominatimService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.Domain.Services.Service.NominatimService;
import com.example.loving_essentials.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    private MapView mapView;
    private GeoPoint selectedLocation;
    private TextView locationTextView;
    private Marker marker;
    private double latitude, longitude;
    private int userId;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        mapView = rootView.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        /*locationTextView = rootView.findViewById(R.id.locationTextView);*/

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(10.8411276, 106.8073081); // Default location
        mapController.setCenter(startPoint);

        // Add a marker to the map
        marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setDraggable(true);
        mapView.getOverlays().add(marker);

        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Projection proj = mapView.getProjection();
                IGeoPoint p = proj.fromPixels((int) event.getX(), (int) event.getY());
                selectedLocation = new GeoPoint(p.getLatitude(), p.getLongitude());
                marker.setPosition(selectedLocation);
                latitude = selectedLocation.getLatitude();
                longitude = selectedLocation.getLongitude();
                /*locationTextView.setText("Selected Location: " + latitude + ", " + longitude);*/
                Log.d("Location", "Selected Location: " + selectedLocation.getLatitude() + ", " + selectedLocation.getLongitude());
            }
            return false;
        });

        Button confirmButton = rootView.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            if (selectedLocation != null) {
                reverseGeocode(latitude, longitude);
            } else {
                Toast.makeText(getActivity(), "No location selected", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void reverseGeocode(double latitude, double longitude) {
        INominatimService service = NominatimService.getNominatimService();
        service.reverseGeocode("json", "1", "vi", latitude, longitude)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String json = response.body().string();
                                JSONObject jsonObject = new JSONObject(json);
                                JSONObject addressObject = jsonObject.getJSONObject("address");
                                String houseNumber = addressObject.optString("house_number", "");
                                String road = addressObject.optString("road", "");
                                String suburb = addressObject.optString("suburb", "");
                                String city = addressObject.optString("city", "");
                                String country = addressObject.optString("country", "");

                                // Assuming user details are retrieved correctly
                                int userId = -1;
                                String userName = "Unknown";
                                String phoneNumber = "";

                                if (getArguments() != null) {
                                    userId = getArguments().getInt("userId", -1);
                                    userName = getArguments().getString("userName", "Unknown");
                                    phoneNumber = getArguments().getString("phoneNumber", "");
                                }

                                // Create user address
                                AddressRequestDTO.UserAddress userAddress = new AddressRequestDTO.UserAddress(userId, userName, phoneNumber);

                                // Create address request DTO
                                AddressRequestDTO addressRequestDTO = new AddressRequestDTO(houseNumber, road, suburb, "", city, userAddress);

                                // Call AddAddress API
                                IAddressService addressService = AddressService.geAddressService();
                                Call<Boolean> addAddressCall = addressService.AddAddress(addressRequestDTO);
                                addAddressCall.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.isSuccessful() && response.body() != null && response.body()) {
                                            Toast.makeText(getActivity(), "Address added successfully", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        } else {
                                            Toast.makeText(getActivity(), "Failed to add address", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Add address successfully", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }
                                });
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Failed to parse address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to get address", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), "Failed to get address: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
