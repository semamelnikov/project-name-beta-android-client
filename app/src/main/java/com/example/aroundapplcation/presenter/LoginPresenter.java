package com.example.aroundapplcation.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.aroundapplcation.contracts.LoginContract;
import com.example.aroundapplcation.model.LoginRequest;
import com.example.aroundapplcation.model.LoginResponse;
import com.example.aroundapplcation.services.ApiInterface;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.ACCESS_TOKEN;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.PHONE_NUMBER;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.REFRESH_TOKEN;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.USER_ID;

public class LoginPresenter implements LoginContract.Presenter {


    private final LoginContract.View view;
    private final SharedPreferences sharedPreferences;
    private final ApiInterface api;
    private final LoginRequest loginRequest;

    public LoginPresenter(final LoginContract.View view,
                          final SharedPreferences sharedPreferences,
                          final ApiInterface api) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
        this.api = api;
        this.loginRequest = new LoginRequest();
    }


    @Override
    public void initLoginRequestByPhone() {
        loginRequest.setPhoneNumber(sharedPreferences.getString(PHONE_NUMBER, " "));
    }

    @Override
    public void savePassword(final String password) {
        loginRequest.setPassword(password);
    }

    @Override
    public void sendLoginRequest() {
        api.sendLoginRequest(loginRequest).enqueue(getLoginRequestCallback());
    }

    private Callback<LoginResponse> getLoginRequestCallback() {
        return new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                final LoginResponse loginResponse = response.body();
                if (loginResponse != null) {
                    final String accessToken = loginResponse.getAccessToken();
                    final String refreshToken = loginResponse.getRefreshToken();
                    final long userId = loginResponse.getUserId();
                    sharedPreferences.edit()
                            .putString(ACCESS_TOKEN, accessToken)
                            .putString(REFRESH_TOKEN, refreshToken)
                            .putLong(USER_ID, userId)
                            .apply();
                    view.navigateToBusinessCardsScreen();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                view.showErrorToast("Network error. Please, try later.");
                Log.e("Login error", t.getMessage());
            }
        };
    }
}
