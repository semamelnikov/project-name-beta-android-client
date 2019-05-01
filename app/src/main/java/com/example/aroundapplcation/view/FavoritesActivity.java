package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.adapter.FavoritesAdapter;
import com.example.aroundapplcation.contracts.FavoritesContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.presenter.FavoritesPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

import java.util.List;

import static com.example.aroundapplcation.constants.IntentConstants.BUSINESS_CARD_ID;

public class FavoritesActivity extends AppCompatActivity implements FavoritesContract.View {

    private FavoritesContract.Presenter presenter;

    private RecyclerView favoritesRecyclerView;
    private FavoritesAdapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initPresenter();

        initFields();

        presenter.initAccessToken();
        presenter.initFavorites();
    }

    @Override
    public void initAdapter(final List<BusinessCard> favorites) {
        favoritesAdapter = new FavoritesAdapter(favorites, presenter, this);

        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void updateFavorites() {
        favoritesAdapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToBusinessCardScreen(final Integer businessCardId) {
        Intent intent = new Intent(FavoritesActivity.this, BusinessCardActivity.class);
        intent.putExtra(BUSINESS_CARD_ID, businessCardId);
        startActivity(intent);
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initPresenter() {
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        presenter = new FavoritesPresenter(this, api, sharedPreferences);
    }

    private void initFields() {
        Toolbar toolbar = findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);

        favoritesRecyclerView = findViewById(R.id.rv_favorites);

        presenter.initAdapter();
    }
}
