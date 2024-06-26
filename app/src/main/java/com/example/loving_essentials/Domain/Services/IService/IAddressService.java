package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.Request.Address.UpdateAddressDto;
import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAddressService {

    @GET("/api/Address/GetAddressByUser/{id}")
    Call<AddressResponseDto[]> GetAddressByUserId(@Path("id") int userId);

    @DELETE("/api/Address/DeleteAddress/{id}")
    Call<Boolean> DeleteAddress(@Path("id") int addId);

    @PUT("/api/Address/UpdateAddress")
    Call<Boolean> UpdateAddress(@Body UpdateAddressDto updateAddressDto);
}
