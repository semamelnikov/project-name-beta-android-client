package com.example.aroundapplcation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickConfirm(View view) {
        Toast.makeText(this, "You pressed confirm", Toast.LENGTH_SHORT).show();
    }
}