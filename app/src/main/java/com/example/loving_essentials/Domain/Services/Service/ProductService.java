package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IProductService;

public class ProductService {
    public static IProductService getProductService() {
        return APIClient.getClient().create(IProductService.class);
    }
}
