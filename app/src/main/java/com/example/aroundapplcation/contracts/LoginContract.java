package com.example.aroundapplcation.contracts;

public interface LoginContract {
    interface View {
        void navigateToBusinessCardsScreen();

        void showErrorToast(final String errorMessage);
    }

    interface Presenter {
        void initLoginRequestByPhone();

        void savePassword(final String password);

        void sendLoginRequest();
    }
}
