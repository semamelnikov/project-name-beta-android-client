package com.example.aroundapplcation.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.PhoneCheckContract;
import com.example.aroundapplcation.presenter.PhoneCheckPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.aroundapplcation.constants.IntentConstants.PHONE_NUMBER;
import static com.example.aroundapplcation.constants.IntentConstants.REGISTRATION_SESSION_ID;

public class PhoneCheckActivity extends AppCompatActivity implements PhoneCheckContract.View {

    private PhoneCheckContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_check);

        initPresenter();

        initFields();

        presenter.initPhoneCheck();
    }

    public void clickConfirm(View view) {
        presenter.sendPhoneCheckRequest();
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToEntryScreen() {
        Intent intent = new Intent(PhoneCheckActivity.this, EntryActivity.class);
        addIntentFlags(intent);
        startActivity(intent);
    }

    @Override
    public void navigateToRegistrationScreen(final String phone, final String registrationSessionId) {
        Intent intent = new Intent(PhoneCheckActivity.this, RegistrationActivity.class);
        intent.putExtra(PHONE_NUMBER, phone);
        intent.putExtra(REGISTRATION_SESSION_ID, registrationSessionId);
        addIntentFlags(intent);
        startActivity(intent);
    }

    private void initPresenter() {
        final Intent intent = getIntent();
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        presenter = new PhoneCheckPresenter(this, intent, api);
    }

    private void initFields() {
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
                presenter.saveCode(s.toString());
            }
        });
    }

    private void addIntentFlags(final Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
