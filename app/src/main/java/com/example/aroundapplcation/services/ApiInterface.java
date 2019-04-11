package com.example.aroundapplcation.services;

import com.example.aroundapplcation.model.EntryRequest;
import com.example.aroundapplcation.model.EntryResponse;
import com.example.aroundapplcation.model.LoginRequest;
import com.example.aroundapplcation.model.LoginResponse;
import com.example.aroundapplcation.model.PhoneCheckRequest;
import com.example.aroundapplcation.model.PhoneCheckResponse;
import com.example.aroundapplcation.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/auth/entry")
    public Call<EntryResponse> sendPhoneNumber(@Body EntryRequest entryRequest);

    @POST("/auth/login")
    public Call<LoginResponse> sendCredentials(@Body LoginRequest credentials);

    @POST("/auth/phone/check")
    public Call<PhoneCheckResponse> sendCode(@Body PhoneCheckRequest phoneCheckRequest);

    @GET("/cards/users/{userId}")
    public Call<User> getUser(@Header("Authorization") String accessToken, @Path("userId") int userId);

}
