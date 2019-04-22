package com.example.aroundapplcation.services;

import com.example.aroundapplcation.model.EntryRequest;
import com.example.aroundapplcation.model.EntryResponse;
import com.example.aroundapplcation.model.LoginRequest;
import com.example.aroundapplcation.model.LoginResponse;
import com.example.aroundapplcation.model.PhoneCheckRequest;
import com.example.aroundapplcation.model.PhoneCheckResponse;
import com.example.aroundapplcation.model.RegistrationRequest;
import com.example.aroundapplcation.model.RegistrationResponse;
import com.example.aroundapplcation.model.BusinessCard;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/auth/entry")
    Call<EntryResponse> sendEntryRequest(@Body EntryRequest entryRequest);

    @POST("/auth/login")
    Call<LoginResponse> sendLoginRequest(@Body LoginRequest credentials);

    @POST("/auth/phone/check")
    Call<PhoneCheckResponse> sendCode(@Body PhoneCheckRequest phoneCheckRequest);

    @GET("/cards/users/{userId}")
    Call<BusinessCard> getBusinessCardByUserId(@Header("Authorization") String accessToken, @Path("userId") int userId);

    @POST("/auth/registration")
    Call<RegistrationResponse> sendRegistrationRequest(@Body RegistrationRequest registrationRequest);

    @GET("/cards/{id}")
    Call<BusinessCard> getBusinessCard(@Header("Authorization") String accessToken, @Path("id") int id);
}
