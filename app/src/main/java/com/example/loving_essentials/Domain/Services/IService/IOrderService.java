package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.OrderDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOrderService {
    String ENDPOINT = "order";

    @GET(ENDPOINT)
    Call<OrderDTO[]> getOrders();
    @GET(ENDPOINT + "/detail")
    Call<OrderDTO[]> getOrdersByUserId(@Query("Id") int userId);
    @GET(ENDPOINT + "/order-detail")
    Call<OrderDetailsDTO[]> getOrderDetailById(@Query("Id") int orderid);
}
