package com.example.loving_essentials.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loving_essentials.Domain.Entity.DTOs.Request.Address.AddressRequestDTO;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends Fragment {
    private EditText houseNumber, street, ward, district, city;
    private Button btnSave;
    private int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_address, container, false);
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }

        houseNumber = root.findViewById(R.id.houseNumber);
        street = root.findViewById(R.id.street);
        ward = root.findViewById(R.id.ward);
        district = root.findViewById(R.id.district);
        city = root.findViewById(R.id.city);
        btnSave = root.findViewById(R.id.saveButton);
        AddressRequestDTO addAddressRequestDto = new AddressRequestDTO();
        AddressRequestDTO.UserAddress userAddress = new AddressRequestDTO.UserAddress(userId, "","");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddressRequestDto.setHouseNumber(houseNumber.getText().toString());
                addAddressRequestDto.setStreet(street.getText().toString());
                addAddressRequestDto.setWard(ward.getText().toString());
                addAddressRequestDto.setDistrict(district.getText().toString());
                addAddressRequestDto.setCity(city.getText().toString());
                addAddressRequestDto.setUserAddress(userAddress);

                IAddressService addressService = AddressService.geAddressService();
                Call<Boolean> addAddressCall = addressService.AddAddress(addAddressRequestDto);
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



            }
        });


        return root;
    }



}