package com.example.aroundapplcation.contracts;

import com.example.aroundapplcation.model.BusinessCard;

import java.util.List;

public interface FavoritesContract {
    interface View {

        void navigateToBusinessCardScreen(final Integer businessCardId);

        void initAdapter(final List<BusinessCard> favorites);

        void updateFavorites();

        void showToast(final String message);
    }

    interface Presenter {
        void loadBusinessCardScreen(final int position);

        void initAdapter();

        void initFavorites();
    }
}
