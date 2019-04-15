package com.example.aroundapplcation.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.EntryRequest;
import com.example.aroundapplcation.model.EntryResponse;
import com.example.aroundapplcation.services.NetworkService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryActivity extends AppCompatActivity {

    static final int CONFIRM_PHONE_REQUEST = 1;
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        EditText editText = findViewById(R.id.et_enter_phone);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = s.toString();
            }
        });
    }

    public void clickConfirm(View view) {
        getSharedPreferences(getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE)
                .edit().putString("phoneNumber", phoneNumber).apply();
        NetworkService.getInstance()
                .getApiInterface()
                .sendPhoneNumber(new EntryRequest(phoneNumber))
                .enqueue(new Callback<EntryResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EntryResponse> call, @NonNull Response<EntryResponse> response) {
                        EntryResponse entryResponse = response.body();
                        if (entryResponse != null) {

                            Log.d("Entry response",
                                    entryResponse.getRegistrationSessionId() + "\n" +
                                            entryResponse.getProcess() + "\n" +
                                            entryResponse.isCodeSent() + "\n" +
                                            entryResponse.isPhoneChecked() + "\n" +
                                            entryResponse.isProcessAvailable());

                            if (entryResponse.getProcess().equals("REGISTRATION")) {
                                if (!entryResponse.isProcessAvailable()) {
                                    Toast.makeText(
                                            getBaseContext(),
                                            "Sorry, this number was banned for some reason...",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    return;
                                }
                                if (entryResponse.isPhoneChecked()) {
                                    skipPhoneCheck(entryResponse.getRegistrationSessionId(), phoneNumber);
                                    return;
                                }
                                if (!entryResponse.isCodeSent()) {
                                    Toast.makeText(
                                            getBaseContext(),
                                            "Sorry, SMS can't be sent. Try again later.",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    return;
                                }
                                Intent intent = new Intent(EntryActivity.this, PhoneCheckActivity.class);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("registrationSessionId", entryResponse.getRegistrationSessionId());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if (entryResponse.getProcess().equals("LOGIN")) {
                                Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<EntryResponse> call, @NonNull Throwable t) {
                        Toast.makeText(EntryActivity.this, "Network error...", Toast.LENGTH_LONG).show();
                        Log.e("Entry error", t.getMessage());
                    }
                });
    }

    private void skipPhoneCheck(String registrationSessionId, String phoneNumber) {
        Intent intent = new Intent(EntryActivity.this, RegistrationActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("registrationSessionId", registrationSessionId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}