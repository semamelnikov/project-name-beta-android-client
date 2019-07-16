package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.adapter.SnoopersAdapter;
import com.example.aroundapplcation.contracts.SnoopersContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.presenter.SnoopersPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

import java.util.List;

import static com.example.aroundapplcation.constants.IntentConstants.BUSINESS_CARD_ID;

public class SnoopersActivity extends BaseActivity implements SnoopersContract.View {

    private SnoopersContract.Presenter presenter;

    private RecyclerView snoopersRecyclerView;
    private SnoopersAdapter snoopersAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snoopers);

        progressBar = findViewById(R.id.snoopers_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        initPresenter();

        initFields();

        presenter.initSnoopers();
    }

    @Override
    public void initAdapter(final List<BusinessCard> items) {
        snoopersAdapter = new SnoopersAdapter(items, presenter, this);

        snoopersRecyclerView.setAdapter(snoopersAdapter);
        snoopersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void updateSnoopers() {
        snoopersAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
        snoopersRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToBusinessCardScreen(final Integer businessCardId) {
        Intent intent = new Intent(SnoopersActivity.this, BusinessCardActivity.class);
        intent.putExtra(BUSINESS_CARD_ID, businessCardId);
        startActivity(intent);
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initPresenter() {
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance(sharedPreferences).getApiInterface();
        presenter = new SnoopersPresenter(this, api);
    }

    private void initFields() {
        initToolbar(R.id.snoopers_toolbar, true);

        snoopersRecyclerView = findViewById(R.id.rv_snoopers);
        snoopersRecyclerView.setVisibility(View.GONE);

        presenter.initAdapter();
    }

}
