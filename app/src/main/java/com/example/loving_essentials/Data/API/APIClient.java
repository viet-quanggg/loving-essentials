package com.example.loving_essentials.Data.API;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
//    private static final String BASE_URL = "https://3831-27-74-201-205.ngrok-free.app/";
    private static final String BASE_URL = "https://3f69-27-74-201-205.ngrok-free.app/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //Khi nào cần check API connection thì gọi hàm này
    public static void checkAPIConnection() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("API Connection Successful. Response code: " + response.code());
                } else {
                    System.out.println("API Connection Failed. Response code: " + response.code());
                }
            }
        });
    }
}