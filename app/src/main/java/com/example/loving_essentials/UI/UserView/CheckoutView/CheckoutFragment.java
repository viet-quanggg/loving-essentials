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

import com.example.loving_essentials.Domain.Entity.DTOs.Request.Order.CreateOrderRequest;
import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;
import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.DTOs.PaymentResponse.DataResponse;
import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.DTOs.PaymentResponse.GetPaymentResponse;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;
import com.example.loving_essentials.Domain.Services.IService.IOrderService;
import com.example.loving_essentials.Domain.Services.IService.IPaymentService;
import com.example.loving_essentials.Domain.Services.Service.AddressService;
import com.example.loving_essentials.Domain.Services.Service.OrderService;
import com.example.loving_essentials.Domain.Services.Service.PaymentService;
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
    private int userId, payment, method, isCheck = 0;
    private IAddressService iAddressService;
    private IOrderService iOrderService;
    private IPaymentService iPaymentService;
    private List<AddressResponseDto> addressResponseDtos;
    private AddressResponseDto selectedAddressResponseDto;
    private TextView txtTotal;
    private ListView addressLv;
    private RadioButton isDelivery, isNotDelivery, isCash, isTransfer;
    private Fragment MyOrderDetail;
    private Button btnConfirm;
    private int cartId;

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
            cartId = bundle.getInt("cartId");
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

        isNotDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCheck == 0){

                }else if (isCheck == 1){
                    double v1 = totalPrice - 30000;
                    totalPrice = v1;
                    txtTotal.setText(totalPrice.toString());
                    isCheck = 0;
                }
            }
        });


        isCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = 0;
            }
        });

        isTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment = 1;
            }
        });

        btnConfirm = view.findViewById(R.id.buttonConfirm);

        iOrderService = OrderService.getOrderService();
        iPaymentService = PaymentService.getPaymentService();
        isDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCheck!= 1){
                    isCheck = 1 ;
                    totalPrice += 30000;
                    txtTotal.setText(totalPrice.toString());
                    Toast.makeText(getActivity(),"Delivery is selected with fee applied !", Toast.LENGTH_SHORT).show();
                }else if(isCheck == 1){
                    Toast.makeText(getActivity(),"Delivery is already selected with fee applied !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addressLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Address " + position + " is selected !", Toast.LENGTH_SHORT).show();
                selectedAddressResponseDto = addressResponseDtos.get(position);

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
                        createOrderRequest.addressId = selectedAddressResponseDto.getId();
                        createOrderRequest.cartId = cartId;
                        createOrderRequest.payment = payment;
                        if(isCheck == 0){
                            method = 0;
                        }else{
                            method = 1;
                        }
                        createOrderRequest.method = method;

                        Call<Boolean> call = iOrderService.createOrder(cartId, createOrderRequest.getAddressId(), createOrderRequest.getMethod(), createOrderRequest.getPayment());
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful() && response.body() != null && response.body()) {
                                    MyOrderDetail = new MyOrderFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("userId", userId);
                                    Toast.makeText(getActivity(), "Order placed !", Toast.LENGTH_SHORT).show();
                                    if(payment == 0){
                                        loadFragment(MyOrderDetail);
                                    }else if(payment == 1){
                                        Call<GetPaymentResponse> paymentCall = iPaymentService.createOrder("Payment for user ID: " + userId, "Pay for order", totalPrice.intValue(), "", "");
                                        paymentCall.enqueue(new Callback<GetPaymentResponse>() {
                                            @Override
                                            public void onResponse(Call<GetPaymentResponse> call, Response<GetPaymentResponse> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    DataResponse data = response.body().getData();
                                                    Log.d("PaymentResponse", "URL: " + data.getCheckoutUrl());
                                                    bundle.putInt("orderCode", data.getOrderCode());
                                                    MyOrderDetail.setArguments(bundle);
                                                    gotoURL(data.getCheckoutUrl());
                                                    loadFragment(MyOrderDetail);
                                                } else {
                                                    Log.e("PaymentResponse", "Failed to get URL: " + response.toString());
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<GetPaymentResponse> call, Throwable t) {
                                                Log.e("PaymentError", "Failed to create order", t);
                                                Toast.makeText(getActivity(), "Failed to create payment order", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

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
            }
        });

        addressResponseDtos = new ArrayList<>();
        iAddressService = AddressService.geAddressService();
        GetAddressData();
        return  view;
    }

    private void gotoURL(String url){
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