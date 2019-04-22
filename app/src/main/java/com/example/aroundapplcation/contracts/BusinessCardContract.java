package com.example.aroundapplcation.contracts;

import com.example.aroundapplcation.model.BusinessCard;

public interface BusinessCardContract {
    interface View {
        void showBusinessCardFields(final BusinessCard businessCard);

        void showToast(final String message);
    }

    interface Presenter {
        void initBusinessCardId();

        void initAccessToken();

        void getBusinessCard();
    }
}
