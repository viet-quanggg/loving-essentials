package com.example.loving_essentials.UI.UserView.AddressView;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Fragments.AddAddress;
import com.example.loving_essentials.UI.Fragments.UserProfileFragment;
import com.example.loving_essentials.Utility.FooUtility.ShippingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingInformation extends Fragment {
    private IAddressService iAddressService;
    private List<AddressResponseDto> addressResponseDtos;
    private ListView lvAddress;
    int userId;
    private static final int REQUEST_LOCATION_PICKER = 1;
    private Button btnAddAddress, getBtnAddAddressPicker;
    private Fragment addAddressFragment;

    private Fragment AddressDetail;

    public ShippingInformation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_shipping_information, container, false);

        btnAddAddress = root.findViewById(R.id.addAddressButton);
        getBtnAddAddressPicker = root.findViewById(R.id.addAddressPicker);
        lvAddress = root.findViewById(R.id.addressLV);
        addressResponseDtos = new ArrayList<>();
        iAddressService = AddressService.geAddressService();
        GetAddressData();

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressResponseDto selected = addressResponseDtos.get(position);
                Toast.makeText(getActivity(), "Address " + selected.getId() + " is selected !", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putParcelable("AddressDetails", selected);
                bundle.putParcelable("UserInfo", selected.userInformation);
                AddressDetail = new AddressDetail();
                AddressDetail.setArguments(bundle);
                loadFragment(AddressDetail);

//                Intent intent = new Intent(getActivity(), AddressDetail.class).putExtra("Bundle", bundle);
//                startActivity(intent);
            }
        });

        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
            btnAddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putInt("userId", userId);

                    MapFragment mapFragment = new MapFragment();
                    mapFragment.setArguments(args);
                    loadFragment(mapFragment);
                }
            });

            getBtnAddAddressPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putInt("userId", userId);

                    addAddressFragment = new AddAddress();
                    addAddressFragment.setArguments(args);
                    loadFragment(addAddressFragment);
                }
            });
        }

        getParentFragmentManager().setFragmentResultListener("locationRequestKey", this, (requestKey, result) -> {
            String location = result.getString("location");
            Toast.makeText(getActivity(), "Location: " + location, Toast.LENGTH_SHORT).show();
            // Handle the received location data
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_PICKER && resultCode == RESULT_OK) {
            String location = data.getStringExtra("location");
            // Use the location string (latitude, longitude)
            Toast.makeText(getActivity(), "Selected Location: " + location, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void GetAddressData() {
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }
        Call<AddressResponseDto[]> call = iAddressService.GetAddressByUserId(userId);
        call.enqueue(new Callback<AddressResponseDto[]>() {
            @Override
            public void onResponse(Call<AddressResponseDto[]> call, Response<AddressResponseDto[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddressResponseDto[] responseDtos = response.body();
                    if (responseDtos.length == 0) {
                        Toast.makeText(getActivity(), "No address data available", Toast.LENGTH_LONG).show();
                    } else {
                        addressResponseDtos.clear(); // Clear any existing data
                        Collections.addAll(addressResponseDtos, responseDtos);
                        ShippingAdapter shippingAdapter = new ShippingAdapter(getActivity(), R.layout.address_adapter, addressResponseDtos);
                        lvAddress.setAdapter(shippingAdapter);
                        Toast.makeText(getActivity(), "Address data available", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressResponseDto[]> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Error in getting data: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Network Failure", throwable.getMessage(), throwable);
            }
        });
    }
}

