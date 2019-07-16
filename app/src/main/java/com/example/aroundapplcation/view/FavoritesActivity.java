package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class FavoritesActivity extends BaseActivity implements FavoritesContract.View {

    private FavoritesContract.Presenter presenter;

    private RecyclerView favoritesRecyclerView;
    private FavoritesAdapter favoritesAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        progressBar = findViewById(R.id.favorites_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        initPresenter();

        initFields();

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

        progressBar.setVisibility(View.GONE);
        favoritesRecyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_favorites_menu, menu);
        return true;
    }

    private void initPresenter() {
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance(sharedPreferences).getApiInterface();
        presenter = new FavoritesPresenter(this, api);
    }

    private void initFields() {
        initToolbar(R.id.favorites_toolbar, true);

        favoritesRecyclerView = findViewById(R.id.rv_favorites);
        favoritesRecyclerView.setVisibility(View.GONE);

        presenter.initAdapter();
    }
}
