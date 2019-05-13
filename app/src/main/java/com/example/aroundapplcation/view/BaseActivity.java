package com.example.aroundapplcation.view;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.aroundapplcation.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected void initToolbar(final int toolbarId, final boolean isBackButtonEnabled) {
        final Toolbar toolbar = findViewById(toolbarId);
        setSupportActionBar(toolbar);
        if (isBackButtonEnabled) {
            toolbar.setNavigationIcon(R.drawable.ic_back_button);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void navigateToShopScreen() {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    public void navigateToFavoritesScreen() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    public void navigateToProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_full_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                navigateToFavoritesScreen();
                break;
            case R.id.action_profile:
                navigateToProfileScreen();
                break;
            case R.id.action_shop:
                navigateToShopScreen();
                break;
        }
        return true;
    }
}
