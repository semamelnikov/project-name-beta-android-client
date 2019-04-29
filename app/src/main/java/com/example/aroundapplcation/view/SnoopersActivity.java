package com.example.aroundapplcation.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.aroundapplcation.R;

public class SnoopersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snoopers);
        Toolbar toolbar = findViewById(R.id.snoopers_toolbar);
        setSupportActionBar(toolbar);
    }

}
