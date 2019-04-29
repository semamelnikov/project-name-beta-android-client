package com.example.aroundapplcation.contracts;

public interface ShopContract {
    interface View {
        void updatePremiumAccountToggleButton(final boolean isPremiumAccountEnabled);

        void updateSnoopersButton(final boolean isPremiumAccountEnabled);

        void showToast(final String message);

        void navigateToSnoopersScreen();
    }

    interface Presenter {
        void initPremiumAccountToggleButton();

        void initAccessToken();

        void updatePremiumAccountStatus(final boolean isChecked);

        void loadSnoopersScreen();
    }
}
