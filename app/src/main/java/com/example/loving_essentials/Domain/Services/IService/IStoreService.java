package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.StoreDTO;
import com.example.loving_essentials.Domain.Entity.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IStoreService {

    String ENDPOINT = "Store";

    @GET(ENDPOINT + "/{id}")
    Call<Store> getStoreById(@Path("id") int id);

}
