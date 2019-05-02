package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.ShopContract;
import com.example.aroundapplcation.presenter.ShopPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

public class ShopActivity extends AppCompatActivity implements ShopContract.View {

    private ShopContract.Presenter presenter;

    private ToggleButton premiumAccountPremiumButton;
    private Button snoopersCheckButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        initPresenter();

        initFields();

        presenter.initPremiumAccountToggleButton();

        addPremiumButtonListener();
    }

    @Override
    public void updatePremiumAccountToggleButton(final boolean isPremiumAccountEnabled) {
        premiumAccountPremiumButton.setChecked(isPremiumAccountEnabled);
    }

    @Override
    public void updateSnoopersButton(final boolean isPremiumAccountEnabled) {
        snoopersCheckButton.setEnabled(isPremiumAccountEnabled);
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToSnoopersScreen() {
        Intent intent = new Intent(ShopActivity.this, SnoopersActivity.class);
        startActivity(intent);
    }

    public void snoopersButtonClick(View view) {
        presenter.loadSnoopersScreen();
    }

    private void initPresenter() {
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance(sharedPreferences).getApiInterface();
        presenter = new ShopPresenter(this, api, sharedPreferences);
    }

    private void initFields() {
        Toolbar toolbar = findViewById(R.id.shop_toolbar);
        setSupportActionBar(toolbar);

        snoopersCheckButton = findViewById(R.id.snoopers_check_button);
        snoopersCheckButton.setEnabled(false);

        premiumAccountPremiumButton = findViewById(R.id.premium_account_toggle);

    }

    private void addPremiumButtonListener() {
        premiumAccountPremiumButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.updatePremiumAccountStatus(isChecked);
            }
        });
    }
}
