package com.example.aroundapplcation.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.EntryRequest;
import com.example.aroundapplcation.model.EntryResponse;
import com.example.aroundapplcation.services.NetworkService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
        NetworkService.getInstance()
                .getApiInterface()
                .sendPhoneNumber(new EntryRequest(phoneNumber))
                .enqueue(new Callback<EntryResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EntryResponse> call, @NonNull Response<EntryResponse> response) {
                        EntryResponse entryResponse = response.body();
                        if (entryResponse != null) {
                            if (entryResponse.getProcess().equals("REGISTRATION")) {
                                if (!entryResponse.isProcessAvailable()) {
                                    showAlert(getBaseContext(), "Sorry, this number was banned for some reason...");
                                    return;
                                }
                                if (entryResponse.isPhoneChecked()) {
                                    skipPhoneCheck();
                                    return;
                                }
                                if (!entryResponse.isCodeSent()) {
                                    showAlert(getBaseContext(), "Sorry, SMS can't be sent. Try again later.");
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
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CONFIRM_PHONE_REQUEST) {
            if (resultCode == RESULT_OK) {
                phoneNumber = data != null ? data.getStringExtra("phoneNumber") : "Undefined";
                Intent intent = new Intent(EntryActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        }
    }

    private void skipPhoneCheck() {
//        TODO
//        Intent intent = new Intent(EntryActivity.this, RegistrationActivity.class);
//        intent.putExtra("phoneNumber", phoneNumber);
//        startActivity(intent);
    }

    private void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("Error")
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
    }
}