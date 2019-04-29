package com.example.aroundapplcation.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.BusinessCardContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.model.FavoriteCardAddRequest;
import com.example.aroundapplcation.services.ApiInterface;

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

    @Override
    public void updateBusinessCardFavoritesStatus(final boolean isFavorite) {
        if (isFavorite) {
            final FavoriteCardAddRequest favoriteCardAddRequest = new FavoriteCardAddRequest();
            favoriteCardAddRequest.setCardId(businessCardId);
            api.addCardIntoFavorites(accessToken, favoriteCardAddRequest).enqueue(getAddIntoFavoritesCallback());
        } else {
            api.removeCardFromFavorites(accessToken, businessCardId).enqueue(getDeleteFromFavoritesCallback());
        }
    }

    @Override
    public void initBusinessCardFavoriteStatus() {
        api.isCardInFavorites(accessToken, businessCardId).enqueue(getIsCardInFavoritesCallback());
    }

    private Callback<Boolean> getIsCardInFavoritesCallback() {
        return new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                final boolean isCardInFavorites = response.body();
                view.updateFavoriteToggleButton(isCardInFavorites);
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
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

    private Callback<Void> getAddIntoFavoritesCallback() {
        return new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                view.showToast("Successful!");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }

    private Callback<Void> getDeleteFromFavoritesCallback() {
        return new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                view.showToast("Successful!");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }
}
