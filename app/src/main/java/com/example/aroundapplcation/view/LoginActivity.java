package com.example.aroundapplcation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
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
import com.example.aroundapplcation.model.TokenResponse;
import com.example.aroundapplcation.model.UserCredentials;
import com.example.aroundapplcation.services.NetworkService;

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

        phoneNumber = getIntent().getStringExtra("phoneNumber");
    }

    public void clickConfirm(View view) {
        NetworkService.getInstance()
                .getApiInterface()
                .sendCredentials(new UserCredentials(phoneNumber, password))
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                        TokenResponse tokenResponse = response.body();
                        if (tokenResponse != null) {
                            String accessToken = tokenResponse.getAccessToken();
                            String refreshToken = tokenResponse.getRefreshToken();
                            SharedPreferences sharedPreferences =
                                    getBaseContext()
                                            .getSharedPreferences(getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE);
                            sharedPreferences.edit()
                                    .putString("accessToken", accessToken)
                                    .putString("refreshToken", refreshToken).apply();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            returnIntent.putExtra("phoneNumber", phoneNumber);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                        Toast.makeText(LoginActivity.this, "Network error...", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
