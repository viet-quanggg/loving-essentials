package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.OSRMResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOSRMService {
    @GET("route/v1/driving/{coordinates}")
    Call<OSRMResponse> getRoute(
            @Path("coordinates") String coordinates,
            @Query("overview") String overview,
            @Query("geometries") String geometries
    );
}
