package com.example.loving_essentials.Domain.Services.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //Thằng nào muốn xài cái này tự deploy lên ngrok r xài nhé. Cái url này của t thôi
//    private static String baseUrl = "https://8079-113-23-109-21.ngrok-free.app/api/";
//    private static final String BASE_URL = "https://3831-27-74-201-205.ngrok-free.app/";
    private static String baseUrl = " https://2540-2402-800-63e9-18e4-cccb-fefc-d3be-b8c7.ngrok-free.app/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
