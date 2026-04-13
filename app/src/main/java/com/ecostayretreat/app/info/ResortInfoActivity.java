package com.ecostayretreat.app.info;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.notifications.NotificationHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class ResortInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resort_info);

        MaterialButton enableTips = findViewById(R.id.enableTipsBtn);
        enableTips.setOnClickListener(v -> {
            NotificationHelper.scheduleDailyEcoTips(this);
            Snackbar.make(enableTips, "Eco tips enabled (daily)", Snackbar.LENGTH_LONG).show();
        });
    }
}

