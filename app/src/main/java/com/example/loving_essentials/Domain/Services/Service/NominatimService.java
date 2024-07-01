package com.example.loving_essentials.Domain.Services.Service;

import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.INominatimService;

public class NominatimService {
    public static INominatimService getNominatimService() {
        return APIClient.getNominatimClient().create(INominatimService.class);
    }
}
