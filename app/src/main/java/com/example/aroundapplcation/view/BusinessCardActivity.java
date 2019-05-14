package com.example.aroundapplcation.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.BusinessCardContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.presenter.BusinessCardPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;
import com.mikhaellopez.circularimageview.CircularImageView;

public class BusinessCardActivity extends BaseActivity implements BusinessCardContract.View {

    private BusinessCardContract.Presenter presenter;

    private TextView tvName;
    private TextView tvSurname;
    private TextView tvPhone;

    private CircularImageView iconImageView;

    private ImageView vkImageView;
    private ImageView facebookImageView;
    private ImageView instagramImageView;
    private ImageView twitterImageView;

    private ImageView favoritesImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        initPresenter();

        initFields();

        presenter.initBusinessCardId();
        presenter.getBusinessCard();

        presenter.initBusinessCardFavoriteStatus();
    }

    @Override
    public void showBusinessCardFields(final BusinessCard businessCard) {
        tvName.setText(businessCard.getName());
        tvSurname.setText(businessCard.getSurname());
        tvPhone.setText(businessCard.getPhone());

        final String iconUri = businessCard.getIconUri();
        if (iconUri != null && !"".equals(iconUri)) {
            Glide.with(this)
                    .load(iconUri)
                    .placeholder(R.drawable.person_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(400, 400)
                    .into(iconImageView);
        } else {
            iconImageView.setImageResource(R.drawable.person_default);
        }
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateFavoriteToggleButton(final boolean isCardInFavorites) {
        favoritesImageView.setImageResource(
                isCardInFavorites ? R.drawable.ic_in_favorites : R.drawable.ic_not_in_favorites
        );
        favoritesImageView.setTag(
                isCardInFavorites ? R.drawable.ic_in_favorites : R.drawable.ic_not_in_favorites
        );
    }

    @Override
    public void updateEnableVkButtonState(final boolean isEnabled) {
        vkImageView.setEnabled(isEnabled);
    }

    @Override
    public void updateEnableInstagramButtonState(final boolean isEnabled) {
        instagramImageView.setEnabled(isEnabled);
    }

    @Override
    public void updateEnableFacebookButtonState(boolean isEnabled) {
        facebookImageView.setEnabled(isEnabled);
    }

    @Override
    public void updateEnableTwitterButtonState(boolean isEnabled) {
        twitterImageView.setEnabled(isEnabled);
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

    @Override
    public void navigateToFacebookApp(final String facebookUri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUri));
        startActivity(intent);
    }

    @Override
    public void navigateToTwitterApp(final String twitterUri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUri));
        startActivity(intent);
    }

    @Override
    public void addListenerToFavoritesToggleButton() {
        addFavoritesToggleListener();
    }

    public void onVkButtonClick(View view) {
        presenter.loadVkApp();
    }

    public void onInstagramButtonClick(View view) {
        presenter.loadInstagramApp();
    }

    public void onFacebookButtonClick(View view) {
        presenter.loadFacebookApp();
    }

    public void onTwitterButtonClick(View view) {
        presenter.loadTwitterApp();
    }

    private void initPresenter() {
        final Intent intent = getIntent();
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance(sharedPreferences).getApiInterface();
        presenter = new BusinessCardPresenter(this, intent, api);
    }

    private void initFields() {
        tvName = findViewById(R.id.name);
        tvSurname = findViewById(R.id.surname);
        tvPhone = findViewById(R.id.phone);

        vkImageView = findViewById(R.id.iv_card_vk);
        facebookImageView = findViewById(R.id.iv_card_facebook);
        instagramImageView = findViewById(R.id.iv_card_instagram);
        twitterImageView = findViewById(R.id.iv_card_twitter);

        favoritesImageView = findViewById(R.id.iv_card_favorites);

        iconImageView = findViewById(R.id.iv_card_icon);

        initToolbar(R.id.business_card_toolbar, true);
    }

    private void addFavoritesToggleListener() {
        favoritesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = false;
                if (favoritesImageView.getTag() != null) {
                    int currentFavoritesTag = (int) favoritesImageView.getTag();
                    if (currentFavoritesTag == R.drawable.ic_not_in_favorites) {
                        isChecked = true;
                    }
                }
                presenter.updateBusinessCardFavoritesStatus(isChecked);
            }
        });
    }
}
