package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.Admin.ProductManagement.CreateProduct;
import com.example.loving_essentials.Domain.Entity.DTOs.Admin.ProductManagement.ProductDetailAdmin;
import com.example.loving_essentials.Domain.Entity.Product;
import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Entity.ProductDetail;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IProductService {
    String ENDPOINT = "products";

    @GET(ENDPOINT)
    Call<ProductDTO[]> getProducts();
    @GET(ENDPOINT + "/admin")
    Call<ProductDTO[]> getProductsAdmin();
    @GET(ENDPOINT + "/filter")
    Call<ProductDTO[]> getFilteredProducts(@Query("search") String search, @Query("categoryId") int categoryId, @Query("brandId") int brandId);

    @GET(ENDPOINT + "/detail")
    Call<ProductDetail> getProductbyId(@Query("Id") int id);

    @GET(ENDPOINT + "/detail-admin")
    Call<ProductDetailAdmin> getProductbyIdAdmin(@Query("Id") int id);
    @POST(ENDPOINT)
    Call<Void> createProduct(@Body CreateProduct product);
    @PUT(ENDPOINT)
    Call<Void> editProduct(@Body ProductDetailAdmin productDetailAdmin);
    @DELETE(ENDPOINT)
    Call<Void> deleteProduct(@Query("Id") int id);
}
