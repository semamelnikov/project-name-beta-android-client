package com.example.aroundapplcation.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.BusinessCardsContract;
import com.example.aroundapplcation.model.AdvertiserBusinessCard;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.services.ApiInterface;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.ACCESS_TOKEN;
import static com.example.aroundapplcation.constants.SharedPreferencesConstants.USER_ID;

public class BusinessCardsPresenter implements BusinessCardsContract.Presenter {
    private static final String APP_NEARBY_ID = "com.example.around.application";

    private final BusinessCardsContract.View view;
    private final ConnectionsClient connectionsClient;
    private final SharedPreferences sharedPreferences;
    private final ApiInterface api;

    private final List<AdvertiserBusinessCard> advertiserBusinessCards;

    public BusinessCardsPresenter(final BusinessCardsContract.View view, final ConnectionsClient connectionsClient,
                                  final SharedPreferences sharedPreferences, final ApiInterface api) {
        this.view = view;
        this.connectionsClient = connectionsClient;
        this.sharedPreferences = sharedPreferences;
        this.api = api;
        this.advertiserBusinessCards = new ArrayList<>();
    }

    @Override
    public void loadBusinessCardScreen(final int businessCardPosition) {
        final Integer businessCardId = advertiserBusinessCards
                .get(businessCardPosition)
                .getBusinessCard()
                .getId();
        view.navigateToBusinessCardScreen(businessCardId);
    }

    @Override
    public void startAdvertising() {
        final AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        connectionsClient.startAdvertising(
                getUserId(),
                APP_NEARBY_ID,
                getConnectionLifecycleCallback(),
                advertisingOptions);
    }

    @Override
    public void startDiscovering() {
        final DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        connectionsClient.startDiscovery(
                APP_NEARBY_ID,
                getEndpointDiscoveryCallback(),
                discoveryOptions);
    }

    @Override
    public void initAdapter() {
        view.initAdapter(advertiserBusinessCards);
    }

    @Override
    public void loadShopScreen() {
        view.navigateToShopScreen();
    }

    @Override
    public void loadFavoritesScreen() {
        view.navigateToFavoritesScreen();
    }

    @Override
    public void loadProfileScreen() {
        view.navigateToProfileScreen();
    }

    @Override
    public void stopDiscovery() {
        connectionsClient.stopDiscovery();
    }

    @Override
    public void stopAdvertising() {
        connectionsClient.stopAdvertising();
    }

    private EndpointDiscoveryCallback getEndpointDiscoveryCallback() {
        return new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NonNull final String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                final int userId = Integer.parseInt(discoveredEndpointInfo.getEndpointName());
                final String accessToken = sharedPreferences.getString(ACCESS_TOKEN, "unknown");
                api.getBusinessCardByUserId(accessToken, userId).enqueue(getBusinessCardByUserIdCallback(endpointId));
            }

            @Override
            public void onEndpointLost(@NonNull String endpointId) {
                AdvertiserBusinessCard lostCard = null;
                for (AdvertiserBusinessCard advertiserBusinessCard : advertiserBusinessCards) {
                    if (endpointId.equals(advertiserBusinessCard.getEndpointId())) {
                        lostCard = advertiserBusinessCard;
                    }
                }
                if (lostCard != null) {
                    advertiserBusinessCards.remove(lostCard);
                    view.updateBusinessCards();
                }
            }
        };
    }

    private Callback<BusinessCard> getBusinessCardByUserIdCallback(final String endpointId) {
        return new Callback<BusinessCard>() {
            @Override
            public void onResponse(@NonNull Call<BusinessCard> call, @NonNull Response<BusinessCard> response) {
                final BusinessCard businessCard = response.body();
                final AdvertiserBusinessCard advertiserBusinessCard = new AdvertiserBusinessCard();
                advertiserBusinessCard.setBusinessCard(businessCard);
                advertiserBusinessCard.setEndpointId(endpointId);
                advertiserBusinessCards.add(advertiserBusinessCard);

                view.updateBusinessCards();
            }

            @Override
            public void onFailure(@NonNull Call<BusinessCard> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }

    private ConnectionLifecycleCallback getConnectionLifecycleCallback() {
        return new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {

            }

            @Override
            public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {

            }

            @Override
            public void onDisconnected(@NonNull String s) {

            }
        };
    }

    private String getUserId() {
        final long userId = sharedPreferences.getLong(USER_ID, -1);
        return String.valueOf(userId);
    }
}
