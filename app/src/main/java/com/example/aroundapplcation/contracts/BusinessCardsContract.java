package com.example.aroundapplcation.contracts;

import java.util.List;

public interface BusinessCardsContract {
    interface View {
        void showToast(final String message);

        void initAdapter(final List items);

        void updateBusinessCards();

        void navigateToBusinessCardScreen(final Integer businessCardId);
    }

    interface Presenter {
        void loadBusinessCardScreen(final int businessCardPosition);

        void startAdvertising();

        void startDiscovering();

        void initAdapter();
    }
}
