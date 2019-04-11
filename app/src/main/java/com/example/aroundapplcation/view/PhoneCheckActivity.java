package com.example.aroundapplcation.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.PhoneCheckRequest;
import com.example.aroundapplcation.model.PhoneCheckResponse;
import com.example.aroundapplcation.services.NetworkService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneCheckActivity extends AppCompatActivity {

    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_check);

        EditText editText = findViewById(R.id.et_enter_code);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code = s.toString();
            }
        });
    }

    public void clickConfirm(View view) {
        final String registrationSessionId = getIntent().getStringExtra("registrationSessionId");
        final String phoneNumber = getIntent().getStringExtra("phoneNumber");
        NetworkService
                .getInstance()
                .getApiInterface()
                .sendCode(new PhoneCheckRequest(
                        registrationSessionId,
                        phoneNumber,
                        code
                ))
                .enqueue(new Callback<PhoneCheckResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PhoneCheckResponse> call, @NonNull Response<PhoneCheckResponse> response) {
                        PhoneCheckResponse phoneCheckResponse = response.body();
                        if (phoneCheckResponse != null) {
                            if (phoneCheckResponse.isCodeChecked()) {
                                Intent intent = new Intent(PhoneCheckActivity.this, RegistrationActivity.class);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("registrationSessionId", registrationSessionId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                if (phoneCheckResponse.isCodeInvalid()) {
                                    Toast.makeText(getBaseContext(), "Sorry, the code you entered is incorrect!", Toast.LENGTH_LONG).show();
                                }
                                if (phoneCheckResponse.isExpirationTimeOver()) {
                                    Toast.makeText(getBaseContext(), "Sorry, the code you entered was expired!", Toast.LENGTH_LONG).show();
                                }
                                if (phoneCheckResponse.isAttemptLimitOver()) {
                                    Toast.makeText(getBaseContext(),
                                            "Sorry, you reached the limit of attempts to enter code!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PhoneCheckActivity.this, EntryActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PhoneCheckResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getBaseContext(), "Network error...", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
