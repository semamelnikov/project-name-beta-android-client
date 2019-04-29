package com.example.aroundapplcation.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.aroundapplcation.contracts.RegistrationContract;
import com.example.aroundapplcation.model.RegistrationRequest;
import com.example.aroundapplcation.model.RegistrationResponse;
import com.example.aroundapplcation.services.ApiInterface;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.IntentConstants.PHONE_NUMBER;
import static com.example.aroundapplcation.constants.IntentConstants.REGISTRATION_SESSION_ID;

public class RegistrationPresenter implements RegistrationContract.Presenter {
    private final RegistrationContract.View view;
    private final Intent intent;
    private final ApiInterface api;
    private final SharedPreferences sharedPreferences;
    private final RegistrationRequest registrationRequest;

    public RegistrationPresenter(final RegistrationContract.View view, final Intent intent,
                                 final ApiInterface api, final SharedPreferences sharedPreferences) {
        this.view = view;
        this.intent = intent;
        this.api = api;
        this.sharedPreferences = sharedPreferences;
        this.registrationRequest = new RegistrationRequest();
    }

    @Override
    public void initRegistrationRequest() {
        registrationRequest.setPhone(intent.getStringExtra(PHONE_NUMBER));
        registrationRequest.setRegistrationSessionId(intent.getStringExtra(REGISTRATION_SESSION_ID));

    }

    @Override
    public void saveRegistrationData(final String firstName, final String lastName,
                                     final String password, final String passwordCheck) {
        registrationRequest.setName(firstName);
        registrationRequest.setSurname(lastName);
        registrationRequest.setPassword(password);
        registrationRequest.setPasswordCheck(passwordCheck);
    }

    @Override
    public void sendRegistrationRequest() {
        api.sendRegistrationRequest(registrationRequest).enqueue(getRegistrationCallback());
    }

    private Callback<RegistrationResponse> getRegistrationCallback() {
        return new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if (registrationResponse != null) {
                    if (!"".equals(registrationResponse.getHashPassword())) {
                        view.showToast("Successful!");
                        sharedPreferences.edit().putLong("userId", registrationResponse.getId()).apply();
                        view.navigateToLoginScreen();
                    }
                } else {
                    view.showToast("Registration Session problems are occurred.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Login error", t.getMessage());
            }
        };
    }
}
