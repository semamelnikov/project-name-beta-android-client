package com.example.aroundapplcation.contracts;

import com.example.aroundapplcation.model.BusinessCard;

public interface BusinessCardContract {
    interface View {
        void showBusinessCardFields(final BusinessCard businessCard);

        void showToast(final String message);

        void updateFavoriteToggleButton(final boolean isCardInFavorites);

        void updateEnableVkButtonState(final boolean isEnabled);

        void updateEnableInstagramButtonState(final boolean isEnabled);

        void navigateToVkApp(final String vkUri);

        void navigateToInstagramApp(final String instagramUri);

        void addListenerToFavoritesToggleButton();
    }

    interface Presenter {
        void initBusinessCardId();

        void getBusinessCard();

        void updateBusinessCardFavoritesStatus(final boolean isFavorite);

        void initBusinessCardFavoriteStatus();

        void initSocialMediaButtons();

        void loadVkApp();

        void loadInstagramApp();
    }
}
