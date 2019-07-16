package com.example.aroundapplcation.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.EntryContract;
import com.example.aroundapplcation.model.EntryRequest;
import com.example.aroundapplcation.model.EntryResponse;
import com.example.aroundapplcation.services.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.PHONE_NUMBER;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.REFRESH_TOKEN;

public class EntryPresenter implements EntryContract.Presenter {

    private EntryContract.View view;
    private EntryRequest entryRequest;

    private SharedPreferences sharedPreferences;
    private ApiInterface api;

    public EntryPresenter(final EntryContract.View view,
                          final SharedPreferences sharedPreferences,
                          final ApiInterface api) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
        this.api = api;
        this.entryRequest = new EntryRequest();
    }

    @Override
    public void savePhoneNumber(final String phone) {
        entryRequest.setPhoneNumber(phone);
    }

    @Override
    public void saveExistingPhoneNumberToStorage() {
        sharedPreferences.edit()
                .putString(PHONE_NUMBER, entryRequest.getPhoneNumber())
                .apply();
    }

    @Override
    public void sendEntryRequest() {
        api.sendEntryRequest(entryRequest).enqueue(getEntryRequestCallback());
    }

    @Override
    public void navigateLoggedInUser() {
        final String refreshToken = sharedPreferences.getString(REFRESH_TOKEN, "");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            view.navigateToBusinessCardsScreen();
        }
    }

    private Callback<EntryResponse> getEntryRequestCallback() {
        return new Callback<EntryResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntryResponse> call, @NonNull Response<EntryResponse> response) {
                EntryResponse entryResponse = response.body();
                if (entryResponse != null) {

                    Log.d("Entry response",
                            entryResponse.getRegistrationSessionId() + "\n" +
                                    entryResponse.getProcess() + "\n" +
                                    entryResponse.isCodeSent() + "\n" +
                                    entryResponse.isPhoneChecked() + "\n" +
                                    entryResponse.isProcessAvailable());

                    if (entryResponse.getProcess().equals("REGISTRATION")) {
                        if (!entryResponse.isProcessAvailable()) {
                            view.showToast("Sorry, this number was banned for some reason.");
                            return;
                        }
                        if (entryResponse.isPhoneChecked()) {
                            view.navigateToRegistrationScreen(
                                    entryRequest.getPhoneNumber(),
                                    entryResponse.getRegistrationSessionId());
                            return;
                        }
                        if (!entryResponse.isCodeSent()) {
                            view.showToast("Sorry, SMS can't be sent. Try again later.");
                            return;
                        }
                        view.navigateToPhoneCheckScreen(
                                entryRequest.getPhoneNumber(),
                                entryResponse.getRegistrationSessionId());
                    } else if (entryResponse.getProcess().equals("LOGIN")) {
                        view.navigateToLoginScreen(entryRequest.getPhoneNumber());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntryResponse> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }
}
