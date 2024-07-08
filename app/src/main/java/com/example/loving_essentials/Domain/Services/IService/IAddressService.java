package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.Request.Address.AddressRequestDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.Request.Address.UpdateAddressDto;
import com.example.loving_essentials.Domain.Entity.DTOs.Response.Address.AddressResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAddressService {

    @GET("Address/GetAddressByUser/{id}")
    Call<AddressResponseDto[]> GetAddressByUserId(@Path("id") int userId);

    @POST("Address/Add-address")
    Call<Boolean> AddAddress(@Body AddressRequestDTO addressRequest);

    @DELETE("Address/DeleteAddress/{id}")
    Call<Boolean> DeleteAddress(@Path("id") int addId);

    @PUT("Address/UpdateAddress")
    Call<Boolean> UpdateAddress(@Body UpdateAddressDto updateAddressDto);
}
