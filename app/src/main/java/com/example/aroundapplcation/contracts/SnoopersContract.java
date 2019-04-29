package com.example.aroundapplcation.contracts;

import com.example.aroundapplcation.model.BusinessCard;

import java.util.List;

public interface SnoopersContract {
    interface View {

        void initAdapter(final List<BusinessCard> items);

        void updateSnoopers();

        void navigateToBusinessCardScreen(final Integer businessCardId);

        void showToast(final String message);
    }

    interface Presenter {
        void initSnoopers();

        void initAccessToken();

        void loadBusinessCardScreen(final int position);

        void initAdapter();
    }
}
