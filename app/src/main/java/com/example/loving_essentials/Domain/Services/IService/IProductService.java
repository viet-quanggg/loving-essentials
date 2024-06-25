package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Entity.ProductDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IProductService {
    String ENDPOINT = "products";

    @GET(ENDPOINT)
    Call<ProductDTO[]> getProducts();
    @GET(ENDPOINT + "/filter")
    Call<ProductDTO[]> getFilteredProducts(@Query("search") String search, @Query("categoryId") int categoryId, @Query("brandId") int brandId);

    @GET(ENDPOINT + "/detail")
    Call<ProductDetail> getProductbyId(@Query("Id") int id);
}
