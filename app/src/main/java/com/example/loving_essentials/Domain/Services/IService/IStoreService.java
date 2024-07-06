package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.StoreDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IStoreService {

    @GET()
    Call<List<StoreDTO>> GetStores();

    @GET("{id}")
    Call<StoreDTO> GetStoreById(@Path("id") int id);

    @POST()
    Call<Void> CreateStore(@Body StoreDTO store);

    @PUT()
    Call<Void> UpdateStore(@Body StoreDTO store);

    @DELETE("{id}")
    Call<Void> DeleteStore(@Path("id") int id);
}
