package com.example.aroundapplcation.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.ProfileContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.presenter.ProfilePresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {

    private ProfileContract.Presenter presenter;

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText phoneEditText;
    private EditText vkEditText;
    private EditText instagramEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initPresenter();

        initFields();

        presenter.initAccessToken();
        presenter.initBusinessCard();
    }

    private void initFields() {
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        nameEditText = findViewById(R.id.et_profile_name);
        nameEditText.addTextChangedListener(getNameEditTextChangeListener());
        surnameEditText = findViewById(R.id.et_profile_surname);
        surnameEditText.addTextChangedListener(getSurnameEditTextChangeListener());
        phoneEditText = findViewById(R.id.et_profile_phone);
        phoneEditText.addTextChangedListener(getPhoneEditTextChangeListener());
        vkEditText = findViewById(R.id.et_profile_vk);
        vkEditText.addTextChangedListener(getVkEditTextChangeListener());
        instagramEditText = findViewById(R.id.et_profile_instagram);
        instagramEditText.addTextChangedListener(getInstagramEditTextChangeListener());
    }

    private void initPresenter() {
        final SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.aroUnd_preference_file_key), MODE_PRIVATE);
        final ApiInterface api = NetworkService.getInstance().getApiInterface();
        presenter = new ProfilePresenter(this, sharedPreferences, api);
    }

    public void updateBusinessCard(View view) {
        presenter.updateBusinessCard();
    }

    @Override
    public void updateBusinessCardFields(final BusinessCard businessCard) {
        nameEditText.setText(businessCard.getName());
        surnameEditText.setText(businessCard.getSurname());
        phoneEditText.setText(businessCard.getPhone());
        vkEditText.setText(businessCard.getVkId());
        instagramEditText.setText(businessCard.getInstagramId());
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
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

    private TextWatcher getNameEditTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.saveName(s.toString());
            }
        };
    }

    private TextWatcher getSurnameEditTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.saveSurname(s.toString());
            }
        };
    }

    private TextWatcher getVkEditTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.saveVk(s.toString());
            }
        };
    }

    private TextWatcher getInstagramEditTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.saveInstagram(s.toString());
            }
        };
    }
}
