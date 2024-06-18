package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.Brand;
import com.example.loving_essentials.Domain.Entity.Category;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ICategoryService {
    String ENDPOINT = "categories";

    @GET(ENDPOINT)
    Call<Category[]> getCategories();
}
