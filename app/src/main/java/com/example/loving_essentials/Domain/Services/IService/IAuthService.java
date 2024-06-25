package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.LoginRequest;
import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.LoginResponse;
import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.RegisterRequest;
import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.RegisterResponse;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthService {
//    String ENDPOINT = "Auth";
//    @POST(ENDPOINT + "/login")
//    Call<LoginDTO> login(@Body LoginDTO loginDTO);
//    @POST(ENDPOINT + "/register")
//    Call<RegisterDTO> register(@Body RegisterDTO registerDTO);

    @POST("Auth/Login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("Auth/Register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);
}
