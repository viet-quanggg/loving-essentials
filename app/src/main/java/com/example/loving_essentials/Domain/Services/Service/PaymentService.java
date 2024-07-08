package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IPaymentService;
import com.example.loving_essentials.Domain.Services.IService.IProductService;

public class PaymentService {
    public static IPaymentService getPaymentService() {
        return APIClient.getClient().create(IPaymentService.class);
    }
}
