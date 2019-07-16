package com.example.aroundapplcation.services;

import android.content.SharedPreferences;

import com.example.aroundapplcation.http.TokenInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static final String BASE_URL = "https://around-project.herokuapp.com";

    private static NetworkService networkService;

    private Retrofit retrofit;

    private NetworkService(final SharedPreferences sharedPreferences) {

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(sharedPreferences, BASE_URL))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static NetworkService getInstance(SharedPreferences sharedPreferences) {
        if (networkService == null) {
            networkService = new NetworkService(sharedPreferences);
        }
        return networkService;
    }

    public ApiInterface getApiInterface() {
        return retrofit.create(ApiInterface.class);
    }
}
