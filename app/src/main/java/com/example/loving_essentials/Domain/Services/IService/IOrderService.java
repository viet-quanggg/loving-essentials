package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.OrderDetailsDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.Request.Order.CreateOrderRequest;
import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderResponse;
import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderStatusUpdateRequest;
import com.example.loving_essentials.Domain.Entity.Foo;
import com.example.loving_essentials.Domain.Entity.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOrderService {
    String ENDPOINT = "order";

    @GET(ENDPOINT)
    Call<OrderDTO[]> getOrders();
    @GET(ENDPOINT + "/detail")
    Call<OrderDTO[]> getOrdersByUserId(@Query("Id") int userId);
    @GET(ENDPOINT + "/order-detail")
    Call<OrderDetailsDTO[]> getOrderDetailById(@Query("orderid") int orderid);
    @GET("Order/ByShipper/{shipperId}")
    Call<List<OrderResponse>> getOrdersByShipper(@Path("shipperId") int shipperId, @Query("status") int status, @Query("buyerName") String buyerName, @Query("productName") String productName);
    @PUT("Order/status")
    Call<Void> updateOrderStatus(@Body OrderStatusUpdateRequest orderStatusUpdate);

    @POST("Order/add")
    Call<Boolean> createOrder(@Query("cartId") int cartId, @Query("addressId") int addressId, @Query("method") int method, @Query("payment") int payment);

    // API này Chuyển status sang Processing Khi admin bấm Approved bên OrderDetail nhé
    // Truyền OrderId vào
    @PUT("Order/status/processing/{id}")
    Call<Void> updateOrderProcessingStatus(@Path("id") int id);
}
