package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.Brand;
import com.example.loving_essentials.Domain.Entity.ProductDTO;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IBrandService {
    String ENDPOINT = "brands";

    @GET(ENDPOINT)
    Call<Brand[]> getBrands();
}
