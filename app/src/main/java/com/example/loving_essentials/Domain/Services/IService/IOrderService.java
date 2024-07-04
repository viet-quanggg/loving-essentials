package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderResponse;
import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderStatusUpdateRequest;
import com.example.loving_essentials.Domain.Entity.Foo;
import com.example.loving_essentials.Domain.Entity.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOrderService {
    String ENDPOINT = "order";

    @GET(ENDPOINT)
    Call<OrderDTO[]> getOrders();

    @GET("Order/ByShipper/{shipperId}")
    Call<List<OrderResponse>> getOrdersByShipper(@Path("shipperId") int shipperId, @Query("status") int status, @Query("buyerName") String buyerName, @Query("productName") String productName);

    @PUT("Order/status")
    Call<Void> updateOrderStatus(@Body OrderStatusUpdateRequest orderStatusUpdate);
}
