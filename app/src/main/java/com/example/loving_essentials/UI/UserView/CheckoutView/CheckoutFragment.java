package com.example.loving_essentials.UI.UserView.CheckoutView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Fragments.MyOrderFragment;
import com.example.loving_essentials.Utility.FooUtility.ShippingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends Fragment {
    private Double totalPrice;
    private int userId;
    private IAddressService iAddressService;
    private List<AddressResponseDto> addressResponseDtos;
    private TextView txtTotal;
    private ListView addressLv;
    private RadioButton isDelivery, isNotDelivery, isCash, isTransfer;
    private Fragment MyOrderDetail;
    private Button btnConfirm;
    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            totalPrice = bundle.getDouble("total");
            userId  = bundle.getInt("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        txtTotal = view.findViewById(R.id.txtTotal);
        txtTotal.setText(totalPrice.toString());
        addressLv = view.findViewById(R.id.addressLV);

        isDelivery = view.findViewById(R.id.radioShip);
        isNotDelivery = view.findViewById(R.id.radioNoship);
        isCash = view.findViewById(R.id.radioCash);
        isTransfer = view.findViewById(R.id.radioBank);

        btnConfirm = view.findViewById(R.id.buttonConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderDetail = new MyOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                MyOrderDetail.setArguments(bundle);
                Toast.makeText(getActivity(), "Order placed !", Toast.LENGTH_SHORT).show();
                loadFragment(MyOrderDetail);
                gotoUrl("https://pay.payos.vn/web/6544084352374b20893331d2c23ed4ce");

            }
        });


        isDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrice += 30000;
                txtTotal.setText(totalPrice.toString());

            }
        });

        isCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cash payment is selected !", Toast.LENGTH_SHORT).show();
            }
        });

        isTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Bank transfer payment is selected !", Toast.LENGTH_SHORT).show();
            }
        });

        isNotDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Take at store is selected !", Toast.LENGTH_SHORT).show();
            }
        });

        addressLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Address " + position + " is selected !", Toast.LENGTH_SHORT).show();

            }
        });

        addressResponseDtos = new ArrayList<>();
        iAddressService = AddressService.geAddressService();
        GetAddressData();
        return  view;
    }

    private void gotoUrl(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
                        addressLv.setAdapter(shippingAdapter);
                        Toast.makeText(getActivity(), "Address data loaded !", Toast.LENGTH_LONG).show();
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

    private void loadFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}