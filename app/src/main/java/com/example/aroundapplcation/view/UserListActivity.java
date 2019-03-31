package com.example.aroundapplcation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aroundapplcation.Constants;
import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.User;
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

public class UserListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> userIds = new ArrayList<>();
    private ArrayList<String> endpointIds = new ArrayList<>();

    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                    userIds.add(discoveredEndpointInfo.getEndpointName());
                    endpointIds.add(s);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onEndpointLost(@NonNull String s) {
                    int index = endpointIds.indexOf(s);
                    endpointIds.remove(s);
                    userIds.remove(index);
                    mAdapter.notifyDataSetChanged();
                }
            };

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
            mRecyclerView = findViewById(R.id.rv_users);
            mRecyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new UserListAdapter(userIds, this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(
                    new DividerItemDecoration(
                            mRecyclerView.getContext(), layoutManager.getLayoutDirection()));
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
                }
                else {
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
                        "ME",
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
}
