package com.example.aroundapplcation.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.ProfileContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.services.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.ACCESS_TOKEN;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.USER_ID;

public class ProfilePresenter implements ProfileContract.Presenter {
    private final ProfileContract.View view;
    private final SharedPreferences sharedPreferences;
    private final ApiInterface api;

    private String accessToken;
    private BusinessCard businessCard;

    public ProfilePresenter(final ProfileContract.View view, final SharedPreferences sharedPreferences,
                            final ApiInterface api) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
        this.api = api;
    }

    @Override
    public void initAccessToken() {
        accessToken = sharedPreferences.getString(ACCESS_TOKEN, "unknown");
    }

    @Override
    public void initBusinessCard() {
        api.getBusinessCardByUserId(accessToken, getUserId()).enqueue(getBusinessCardByUserIdCallback());
    }

    private Callback<BusinessCard> getBusinessCardByUserIdCallback() {
        return new Callback<BusinessCard>() {
            @Override
            public void onResponse(@NonNull Call<BusinessCard> call, @NonNull Response<BusinessCard> response) {
                businessCard = response.body();
                view.updateBusinessCardFields(businessCard);
            }

            @Override
            public void onFailure(@NonNull Call<BusinessCard> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }

    @Override
    public void updateBusinessCard() {
        api.updateBusinessCard(accessToken, businessCard.getId(), businessCard).enqueue(getUpdateBusinessCardCallback());
    }

    private Callback<BusinessCard> getUpdateBusinessCardCallback() {
        return new Callback<BusinessCard>() {
            @Override
            public void onResponse(@NonNull Call<BusinessCard> call, @NonNull Response<BusinessCard> response) {
                businessCard = response.body();
                view.updateBusinessCardFields(businessCard);
                view.showToast("Successful!");
            }

            @Override
            public void onFailure(@NonNull Call<BusinessCard> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }

    @Override
    public void savePhoneNumber(final String phone) {
        businessCard.setPhone(phone);
    }

    @Override
    public void saveName(final String name) {
        businessCard.setName(name);
    }

    @Override
    public void saveSurname(final String surname) {
        businessCard.setSurname(surname);
    }

    @Override
    public void saveVk(final String vkId) {
        businessCard.setVkId(vkId);
    }

    @Override
    public void saveInstagram(final String instagramId) {
        businessCard.setInstagramId(instagramId);
    }

    private Integer getUserId() {
        final Long userId = sharedPreferences.getLong(USER_ID, -1);
        return userId.intValue();
    }
}
