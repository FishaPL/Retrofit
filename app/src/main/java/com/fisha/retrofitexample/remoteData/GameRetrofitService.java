package com.fisha.retrofitexample.remoteData;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameRetrofitService
{
    private static final String BASE_URL = "https://api.igdb.com/v4/";
    private static final String CLIENT_ID = "";
    private static final String TOKEN = "";
    private static Retrofit INSTANCE;

    private static final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + TOKEN)
                    .addHeader("Client-ID", CLIENT_ID)
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    private static Retrofit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }

    public static GameApi getInterface() {
        return getInstance().create(GameApi.class);
    }
}
