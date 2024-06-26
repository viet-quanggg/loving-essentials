package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserProfileDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUserService {
    String ENDPOINT = "Users";
    @GET(ENDPOINT)
    Call<UserDTO[]> getAllUsers();
    @GET(ENDPOINT + "/user-detail/{id}")
    Call<UserProfileDTO> getUserProfile(@Path("id") int id);
    @PUT(ENDPOINT + "/udpate-profile")
    Call<UserProfileDTO> updateProfile(@Body UserProfileDTO user);
}
