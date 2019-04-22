package com.example.aroundapplcation.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.aroundapplcation.contracts.BusinessCardContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.services.ApiInterface;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.IntentConstants.BUSINESS_CARD_ID;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.ACCESS_TOKEN;

public class BusinessCardPresenter implements BusinessCardContract.Presenter {
    private final BusinessCardContract.View view;
    private final Intent intent;
    private final SharedPreferences sharedPreferences;
    private final ApiInterface api;

    private String accessToken;
    private Integer businessCardId;
    private BusinessCard businessCard;

    public BusinessCardPresenter(final BusinessCardContract.View view, final Intent intent,
                                 final SharedPreferences sharedPreferences, final ApiInterface api) {

        this.view = view;
        this.intent = intent;
        this.sharedPreferences = sharedPreferences;
        this.api = api;
    }


    @Override
    public void initBusinessCardId() {
        businessCardId = intent.getIntExtra(BUSINESS_CARD_ID, -1);
    }

    @Override
    public void initAccessToken() {
        accessToken = sharedPreferences.getString(ACCESS_TOKEN, "unknown");
    }

    @Override
    public void getBusinessCard() {
        api.getBusinessCard(accessToken, businessCardId).enqueue(getBusinessCardCallback());

    }

    private Callback<BusinessCard> getBusinessCardCallback() {
        return new Callback<BusinessCard>() {
            @Override
            public void onResponse(@NonNull Call<BusinessCard> call, @NonNull Response<BusinessCard> response) {
                businessCard = response.body();
                view.showBusinessCardFields(businessCard);
            }

            @Override
            public void onFailure(@NonNull Call<BusinessCard> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }
}
