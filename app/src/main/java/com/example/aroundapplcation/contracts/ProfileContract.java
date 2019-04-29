package com.example.aroundapplcation.contracts;

import com.example.aroundapplcation.model.BusinessCard;

public interface ProfileContract {
    interface View {

        void updateBusinessCardFields(final BusinessCard businessCard);

        void showToast(final String message);
    }

    interface Presenter {
        void initAccessToken();

        void initBusinessCard();

        void updateBusinessCard();

        void savePhoneNumber(final String phone);

        void saveName(final String name);

        void saveSurname(final String surname);
    }
}
