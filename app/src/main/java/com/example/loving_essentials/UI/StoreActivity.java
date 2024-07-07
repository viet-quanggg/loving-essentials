package com.example.loving_essentials.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.loving_essentials.Domain.Entity.Store;
import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IStoreService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Fragments.UpdateStoreFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView storeName;
    private TextView storeAddress;
    private TextView storePhone;
    private TextView storeHours;
    private Button updateStoreButton;
    private IStoreService storeService;
    private static final String TAG = "StoreActivity";
    private GoogleMap gMap;
    private Store store;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_management);

        storeName = findViewById(R.id.store_name);
        storeAddress = findViewById(R.id.store_address);
        storePhone = findViewById(R.id.store_phone);
        storeHours = findViewById(R.id.store_hours);
        updateStoreButton = findViewById(R.id.update_store_button);

        // Initialize store service
        storeService = APIClient.getClient().create(IStoreService.class);

        // Example store ID to fetch
        int storeId = 1; // You can pass this ID from another activity or fragment

        // Fetch store details
        fetchStoreDetails(storeId);

        // Map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Update store button click listener
        updateStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateStoreDialog();
            }
        });
    }

    private void fetchStoreDetails(int storeId) {
        Call<Store> call = storeService.GetStoreById(storeId);
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                if (response.isSuccessful()) {
                    store = response.body();
                    if (store != null) {
                        storeName.setText(store.getName());
                        storeAddress.setText(store.getAddress());
                        storePhone.setText(store.getPhone());
                        storeHours.setText(store.getOpenHours() + " - " + store.getCloseHours());
                        updateMapLocation(store.getLatitude(), store.getLongitude());
                    }
                } else {
                    Log.e(TAG, "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.e(TAG, "Failed to fetch store details", t);
            }
        });
    }

    private void updateMapLocation(double latitude, double longitude) {
        if (gMap != null) {
            LatLng storeLocation = new LatLng(latitude, longitude);
            gMap.clear();
            gMap.addMarker(new MarkerOptions().position(storeLocation).title(store.getName()));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 18));
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        if (store != null) {
            updateMapLocation(store.getLatitude(), store.getLongitude());
        }
    }

    private void openUpdateStoreDialog() {
        if (store != null) {
            UpdateStoreFragment dialogFragment = new UpdateStoreFragment(store);
            dialogFragment.setOnStoreUpdatedListener(new UpdateStoreFragment.OnStoreUpdatedListener() {
                @Override
                public void onStoreUpdated(Store updatedStore) {
                    // Xử lý khi thông tin cửa hàng được cập nhật thành công
                    store = updatedStore;
                    updateUI(); // Cập nhật giao diện người dùng
                    Toast.makeText(StoreActivity.this, "Store updated successfully", Toast.LENGTH_SHORT).show();
                }
            });
            dialogFragment.show(getSupportFragmentManager(), "UpdateStoreDialog");
        }
    }
    private void updateUI() {
        storeName.setText(store.getName());
        storeAddress.setText(store.getAddress());
        storePhone.setText(store.getPhone());
        storeHours.setText(store.getOpenHours() + " - " + store.getCloseHours());
        updateMapLocation(store.getLatitude(), store.getLongitude());
    }
}
