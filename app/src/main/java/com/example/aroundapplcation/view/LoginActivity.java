package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.LoginContract;
import com.example.aroundapplcation.presenter.LoginPresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initPresenter();

        initFields();

        presenter.initLoginRequestByPhone();
    }

    public void clickConfirm(View view) {
        presenter.sendLoginRequest();
    }

    @Override
    public void navigateToBusinessCardsScreen() {
        Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showErrorToast(String errorMessage) {
        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private void initPresenter() {
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        presenter = new LoginPresenter(this, sharedPreferences, api);
    }

    private void initFields() {
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
                presenter.savePassword(s.toString());
            }
        });
    }
}
