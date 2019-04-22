package com.example.aroundapplcation.services;

import com.example.aroundapplcation.model.EntryRequest;
import com.example.aroundapplcation.model.EntryResponse;
import com.example.aroundapplcation.model.LoginRequest;
import com.example.aroundapplcation.model.LoginResponse;
import com.example.aroundapplcation.model.PhoneCheckRequest;
import com.example.aroundapplcation.model.PhoneCheckResponse;
import com.example.aroundapplcation.model.RegistrationRequest;
import com.example.aroundapplcation.model.RegistrationResponse;
import com.example.aroundapplcation.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/auth/entry")
    Call<EntryResponse> sendPhoneNumber(@Body EntryRequest entryRequest);

    @POST("/auth/login")
    Call<LoginResponse> sendCredentials(@Body LoginRequest credentials);

    @POST("/auth/phone/check")
    Call<PhoneCheckResponse> sendCode(@Body PhoneCheckRequest phoneCheckRequest);

    @GET("/cards/users/{userId}")
    Call<User> getUser(@Header("Authorization") String accessToken, @Path("userId") int userId);

    @POST("/auth/registration")
    Call<RegistrationResponse> register(@Body RegistrationRequest registrationRequest);

    @GET("/cards/{id}")
    Call<User> getBusinessCard(@Header("Authorization") String accessToken, @Path("id") int id);
}
