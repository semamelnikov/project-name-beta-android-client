package com.example.aroundapplcation.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.RegistrationRequest;
import com.example.aroundapplcation.model.RegistrationResponse;
import com.example.aroundapplcation.services.NetworkService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPassword;
    private EditText etPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_lastname);
        etPassword = findViewById(R.id.et_password1);
        etPasswordConfirm = findViewById(R.id.et_password2);
    }

    public void clickConfirm(View view) {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String password = etPassword.getText().toString();
        String passwordCheck = etPasswordConfirm.getText().toString();

        RegistrationRequest request = new RegistrationRequest(
                getIntent().getStringExtra("registrationSessionId"),
                getIntent().getStringExtra("phoneNumber"),
                firstName,
                lastName,
                password,
                passwordCheck);

        Log.d("Registration request", request.toString());

        NetworkService.getInstance()
                .getApiInterface()
                .register(request)
                .enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                        RegistrationResponse registrationResponse = response.body();
                        if (registrationResponse != null) {
                            if (!registrationResponse.getHashPassword().equals("")) {
                                Toast.makeText(getBaseContext(), "Successful!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getSharedPreferences(getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE)
                                        .edit().putLong("userId", registrationResponse.getId()).apply();
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "403 Forbidden", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getBaseContext(), "Sorry, an error occurred...", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
