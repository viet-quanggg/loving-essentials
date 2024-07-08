package com.example.loving_essentials.Domain.Services.Service;


import com.example.loving_essentials.Domain.Services.API.APIClient;
import com.example.loving_essentials.Domain.Services.IService.IOSRMService;

public class OSRMService {
    public static IOSRMService getOSRMService() {
        return APIClient.getOSRMClient().create(IOSRMService.class);
    }
}
