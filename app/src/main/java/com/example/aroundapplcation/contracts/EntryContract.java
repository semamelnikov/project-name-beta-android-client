package com.example.aroundapplcation.contracts;

public interface EntryContract {
    interface View {
        void navigateToLoginScreen(final String phoneNumber);

        void navigateToRegistrationScreen(final String phoneNumber, final String registrationSessionId);

        void navigateToPhoneCheckScreen(final String phoneNumber, final String registrationSessionId);

        void showErrorToast(final String errorMessage);
    }

    interface Presenter {
        void savePhoneNumber(final String phone);

        void saveExistingPhoneNumberToStorage();

        void sendEntryRequest();
    }
}
