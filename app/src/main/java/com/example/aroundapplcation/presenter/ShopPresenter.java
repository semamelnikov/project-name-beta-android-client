package com.example.aroundapplcation.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.ShopContract;
import com.example.aroundapplcation.services.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.ACCESS_TOKEN;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.PREMIUM_ACCOUNT;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.USER_ID;

public class ShopPresenter implements ShopContract.Presenter {
    private final ShopContract.View view;
    private final ApiInterface api;
    private final SharedPreferences sharedPreferences;

    private String accessToken;

    public ShopPresenter(final ShopContract.View view, final ApiInterface apiInterface,
                         final SharedPreferences sharedPreferences) {
        this.view = view;
        this.api = apiInterface;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void initPremiumAccountToggleButton() {
        final boolean isPremiumAccountEnabled = sharedPreferences.getBoolean(PREMIUM_ACCOUNT, false);
        view.updatePremiumAccountToggleButton(isPremiumAccountEnabled);
        view.updateSnoopersButton(isPremiumAccountEnabled);
    }

    @Override
    public void initAccessToken() {
        accessToken = sharedPreferences.getString(ACCESS_TOKEN, "undefined");
    }

    @Override
    public void updatePremiumAccountStatus(final boolean isChecked) {
        api.updateAccountPremiumStatus(accessToken, getUserId()).enqueue(getUpdatePremiumStatusCallback());
    }

    @Override
    public void loadSnoopersScreen() {
        view.navigateToSnoopersScreen();
    }

    private Callback<Boolean> getUpdatePremiumStatusCallback() {
        return new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                final boolean isCurrentUserPremium = response.body();
                sharedPreferences.edit().putBoolean(PREMIUM_ACCOUNT, isCurrentUserPremium).apply();
                view.updateSnoopersButton(isCurrentUserPremium);
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }

    private String getUserId() {
        final long userId = sharedPreferences.getLong(USER_ID, -1);
        return String.valueOf(userId);
    }
}
