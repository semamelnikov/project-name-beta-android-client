package com.example.aroundapplcation.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aroundapplcation.Constants;
import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.User;
import com.example.aroundapplcation.services.NetworkService;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
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
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;

    private List<String> devices;

    private ListView listView;
    private ArrayAdapter<String> adapter;

    private ArrayList<Integer> businessCardIds = new ArrayList<>();
    private ArrayList<String> endpointIds = new ArrayList<>();

    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NonNull final String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
//                    businessCardIds.add(discoveredEndpointInfo.getEndpointName());
                    final int userId = Integer.parseInt(discoveredEndpointInfo.getEndpointName());
                    String accessToken = getBaseContext().getSharedPreferences(getBaseContext().getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE).getString("accessToken", "unknown");
                    NetworkService.getInstance()
                            .getApiInterface()
                            .getUser(accessToken, userId)
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                    businessCardIds.add(response.body().getId());
                                    devices.add(response.body().getName() + "\n" + response.body().getSurname() + "\n" + response.body().getPhone());
                                    endpointIds.add(s);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                    Toast.makeText(getBaseContext(), "Network error...", Toast.LENGTH_LONG).show();
                                }
                            });
//                    endpointIds.add(s);
//                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onEndpointLost(@NonNull String s) {
                    int index = endpointIds.indexOf(s);
                    endpointIds.remove(s);
                    businessCardIds.remove(index);
                    adapter.notifyDataSetChanged();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if (ContextCompat.checkSelfPermission(UserListActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            if (ActivityCompat.shouldShowRequestPermissionRationale(UserListActivity.this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                // TODO Permission request explanation
//            } else {
            ActivityCompat.requestPermissions(UserListActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//            }
        } else {
            listView = findViewById(R.id.rv_users);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Integer businessCardId = businessCardIds.get(position);
                    Intent intent = new Intent(UserListActivity.this, BusinessCardActivity.class);
                    intent.putExtra(BusinessCardActivity.BUSINESS_CARD_ID, businessCardId);
                    startActivity(intent);
                }
            });
            devices = new ArrayList<>();

            adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, devices);
            listView.setAdapter(adapter);

            /*mRecyclerView = findViewById(R.id.rv_users);
            mRecyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new UserListAdapter(businessCardIds, this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(
                    new DividerItemDecoration(
                            mRecyclerView.getContext(), layoutManager.getLayoutDirection()));*/
            startAdvertising();
            startDiscovering();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UserListActivity.this.recreate();
                } else {
                    Toast.makeText(UserListActivity.this, "Permission denied...", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void startAdvertising() {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        Nearby.getConnectionsClient(this)
                .startAdvertising(
                        getUserId(),
                        "com.example.aroundapplication",
                        connectionLifecycleCallback,
                        advertisingOptions);
    }

    private void startDiscovering() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        Nearby.getConnectionsClient(this)
                .startDiscovery(
                        "com.example.aroundapplication",
                        endpointDiscoveryCallback,
                        discoveryOptions);
    }

    private String getUserId() {
        final long userId = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE
        ).getLong("userId", -1);
        return String.valueOf(userId);
    }
}
