package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.NominatimResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INominatimService {
    @GET("search")
    Call<List<NominatimResponse>> getCoordinates(
            @Query("q") String address,
            @Query("format") String format
    );

    @GET("reverse")
    Call<ResponseBody> reverseGeocode(
            @Query("format") String format,
            @Query("jsonv2") String jsonVersion,
            @Query("accept-language") String language,
            @Query("lat") double latitude,
            @Query("lon") double longitude
    );
}
