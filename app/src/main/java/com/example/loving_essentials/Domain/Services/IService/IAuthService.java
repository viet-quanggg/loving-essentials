package com.example.loving_essentials.Domain.Services.IService;

import com.example.loving_essentials.Domain.Entity.DTOs.LoginDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.RegisterDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthService {
    String ENDPOINT = "Auth";
    @POST(ENDPOINT + "/login")
    Call<LoginDTO> login(@Body LoginDTO loginDTO);
    @POST(ENDPOINT + "/register")
    Call<RegisterDTO> register(@Body RegisterDTO registerDTO);
}
