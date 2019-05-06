package com.example.aroundapplcation.presenter;

import android.content.Intent;
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

public class BusinessCardPresenter implements BusinessCardContract.Presenter {
    private final BusinessCardContract.View view;
    private final Intent intent;
    private final ApiInterface api;

    private Integer businessCardId;
    private BusinessCard businessCard;

    public BusinessCardPresenter(final BusinessCardContract.View view, final Intent intent,
                                 final ApiInterface api) {

        this.view = view;
        this.intent = intent;
        this.api = api;
    }

    @Override
    public void initBusinessCardId() {
        businessCardId = intent.getIntExtra(BUSINESS_CARD_ID, -1);
    }

    @Override
    public void getBusinessCard() {
        api.getBusinessCard(businessCardId).enqueue(getBusinessCardCallback());
    }

    @Override
    public void updateBusinessCardFavoritesStatus(final boolean isFavorite) {
        if (isFavorite) {
            final FavoriteCardAddRequest favoriteCardAddRequest = new FavoriteCardAddRequest();
            favoriteCardAddRequest.setCardId(businessCardId);
            api.addCardIntoFavorites(favoriteCardAddRequest).enqueue(getAddIntoFavoritesCallback());
        } else {
            api.removeCardFromFavorites(businessCardId).enqueue(getDeleteFromFavoritesCallback());
        }
    }

    @Override
    public void initBusinessCardFavoriteStatus() {
        api.isCardInFavorites(businessCardId).enqueue(getIsCardInFavoritesCallback());
    }

    @Override
    public void initSocialMediaButtons() {
        view.updateEnableVkButtonState(businessCard.getVkId() != null && !"".equals(businessCard.getVkId()));
        view.updateEnableInstagramButtonState(businessCard.getInstagramId() != null && !"".equals(businessCard.getInstagramId()));
    }

    @Override
    public void loadVkApp() {
        final String uri = "https://vk.com/" + businessCard.getVkId();
        view.navigateToVkApp(uri);
    }

    @Override
    public void loadInstagramApp() {
        final String uri = "https://www.instagram.com/" + businessCard.getInstagramId() + "/";
        view.navigateToInstagramApp(uri);
    }

    private Callback<Boolean> getIsCardInFavoritesCallback() {
        return new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                final boolean isCardInFavorites = response.body();
                view.updateFavoriteToggleButton(isCardInFavorites);
                view.addListenerToFavoritesToggleButton();
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
                initSocialMediaButtons();
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
