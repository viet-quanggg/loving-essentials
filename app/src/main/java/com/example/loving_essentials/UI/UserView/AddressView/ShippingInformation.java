package com.example.loving_essentials.UI.UserView.AddressView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class ShippingInformation extends AppCompatActivity {
    private IAddressService iAddressService;
    private List<AddressResponseDto> addressResponseDtos;
    private ListView lvAddress;

    private Button btnAdd, btnDelete, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shipping_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAdd = (Button) findViewById(R.id.addAddressButton);

        lvAddress = (ListView) findViewById(R.id.addressLV);
        addressResponseDtos = new ArrayList<>();
        iAddressService = AddressService.geAddressService();
        GetAddressData();

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressResponseDto selected = addressResponseDtos.get(position);
                Toast.makeText(ShippingInformation.this, "Address " + selected.getId() + " is selected !", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putParcelable("AddressDetails", selected);
                bundle.putParcelable("UserInfo", selected.userInformation);
                Intent intent = new Intent(ShippingInformation.this, AddressDetail.class).putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
    }

    private void GetAddressData(){
        Call<AddressResponseDto[]> call = iAddressService.GetAddressByUserId(6);
        call.enqueue(new Callback<AddressResponseDto[]>() {
            @Override
            public void onResponse(Call<AddressResponseDto[]> call, Response<AddressResponseDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                    AddressResponseDto[] responseDtos = response.body();
                    if(responseDtos.length == 0){
                        Toast.makeText(ShippingInformation.this, "No address data available", Toast.LENGTH_LONG).show();
                    }else{
                        addressResponseDtos.clear(); // Clear any existing data
                        Collections.addAll(addressResponseDtos, responseDtos);
                        ShippingAdapter shippingAdapter = new ShippingAdapter(ShippingInformation.this, R.layout.address_adapter, addressResponseDtos);
                        lvAddress.setAdapter(shippingAdapter);
                        Toast.makeText(ShippingInformation.this, "Address data available", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<AddressResponseDto[]> call, Throwable throwable) {
                Toast.makeText(ShippingInformation.this, "Error in getting data: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Network Failure", throwable.getMessage(), throwable);
            }
        });
    }
}