package com.example.aroundapplcation.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.constants.Constants;
import com.example.aroundapplcation.contracts.BusinessCardsContract;
import com.example.aroundapplcation.presenter.BusinessCardsPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;
import com.google.android.gms.nearby.Nearby;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.example.aroundapplcation.constants.IntentConstants.BUSINESS_CARD_ID;

public class BusinessCardsActivity extends AppCompatActivity implements BusinessCardsContract.View {

    private BusinessCardsContract.Presenter presenter;

    // TODO This adapter is temporary.
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

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
                    Toast.makeText(BusinessCardsActivity.this, "Permission denied...", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void initAdapter(final List items) {
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    @Override
    public void updateBusinessCards() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToBusinessCardScreen(final Integer businessCardId) {
        Intent intent = new Intent(BusinessCardsActivity.this, BusinessCardActivity.class);
        intent.putExtra(BUSINESS_CARD_ID, businessCardId);
        startActivity(intent);
    }

    private void initPresenter() {
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE);
        presenter = new BusinessCardsPresenter(this, Nearby.getConnectionsClient(this), sharedPreferences, api);
    }

    private void initFields() {
        listView = findViewById(R.id.lv_users);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.loadBusinessCardScreen(position);
            }
        });

        presenter.initAdapter();
    }
}
