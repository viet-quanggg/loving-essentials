package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
import com.example.loving_essentials.Domain.Entity.Foo;
import com.example.loving_essentials.Domain.Entity.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IOrderService {
    String ENDPOINT = "order";

    @GET(ENDPOINT)
    Call<OrderDTO[]> getOrders();
}
