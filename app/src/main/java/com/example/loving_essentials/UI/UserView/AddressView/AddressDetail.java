package com.example.loving_essentials.UI.UserView.AddressView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.loving_essentials.Domain.Entity.DTOs.Request.Address.UpdateAddressDto;
import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressDetail extends AppCompatActivity {
    private IAddressService iAddressService;
    private Toolbar toolbar;
    private Button btnUpdate, btnDelete;
    private EditText edtName, edtPhone, edtHouseNumber, edtStreet, edtWard, edtDistrict, edtCity;
    private AddressResponseDto addressResponseDto;
    private UpdateAddressDto updateAddressDto;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Mapping();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        addressResponseDto = (AddressResponseDto) bundle.getParcelable("AddressDetails");
        updateAddressDto = new UpdateAddressDto();
        updateAddressDto.setId(addressResponseDto.getId());
        addressResponseDto.setUserInfo((AddressResponseDto.UserInfo) bundle.getParcelable("UserInfo"));

        edtName.setText(addressResponseDto.getUserInfo().getName());
        edtPhone.setText(addressResponseDto.getUserInfo().getPhoneNumber());
        edtHouseNumber.setText(addressResponseDto.getHouseNumber());
        edtStreet.setText(addressResponseDto.getStreet());
        edtWard.setText(addressResponseDto.getWard());
        edtDistrict.setText(addressResponseDto.getDistrict());
        edtCity.setText(addressResponseDto.getCity());

/*        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressResponseDto deleteAddress = addressResponseDto;
                Call<Boolean> call = iAddressService.DeleteAddress(deleteAddress.getId());

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body() != null && response.isSuccessful()){
                          Intent intent = new Intent(AddressDetail.this, ShippingInformation.class);
                          startActivity(intent);
                        }else {
                            Log.e("API Error", "Response Code: " + response.code() + ", Message: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable throwable) {
                        Toast.makeText(AddressDetail.this, "Error in deleting data: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AddressDetail.this, "Save successful", Toast.LENGTH_LONG).show();
                            Intent intent1 = new Intent(AddressDetail.this, ShippingInformation.class);
                            startActivity(intent1);
                        } else {
                            Toast.makeText(AddressDetail.this, "Save failed: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(AddressDetail.this, "Save failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Save Failure", t.getMessage(), t);
                    }
                });
            }
        });


    }

    public void Mapping(){
        iAddressService = AddressService.geAddressService();

        btnUpdate = findViewById(R.id.updateAddressButton);
        btnDelete = findViewById(R.id.deleteAddressButton);

        toolbar = findViewById(R.id.toolbarAddress);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtHouseNumber = findViewById(R.id.edtHouseNumber);
        edtStreet = findViewById(R.id.edtStreet);
        edtWard = findViewById(R.id.edtWard);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtCity = findViewById(R.id.edtCity);
    }
}