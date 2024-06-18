package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IBrandService;
import com.example.loving_essentials.Domain.Services.IService.IProductService;

public class BrandService {
    public static IBrandService getBrandService() {
        return APIClient.getClient().create(IBrandService.class);
    }
}
