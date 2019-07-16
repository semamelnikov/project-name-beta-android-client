package com.example.aroundapplcation.contracts;

public interface LoginContract {
    interface View {
        void navigateToBusinessCardsScreen();

        void showToast(final String message);
    }

    interface Presenter {
        void initLoginRequestByPhone();

        void savePassword(final String password);

        void sendLoginRequest();
    }
}
