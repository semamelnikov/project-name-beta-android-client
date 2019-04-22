package com.example.aroundapplcation.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.LoginRequest;
import com.example.aroundapplcation.model.LoginResponse;
import com.example.aroundapplcation.services.NetworkService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private int attemptCounter = 0;
    private String phoneNumber = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editText = findViewById(R.id.et_enter_password);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }
        });

        phoneNumber = getSharedPreferences(getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE)
                .getString("phoneNumber", " ");
    }

    public void clickConfirm(View view) {
        NetworkService.getInstance()
                .getApiInterface()
                .sendCredentials(new LoginRequest(phoneNumber, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            String accessToken = loginResponse.getAccessToken();
                            String refreshToken = loginResponse.getRefreshToken();
                            long userId = loginResponse.getUserId();
                            SharedPreferences sharedPreferences =
                                    getBaseContext()
                                            .getSharedPreferences(getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE);
                            sharedPreferences.edit()
                                    .putString("accessToken", accessToken)
                                    .putString("refreshToken", refreshToken)
                                    .putLong("userId", userId).apply();
                            Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        Toast.makeText(LoginActivity.this, "Network error...", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
