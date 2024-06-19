package com.example.loving_essentials.Domain.Services.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //Thằng nào muốn xài cái này tự deploy lên ngrok r xài nhé. Cái url này của t thôi
    private static String baseUrl = "https://97ac-2001-ee0-4f48-c8c0-1d58-6a1c-a7e5-de45.ngrok-free.app/api/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
