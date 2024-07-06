package com.example.loving_essentials.UI.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loving_essentials.Domain.Entity.DTOs.StoreDTO;
import com.example.loving_essentials.Domain.Services.Service.StoreService;
import com.example.loving_essentials.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreFragment extends Fragment {

    private GoogleMap googleMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap map) {
            googleMap = map;
            loadStoreLocation();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void loadStoreLocation() {
        StoreService.getStoreService().GetStoreById(1).enqueue(new Callback<StoreDTO>() {
            @Override
            public void onResponse(Call<StoreDTO> call, Response<StoreDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StoreDTO store = response.body();
                    LatLng storeLocation = new LatLng(store.getLatitude(), store.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(storeLocation).title(store.getName()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15));
                }
            }

            @Override
            public void onFailure(Call<StoreDTO> call, Throwable t) {
                // Handle failure
            }
        });
    }
}