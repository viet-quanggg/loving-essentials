package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.ICartService;

public class CartService {
    public static ICartService getICartService() {
        return APIClient.getClient().create(ICartService.class);
    }
}
