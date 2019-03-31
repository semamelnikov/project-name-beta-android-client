package com.example.aroundapplcation.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.PhoneNumber;
import com.example.aroundapplcation.model.PhoneResponse;
import com.example.aroundapplcation.services.NetworkService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumberActivity extends AppCompatActivity {

    static final int CONFIRM_PHONE_REQUEST = 1;
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

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
                .sendPhoneNumber(new PhoneNumber(phoneNumber))
                .enqueue(new Callback<PhoneResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PhoneResponse> call, @NonNull Response<PhoneResponse> response) {
                        PhoneResponse phoneResponse = response.body();
                        if (phoneResponse != null && phoneResponse.getProcess().equals("LOGIN")) {
                            Intent intent = new Intent(PhoneNumberActivity.this, LoginActivity.class);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivityForResult(intent, CONFIRM_PHONE_REQUEST);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PhoneResponse> call, @NonNull Throwable t) {
                        Toast.makeText(PhoneNumberActivity.this, "Network error...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CONFIRM_PHONE_REQUEST) {
            if (resultCode == RESULT_OK) {
                phoneNumber = data != null ? data.getStringExtra("phoneNumber") : "Undefined";
                // TODO make API call
                Intent intent = new Intent(PhoneNumberActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        }
    }


}