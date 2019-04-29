package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.EntryContract;
import com.example.aroundapplcation.presenter.EntryPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

import static com.example.aroundapplcation.constants.IntentConstants.PHONE_NUMBER;
import static com.example.aroundapplcation.constants.IntentConstants.REGISTRATION_SESSION_ID;

public class EntryActivity extends AppCompatActivity implements EntryContract.View {

    private EntryContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        initPresenter();

        initPhoneField();
    }

    public void confirmEntryPhone(View view) {
        presenter.saveExistingPhoneNumberToStorage();
        presenter.sendEntryRequest();
    }

    @Override
    public void navigateToLoginScreen(final String phoneNumber) {
        Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        addIntentFlags(intent);
        startActivity(intent);
    }

    @Override
    public void navigateToRegistrationScreen(final String phoneNumber, final String registrationSessionId) {
        Intent intent = new Intent(EntryActivity.this, RegistrationActivity.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        intent.putExtra(REGISTRATION_SESSION_ID, registrationSessionId);
        addIntentFlags(intent);
        startActivity(intent);
    }

    @Override
    public void navigateToPhoneCheckScreen(final String phoneNumber, final String registrationSessionId) {
        Intent intent = new Intent(EntryActivity.this, PhoneCheckActivity.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        intent.putExtra(REGISTRATION_SESSION_ID, registrationSessionId);
        addIntentFlags(intent);
        startActivity(intent);
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initPresenter() {
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        presenter = new EntryPresenter(this, sharedPreferences, api);
    }

    private void initPhoneField() {
        EditText etPhone = findViewById(R.id.et_enter_phone);
        etPhone.addTextChangedListener(getPhoneEditTextChangeListener());
    }

    private TextWatcher getPhoneEditTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.savePhoneNumber(s.toString());
            }
        };
    }

    private void addIntentFlags(final Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}