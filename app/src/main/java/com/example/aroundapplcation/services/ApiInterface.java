package com.example.aroundapplcation.services;

import com.example.aroundapplcation.model.PhoneResponse;
import com.example.aroundapplcation.model.PhoneNumber;
import com.example.aroundapplcation.model.TokenResponse;
import com.example.aroundapplcation.model.User;
import com.example.aroundapplcation.model.UserCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/auth/entry")
    public Call<PhoneResponse> sendPhoneNumber(@Body PhoneNumber phoneNumber);

    @POST("/auth/login")
    public Call<TokenResponse> sendCredentials(@Body UserCredentials credentials);

    @GET("/cards/users/{userId}")
    public Call<User> getUser(@Header("Authorization") String accessToken, @Path("userId") int userId);
}
