package com.example.aroundapplcation.contracts;

public interface PhoneCheckContract {
    interface View {
        void navigateToRegistrationScreen(final String phone, final String registrationSessionId);

        void showErrorToast(final String errorMessage);

        void navigateToEntryScreen();
    }

    interface Presenter {
        void initPhoneCheck();

        void saveCode(final String code);

        void sendPhoneCheckRequest();
    }
}
