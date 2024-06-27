package com.example.loving_essentials.UI.UserView.AddressView;

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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.R;
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

    private Button btnAdd, btnDelete, btnUpdate;

   public ShippingInformation(){

   }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_shipping_information, container, false);

        btnAdd = (Button) root.findViewById(R.id.addAddressButton);

        lvAddress = (ListView) root.findViewById(R.id.addressLV);
        addressResponseDtos = new ArrayList<>();
        iAddressService = AddressService.geAddressService();
        GetAddressData();

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressResponseDto selected = addressResponseDtos.get(position);
                Toast.makeText(getContext(), "Address " + selected.getId() + " is selected !", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putParcelable("AddressDetails", selected);
                bundle.putParcelable("UserInfo", selected.userInformation);
                Intent intent = new Intent(getContext(), AddressDetail.class).putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
        return root;
    }

    private void GetAddressData(){
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }
        Call<AddressResponseDto[]> call = iAddressService.GetAddressByUserId(userId);
        call.enqueue(new Callback<AddressResponseDto[]>() {
            @Override
            public void onResponse(Call<AddressResponseDto[]> call, Response<AddressResponseDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                    AddressResponseDto[] responseDtos = response.body();
                    if(responseDtos.length == 0){
                        Toast.makeText(getContext(), "No address data available", Toast.LENGTH_LONG).show();
                    }else{
                        addressResponseDtos.clear(); // Clear any existing data
                        Collections.addAll(addressResponseDtos, responseDtos);
                        ShippingAdapter shippingAdapter = new ShippingAdapter(getContext(), R.layout.address_adapter, addressResponseDtos);
                        lvAddress.setAdapter(shippingAdapter);
                        Toast.makeText(getContext(), "Address data available", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<AddressResponseDto[]> call, Throwable throwable) {
                Toast.makeText(getContext(), "Error in getting data: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Network Failure", throwable.getMessage(), throwable);
            }
        });
    }
}