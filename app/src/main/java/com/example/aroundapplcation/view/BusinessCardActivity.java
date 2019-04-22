package com.example.aroundapplcation.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.BusinessCard;
import com.example.aroundapplcation.services.NetworkService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCardActivity extends AppCompatActivity {

    public static final String BUSINESS_CARD_ID = "businessCardId";

    private TextView tvName;
    private TextView tvSurname;
    private TextView tvPhone;

    private Integer currentBusinessCardId;
    private BusinessCard currentBusinessCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        tvName = findViewById(R.id.name);
        tvSurname = findViewById(R.id.surname);
        tvPhone = findViewById(R.id.phone);

        currentBusinessCardId = getIntent().getIntExtra(BUSINESS_CARD_ID, -1);
        String accessToken = getBaseContext().getSharedPreferences(getBaseContext().getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE).getString("accessToken", "unknown");

        NetworkService.getInstance().getApiInterface().getBusinessCard(
                accessToken,
                currentBusinessCardId
        ).enqueue(new Callback<BusinessCard>() {
            @Override
            public void onResponse(@NonNull Call<BusinessCard> call, @NonNull Response<BusinessCard> response) {
                currentBusinessCard = response.body();
                tvName.setText(currentBusinessCard.getName());
                tvSurname.setText(currentBusinessCard.getSurname());
                tvPhone.setText(currentBusinessCard.getPhone());

            }

            @Override
            public void onFailure(@NonNull Call<BusinessCard> call, @NonNull Throwable t) {
                Toast.makeText(getBaseContext(), "Network error...", Toast.LENGTH_LONG).show();
            }
        });

    }
}
