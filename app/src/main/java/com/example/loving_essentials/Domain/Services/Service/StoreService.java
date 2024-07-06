package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IStoreService;

public class StoreService {
    public static IStoreService getStoreService(){
        return APIClient.getClient().create(IStoreService.class);
    }
}
