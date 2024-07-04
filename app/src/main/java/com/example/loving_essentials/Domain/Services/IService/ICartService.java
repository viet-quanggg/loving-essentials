package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.Cart;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ICartService {
    String END_POINT = "Cart";

    @GET(END_POINT)
    Call<Cart[]> getAllCarts();

    @GET(END_POINT + "/{cartId}")
    Call<Cart> getCartById(@Path("cartId") int cartId);
    @GET(END_POINT + "/check/{userId}")
    Call<Cart[]> getUserCart(@Path("userId") int userId);
    @POST(END_POINT + "/{buyerId}/{productId}/{quantity}")
    Call<Cart> addProductsToCart(@Path("buyerId") int buyerId, @Path("productId") int productId, @Path("quantity") int quantity);

    @DELETE(END_POINT + "/{buyerId}/{productId}/{quantity}")
    Call<Cart> removeProductFromCart(@Path("buyerId") int buyerId, @Path("productId") int productId, @Path("quantity") int quantity);

    @DELETE(END_POINT + "/{cartId}")
    Call<Void> deleteCart(@Path("cartId") int cartId);
}
