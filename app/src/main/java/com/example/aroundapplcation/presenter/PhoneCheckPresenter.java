package com.example.aroundapplcation.presenter;

import android.content.Intent;
import android.util.Log;

import com.example.aroundapplcation.contracts.PhoneCheckContract;
import com.example.aroundapplcation.model.PhoneCheckRequest;
import com.example.aroundapplcation.model.PhoneCheckResponse;
import com.example.aroundapplcation.services.ApiInterface;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.IntentConstants.PHONE_NUMBER;
import static com.example.aroundapplcation.constants.IntentConstants.REGISTRATION_SESSION_ID;

public class PhoneCheckPresenter implements PhoneCheckContract.Presenter {
    private final PhoneCheckContract.View view;
    private final Intent intent;
    private final ApiInterface api;
    private final PhoneCheckRequest phoneCheckRequest;

    public PhoneCheckPresenter(final PhoneCheckContract.View view, final Intent intent, final ApiInterface api) {
        this.view = view;
        this.intent = intent;
        this.api = api;
        this.phoneCheckRequest = new PhoneCheckRequest();
    }


    @Override
    public void initPhoneCheck() {
        phoneCheckRequest.setPhone(intent.getStringExtra(PHONE_NUMBER));
        phoneCheckRequest.setRegistrationSessionId(REGISTRATION_SESSION_ID);
    }

    @Override
    public void saveCode(final String code) {
        phoneCheckRequest.setCode(code);
    }

    @Override
    public void sendPhoneCheckRequest() {
        api.sendCode(phoneCheckRequest).enqueue(getPhoneCheckCallback());
    }

    private Callback<PhoneCheckResponse> getPhoneCheckCallback() {
        return new Callback<PhoneCheckResponse>() {
            @Override
            public void onResponse(@NonNull Call<PhoneCheckResponse> call, @NonNull Response<PhoneCheckResponse> response) {
                PhoneCheckResponse phoneCheckResponse = response.body();
                if (phoneCheckResponse != null) {
                    if (phoneCheckResponse.isCodeChecked()) {
                        view.navigateToRegistrationScreen(
                                phoneCheckRequest.getPhone(),
                                phoneCheckRequest.getRegistrationSessionId());
                    } else {
                        if (phoneCheckResponse.isCodeInvalid()) {
                            view.showErrorToast("Sorry, the code you entered is incorrect!");
                        }
                        if (phoneCheckResponse.isExpirationTimeOver()) {
                            view.showErrorToast("Sorry, the code you entered was expired!");
                        }
                        if (phoneCheckResponse.isAttemptLimitOver()) {
                            view.showErrorToast("Sorry, you reached the limit of attempts to enter code!");
                            view.navigateToEntryScreen();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhoneCheckResponse> call, @NonNull Throwable t) {
                view.showErrorToast("Network error. Please, try later.");
                Log.e("Login error", t.getMessage());
            }
        };
    }
}
