package com.example.loving_essentials.Domain.Services.Service;
import com.example.loving_essentials.Domain.DTO.AuthDTO.LoginRequest;
import com.example.loving_essentials.Domain.DTO.AuthDTO.LoginResponse;
import com.example.loving_essentials.Domain.DTO.AuthDTO.RegisterRequest;
import com.example.loving_essentials.Domain.DTO.AuthDTO.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/Auth/Login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("api/Auth/Register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);
}