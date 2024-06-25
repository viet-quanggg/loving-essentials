package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IAddressService;

public class AddressService {
    public static IAddressService geAddressService() {
        return APIClient.getClient().create(IAddressService.class);
    }
}
