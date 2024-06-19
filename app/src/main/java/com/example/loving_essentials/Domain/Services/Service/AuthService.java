package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IAuthService;

public class AuthService {
    public static IAuthService getAuthService() {
        return APIClient.getClient().create(IAuthService.class);
    }
}
