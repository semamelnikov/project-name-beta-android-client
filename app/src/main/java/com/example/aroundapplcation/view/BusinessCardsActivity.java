package com.example.aroundapplcation.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.adapter.BusinessCardsAdapter;
import com.example.aroundapplcation.constants.Constants;
import com.example.aroundapplcation.contracts.BusinessCardsContract;
import com.example.aroundapplcation.model.AdvertiserBusinessCard;
import com.example.aroundapplcation.presenter.BusinessCardsPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;
import com.google.android.gms.nearby.Nearby;

import java.util.List;

import static com.example.aroundapplcation.constants.IntentConstants.BUSINESS_CARD_ID;

public class BusinessCardsActivity extends AppCompatActivity implements BusinessCardsContract.View {

    private BusinessCardsContract.Presenter presenter;

    private RecyclerView businessCardsRecyclerView;
    private BusinessCardsAdapter businessCardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_cards);

        if (ContextCompat.checkSelfPermission(BusinessCardsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(BusinessCardsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            initPresenter();

            initFields();

            presenter.startAdvertising();

            presenter.startDiscovering();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    BusinessCardsActivity.this.recreate();
                } else {
                    showToast("Permission denied");
                }
            }
        }
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void initAdapter(final List<AdvertiserBusinessCard> items) {
        businessCardsAdapter = new BusinessCardsAdapter(items, presenter, this);

        businessCardsRecyclerView.setAdapter(businessCardsAdapter);
        businessCardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void updateBusinessCards() {
        businessCardsAdapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToBusinessCardScreen(final Integer businessCardId) {
        Intent intent = new Intent(BusinessCardsActivity.this, BusinessCardActivity.class);
        intent.putExtra(BUSINESS_CARD_ID, businessCardId);
        startActivity(intent);
    }

    @Override
    public void navigateToShopScreen() {
        Intent intent = new Intent(BusinessCardsActivity.this, ShopActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToFavoritesScreen() {
        Intent intent = new Intent(BusinessCardsActivity.this, FavoritesActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToProfileScreen() {
        Intent intent = new Intent(BusinessCardsActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void initPresenter() {
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance(sharedPreferences).getApiInterface();
        presenter = new BusinessCardsPresenter(this, Nearby.getConnectionsClient(this), sharedPreferences, api);
    }

    private void initFields() {
        businessCardsRecyclerView = findViewById(R.id.rv_business_cards);
        final Toolbar toolbar = findViewById(R.id.business_cards_toolbar);
        setSupportActionBar(toolbar);

        presenter.initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                presenter.loadFavoritesScreen();
                break;
            case R.id.action_profile:
                presenter.loadProfileScreen();
                break;
            case R.id.action_shop:
                presenter.loadShopScreen();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopAdvertising();
        presenter.stopDiscovery();
    }
}
