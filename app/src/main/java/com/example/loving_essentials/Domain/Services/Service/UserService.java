package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IUserService;

public class UserService {
    public static IUserService getUserService() {
        return APIClient.getClient().create(IUserService.class);
    }
}
