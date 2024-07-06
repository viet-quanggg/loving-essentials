package com.example.loving_essentials.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loving_essentials.Domain.Entity.DTOs.StoreDTO;
import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IStoreService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.UserView.AddressView.MapFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {
    private TextView storeName;
    private TextView storeAddress;
    private TextView storePhone;
    private TextView storeHours;
    private Button viewOnMapButton;
    private Button updateStoreButton;
    private IStoreService storeService;
    private static final String TAG = "StoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_management);

        storeName = findViewById(R.id.store_name);
        storeAddress = findViewById(R.id.store_address);
        storePhone = findViewById(R.id.store_phone);
        storeHours = findViewById(R.id.store_hours);
        viewOnMapButton = findViewById(R.id.view_on_map_button);
        updateStoreButton = findViewById(R.id.update_store_button);

        // Initialize store service
        storeService = APIClient.getClient().create(IStoreService.class);

        // Example store ID to fetch
        int storeId = 1; // You can pass this ID from another activity or fragment

        // Fetch store details
        fetchStoreDetails(storeId);

        // Set button listeners
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.storeMap, new MapFragment())
                        .addToBackStack(null)  // Nếu bạn cần cho phép back lại
                        .commit();
            }
        });

        updateStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle update store logic here
            }
        });
    }

    private void fetchStoreDetails(int storeId) {
        Call<StoreDTO> call = storeService.GetStoreById(storeId);
        call.enqueue(new Callback<StoreDTO>() {
            @Override
            public void onResponse(Call<StoreDTO> call, Response<StoreDTO> response) {
                if (response.isSuccessful()) {
                    StoreDTO store = response.body();
                    if (store != null) {
                        storeName.setText(store.getName());
                        storeAddress.setText(store.getAddress());
                        storePhone.setText(store.getPhone());
                        storeHours.setText(store.getOpenHours() + " - " + store.getCloseHours());
                    }
                } else {
                    Log.e(TAG, "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StoreDTO> call, Throwable t) {
                Log.e(TAG, "Failed to fetch store details", t);
            }
        });
    }
}
