package com.example.aroundapplcation.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.FavoritesContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.services.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesPresenter implements FavoritesContract.Presenter {
    private final FavoritesContract.View view;
    private final ApiInterface api;
    private final List<BusinessCard> favorites;

    public FavoritesPresenter(final FavoritesContract.View view, final ApiInterface api) {
        this.view = view;
        this.api = api;
        this.favorites = new ArrayList<>();
    }

    @Override
    public void loadBusinessCardScreen(final int position) {
        final Integer businessCardId = favorites
                .get(position)
                .getId();
        view.navigateToBusinessCardScreen(businessCardId);
    }

    @Override
    public void initAdapter() {
        view.initAdapter(favorites);
    }

    @Override
    public void initFavorites() {
        api.getFavoritesBusinessCards().enqueue(getFavoritesCallback());
    }

    private Callback<List<BusinessCard>> getFavoritesCallback() {
        return new Callback<List<BusinessCard>>() {
            @Override
            public void onResponse(@NonNull Call<List<BusinessCard>> call, @NonNull Response<List<BusinessCard>> response) {
                final List<BusinessCard> businessCards = response.body();
                favorites.clear();
                favorites.addAll(businessCards);
                view.updateFavorites();
            }

            @Override
            public void onFailure(@NonNull Call<List<BusinessCard>> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }
}
