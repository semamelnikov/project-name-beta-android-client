package com.example.aroundapplcation.contracts;

public interface RegistrationContract {
    interface View {
        void navigateToLoginScreen();

        void showToast(final String message);
    }

    interface Presenter {
        void sendRegistrationRequest();

        void saveRegistrationData(final String firstName, final String lastName,
                                  final String password, final String passwordCheck);

        void initRegistrationRequest();
    }
}
