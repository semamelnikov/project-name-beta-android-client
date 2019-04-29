package com.example.aroundapplcation.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.SnoopersContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.services.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.ACCESS_TOKEN;

public class SnoopersPresenter implements SnoopersContract.Presenter {
    private final SnoopersContract.View view;
    private final ApiInterface api;
    private final SharedPreferences sharedPreferences;
    private final List<BusinessCard> snoopers;

    private String accessToken;

    public SnoopersPresenter(final SnoopersContract.View view, final ApiInterface api,
                             final SharedPreferences sharedPreferences) {
        this.view = view;
        this.api = api;
        this.sharedPreferences = sharedPreferences;
        this.snoopers = new ArrayList<>();
    }

    @Override
    public void initAccessToken() {
        accessToken = sharedPreferences.getString(ACCESS_TOKEN, "undefined");
    }

    @Override
    public void loadBusinessCardScreen(final int position) {
        final Integer businessCardId = snoopers
                .get(position)
                .getId();
        view.navigateToBusinessCardScreen(businessCardId);
    }

    @Override
    public void initAdapter() {
        view.initAdapter(snoopers);
    }

    @Override
    public void initSnoopers() {
        api.getSnoopers(accessToken).enqueue(getSnoopersCallback());
    }

    private Callback<List<BusinessCard>> getSnoopersCallback() {
        return new Callback<List<BusinessCard>>() {
            @Override
            public void onResponse(@NonNull Call<List<BusinessCard>> call, @NonNull Response<List<BusinessCard>> response) {
                final List<BusinessCard> receivedSnoopers = response.body();
                snoopers.clear();
                snoopers.addAll(receivedSnoopers);
                view.updateSnoopers();
            }

            @Override
            public void onFailure(@NonNull Call<List<BusinessCard>> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }
}
