package com.example.aroundapplcation.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.BusinessCardContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.presenter.BusinessCardPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

public class BusinessCardActivity extends AppCompatActivity implements BusinessCardContract.View {

    private BusinessCardContract.Presenter presenter;

    private TextView tvName;
    private TextView tvSurname;
    private TextView tvPhone;

    private Button vkButton;
    private Button instagramButton;

    private ToggleButton favoritesToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        initPresenter();

        initFields();

        presenter.initBusinessCardId();
        presenter.initAccessToken();
        presenter.getBusinessCard();

        presenter.initBusinessCardFavoriteStatus();

        presenter.initSocialMediaButtons();

        addFavoritesToggleListener();
    }

    @Override
    public void showBusinessCardFields(final BusinessCard businessCard) {
        tvName.setText(businessCard.getName());
        tvSurname.setText(businessCard.getSurname());
        tvPhone.setText(businessCard.getPhone());
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateFavoriteToggleButton(final boolean isCardInFavorites) {
        favoritesToggleButton.setChecked(isCardInFavorites);
    }

    @Override
    public void updateEnableVkButtonState(final boolean isEnabled) {
        vkButton.setEnabled(isEnabled);
    }

    @Override
    public void updateEnableInstagramButtonState(final boolean isEnabled) {
        instagramButton.setEnabled(isEnabled);
    }

    @Override
    public void navigateToVkApp(final String vkUri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(vkUri));
        startActivity(intent);
    }

    @Override
    public void navigateToInstagramApp(final String instagramUri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUri));
        startActivity(intent);
    }

    public void onVkButtonClick(View view) {
        presenter.loadVkApp();
    }

    public void onInstagramButtonClick(View view) {
        presenter.loadInstagramApp();
    }

    private void initPresenter() {
        final Intent intent = getIntent();
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        presenter = new BusinessCardPresenter(this, intent, sharedPreferences, api);
    }

    private void initFields() {
        tvName = findViewById(R.id.name);
        tvSurname = findViewById(R.id.surname);
        tvPhone = findViewById(R.id.phone);

        vkButton = findViewById(R.id.vk_button);
        instagramButton = findViewById(R.id.instagram_button);

        favoritesToggleButton = findViewById(R.id.favorites_toggle_button);
    }

    private void addFavoritesToggleListener() {
        favoritesToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.updateBusinessCardFavoritesStatus(isChecked);
            }
        });
    }
}
