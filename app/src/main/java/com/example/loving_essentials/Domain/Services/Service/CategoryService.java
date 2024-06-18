package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.ICategoryService;
import com.example.loving_essentials.Domain.Services.IService.IProductService;

public class CategoryService {
    public static ICategoryService getCategoryService() {
        return APIClient.getClient().create(ICategoryService.class);
    }
}
