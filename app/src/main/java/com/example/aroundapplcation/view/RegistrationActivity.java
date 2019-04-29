package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.RegistrationContract;
import com.example.aroundapplcation.presenter.RegistrationPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {

    private RegistrationContract.Presenter presenter;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPassword;
    private EditText etPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initPresenter();

        initFields();

        presenter.initRegistrationRequest();
    }

    public void clickConfirm(View view) {
        presenter.saveRegistrationData(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etPassword.getText().toString(),
                etPasswordConfirm.getText().toString()
        );
        presenter.sendRegistrationRequest();
    }

    @Override
    public void navigateToLoginScreen() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initFields() {
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_lastname);
        etPassword = findViewById(R.id.et_password1);
        etPasswordConfirm = findViewById(R.id.et_password2);
    }

    private void initPresenter() {
        final Intent intent = getIntent();
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        presenter = new RegistrationPresenter(this, intent, api, sharedPreferences);
    }
}
