package com.example.loving_essentials.Domain.Services.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //Thằng nào muốn xài cái này tự deploy lên ngrok r xài nhé. Cái url này của t thôi

//    private static String baseUrl = "https://8079-113-23-109-21.ngrok-free.app/api/";

//    private static final String BASE_URL = "https://3831-27-74-201-205.ngrok-free.app/";


    private static final String BASE_URL = "https://695b-58-187-191-147.ngrok-free.app/api/";

    private static Retrofit retrofit;
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // 30 seconds
            .readTimeout(60, TimeUnit.SECONDS) // 30 seconds
            .writeTimeout(30, TimeUnit.SECONDS) // 30 seconds
            .build();
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
