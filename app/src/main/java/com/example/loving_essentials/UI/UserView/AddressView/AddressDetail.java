package com.example.loving_essentials.UI.UserView.AddressView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.example.loving_essentials.Domain.Entity.DTOs.Request.Address.UpdateAddressDto;
import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressDetail extends Fragment {
    private IAddressService iAddressService;
    private Button btnUpdate, btnDelete;
    private EditText edtName, edtPhone, edtHouseNumber, edtStreet, edtWard, edtDistrict, edtCity;
    private AddressResponseDto addressResponseDto;
    private UpdateAddressDto updateAddressDto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.activity_address_detail, container, false);

        // Initialize UI components
        Mapping(view);

        return view;
    }

    @Override
    public void onViewCreated( View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up toolbar or handle as per your design

        // Retrieve data from arguments instead of Intent
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            addressResponseDto =  bundle.getParcelable("AddressDetails");
            updateAddressDto = new UpdateAddressDto();
            updateAddressDto.setId(addressResponseDto.getId());
            addressResponseDto.setUserInfo(bundle.getParcelable("UserInfo"));

            // Populate the fields with data
            edtName.setText(addressResponseDto.getUserInfo().getName());
            edtPhone.setText(addressResponseDto.getUserInfo().getPhoneNumber());
            edtHouseNumber.setText(addressResponseDto.getHouseNumber());
            edtStreet.setText(addressResponseDto.getStreet());
            edtWard.setText(addressResponseDto.getWard());
            edtDistrict.setText(addressResponseDto.getDistrict());
            edtCity.setText(addressResponseDto.getCity());
        }

        // Set up button click listeners
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressResponseDto deleteAddress = addressResponseDto;
                Call<Boolean> call = iAddressService.DeleteAddress(deleteAddress.getId());

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body() != null && response.isSuccessful()){
                            // Handle successful deletion
                            if (getActivity() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        } else {
                            Log.e("API Error", "Response Code: " + response.code() + ", Message: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable throwable) {
                        Toast.makeText(getActivity(), "Error in deleting data: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Network Failure", throwable.getMessage(), throwable);
                    }
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddressDto.setId(addressResponseDto.getId());
                updateAddressDto.setHouseNumber(edtHouseNumber.getText().toString());
                updateAddressDto.setStreet(edtStreet.getText().toString());
                updateAddressDto.setWard(edtWard.getText().toString());
                updateAddressDto.setDistrict(edtDistrict.getText().toString());
                updateAddressDto.setCity(edtCity.getText().toString());

                Call<Boolean> call = iAddressService.UpdateAddress(updateAddressDto);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getActivity(), "Save successful", Toast.LENGTH_LONG).show();
                            if (getActivity() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Save failed: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), "Save failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Save Failure", t.getMessage(), t);
                    }
                });
            }
        });
    }

    // Initialize UI components and service
    private void Mapping(View view){
        iAddressService = AddressService.geAddressService();

        btnUpdate = view.findViewById(R.id.updateAddressButton);
        btnDelete = view.findViewById(R.id.deleteAddressButton);

        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtHouseNumber = view.findViewById(R.id.edtHouseNumber);
        edtStreet = view.findViewById(R.id.edtStreet);
        edtWard = view.findViewById(R.id.edtWard);
        edtDistrict = view.findViewById(R.id.edtDistrict);
        edtCity = view.findViewById(R.id.edtCity);
    }
}
