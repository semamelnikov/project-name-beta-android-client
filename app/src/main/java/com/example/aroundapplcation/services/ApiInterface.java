package com.example.aroundapplcation.services;

import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.model.EntryRequest;
import com.example.aroundapplcation.model.EntryResponse;
import com.example.aroundapplcation.model.FavoriteCardAddRequest;
import com.example.aroundapplcation.model.LoginRequest;
import com.example.aroundapplcation.model.LoginResponse;
import com.example.aroundapplcation.model.PhoneCheckRequest;
import com.example.aroundapplcation.model.PhoneCheckResponse;
import com.example.aroundapplcation.model.RegistrationRequest;
import com.example.aroundapplcation.model.RegistrationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("/cards/{id}")
    Call<BusinessCard> updateBusinessCard(@Header("Authorization") String accessToken,
                                          @Path("id") int id,
                                          @Body BusinessCard businessCard);

    @GET("/cards/favorites")
    Call<List<BusinessCard>> getFavoritesBusinessCards(@Header("Authorization") String accessToken);

    @POST("/cards/favorites")
    Call<Void> addCardIntoFavorites(@Header("Authorization") String accessToken,
                                    @Body FavoriteCardAddRequest favoriteCardAddRequest);

    @DELETE("/cards/favorites/{cardId}")
    Call<Void> removeCardFromFavorites(@Header("Authorization") String accessToken,
                                       @Path("cardId") int cardId);

    @GET("/cards/favorites/{cardId}/check")
    Call<Boolean> isCardInFavorites(@Header("Authorization") String accessToken,
                                    @Path("cardId") int cardId);

    @GET("/premium/update/{userId}")
    Call<Boolean> updateAccountPremiumStatus(@Header("Authorization") String accessToken,
                                             @Path("userId") String userId);

    @GET("/premium/snoopers")
    Call<List<BusinessCard>> getSnoopers(@Header("Authorization") String accessToken);
}
