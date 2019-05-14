package com.example.aroundapplcation.presenter;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aroundapplcation.contracts.ProfileContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.services.ApiInterface;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aroundapplcation.constants.SharedPreferencesConstants.USER_ID;

public class ProfilePresenter implements ProfileContract.Presenter {
    private final ProfileContract.View view;
    private final SharedPreferences sharedPreferences;
    private final ApiInterface api;
    private final StorageReference storageReference;

    private BusinessCard businessCard;

    private Uri currentIconUri;

    public ProfilePresenter(final ProfileContract.View view, final SharedPreferences sharedPreferences,
                            final ApiInterface api, final StorageReference storageReference) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
        this.api = api;
        this.storageReference = storageReference;
    }

    @Override
    public void initBusinessCard() {
        api.getBusinessCardByUserId(getUserId()).enqueue(getBusinessCardByUserIdCallback());
    }

    private Callback<BusinessCard> getBusinessCardByUserIdCallback() {
        return new Callback<BusinessCard>() {
            @Override
            public void onResponse(@NonNull Call<BusinessCard> call, @NonNull Response<BusinessCard> response) {
                businessCard = response.body();
                loadCardIcon();
                view.updateBusinessCardFields(businessCard);
            }

            @Override
            public void onFailure(@NonNull Call<BusinessCard> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }

    private void loadCardIcon() {
        final String iconUri = businessCard.getIconUri();
        view.updateIconByLoadedImage(iconUri);
    }

    @Override
    public void updateBusinessCard() {
        if (currentIconUri != null) {
            saveIcon(currentIconUri);
        } else {
            saveBusinessCard();
        }
    }

    private void saveIcon(final Uri profileIconUri) {
        final StorageReference childReference = storageReference.child(getUserIconName());

        childReference.putFile(profileIconUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return childReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    final Uri iconUri = task.getResult();
                    businessCard.setIconUri(iconUri.toString());
                    saveBusinessCard();
                }
            }
        });
    }

    private void saveBusinessCard() {
        api.updateBusinessCard(businessCard.getId(), businessCard).enqueue(getUpdateBusinessCardCallback());
    }

    private String getUserIconName() {
        return "image_" + businessCard.getPhone() +
                "_" + businessCard.getUserId() +
                "_" + businessCard.getId() +
                "_" + UUID.randomUUID() +
                ".jpg";
    }

    private Callback<BusinessCard> getUpdateBusinessCardCallback() {
        return new Callback<BusinessCard>() {
            @Override
            public void onResponse(@NonNull Call<BusinessCard> call, @NonNull Response<BusinessCard> response) {
                businessCard = response.body();
                view.updateBusinessCardFields(businessCard);
                view.showToast("Successful!");
            }

            @Override
            public void onFailure(@NonNull Call<BusinessCard> call, @NonNull Throwable t) {
                view.showToast("Network error. Please, try later.");
                Log.e("Entry error", t.getMessage());
            }
        };
    }

    @Override
    public void savePhoneNumber(final String phone) {
        businessCard.setPhone(phone);
    }

    @Override
    public void saveName(final String name) {
        businessCard.setName(name);
    }

    @Override
    public void saveSurname(final String surname) {
        businessCard.setSurname(surname);
    }

    @Override
    public void saveVk(final String vkId) {
        businessCard.setVkId(vkId);
    }

    @Override
    public void saveInstagram(final String instagramId) {
        businessCard.setInstagramId(instagramId);
    }

    @Override
    public void saveTwitter(final String twitterId) {
        businessCard.setTwitterId(twitterId);
    }

    @Override
    public void saveFacebook(final String facebookLink) {
        businessCard.setFacebookId(facebookLink);
    }

    @Override
    public void saveIconPath(final Uri iconUri) {
        currentIconUri = iconUri;
        view.updateIconByMemoryImage(currentIconUri);
    }

    private Integer getUserId() {
        final Long userId = sharedPreferences.getLong(USER_ID, -1);
        return userId.intValue();
    }
}
