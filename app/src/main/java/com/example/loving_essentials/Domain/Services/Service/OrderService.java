package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IOrderService;
import com.example.loving_essentials.Domain.Services.IService.IProductService;

public class OrderService {
    public static IOrderService getOrderService() {
        return APIClient.getClient().create(IOrderService.class);
    }
}
