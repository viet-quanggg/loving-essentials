package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.DTOs.PaymentResponse.GetPaymentResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IPaymentService {

    @POST("Payment/create-payment-link-PayOs")
    Call<GetPaymentResponse> createOrder(@Query("productName") String productName, @Query("description") String description, @Query("price") int price, @Query("returnUrl") String returnUrl, @Query("cancelUrl") String cancelUrl);
}
