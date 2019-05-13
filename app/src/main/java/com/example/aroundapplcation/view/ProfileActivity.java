package com.example.aroundapplcation.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.ProfileContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.presenter.ProfilePresenter;
import com.example.aroundapplcation.services.ApiInterface;
import com.example.aroundapplcation.services.NetworkService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class ProfileActivity extends BaseActivity implements ProfileContract.View {
    private final static int SELECT_PROFILE_IMAGE_REQUEST_CODE = 1;

    private ProfileContract.Presenter presenter;

    private ImageView iconImageView;
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

        presenter.initBusinessCard();
    }

    private void initFields() {
        initToolbar(R.id.profile_toolbar, true);
        iconImageView = findViewById(R.id.iv_profile_icon);
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
        final ApiInterface api = NetworkService.getInstance(sharedPreferences).getApiInterface();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        presenter = new ProfilePresenter(this, sharedPreferences, api, storageReference);
    }

    public void updateBusinessCard(View view) {
        presenter.updateBusinessCard();
    }

    public void uploadProfileIcon(View view) {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), SELECT_PROFILE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PROFILE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            presenter.saveIconPath(data.getData());
        }
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
    public void updateIconByMemoryImage(final Uri currentIconUri) {
        final Bitmap iconBitmap;
        try {
            if (currentIconUri != null) {
                iconBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), currentIconUri);
                iconImageView.setImageBitmap(iconBitmap);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(final String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateIconByLoadedImage(final String iconUri) {
        if (iconUri != null && !"".equals(iconUri)) {
            Glide.with(this)
                    .load(iconUri)
                    .placeholder(R.drawable.person_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(400, 400)
                    .into(iconImageView);
        } else {
            iconImageView.setImageResource(R.drawable.add_person_photo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_profile_menu, menu);
        return true;
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
