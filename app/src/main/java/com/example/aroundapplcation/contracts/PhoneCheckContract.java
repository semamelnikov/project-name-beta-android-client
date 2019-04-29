package com.example.aroundapplcation.contracts;

public interface PhoneCheckContract {
    interface View {
        void navigateToRegistrationScreen(final String phone, final String registrationSessionId);

        void navigateToEntryScreen();

        void showToast(final String message);
    }

    interface Presenter {
        void initPhoneCheck();

        void saveCode(final String code);

        void sendPhoneCheckRequest();
    }
}
