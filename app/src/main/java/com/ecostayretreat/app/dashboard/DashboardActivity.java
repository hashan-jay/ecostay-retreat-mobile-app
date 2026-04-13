package com.ecostayretreat.app.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.auth.LoginActivity;
import com.ecostayretreat.app.activities.ActivitiesActivity;
import com.ecostayretreat.app.activities.ActivityCalendarActivity;
import com.ecostayretreat.app.rooms.RoomsActivity;
import com.ecostayretreat.app.bookings.BookingsActivity;
import com.ecostayretreat.app.info.ResortInfoActivity;
import com.ecostayretreat.app.notifications.NotificationHelper;
import com.ecostayretreat.app.profile.ProfileActivity;
import com.ecostayretreat.app.session.SessionManager;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {
    private SessionManager session;
    private static final int REQ_NOTIFICATIONS = 10_001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        session = new SessionManager(this);
        NotificationHelper.ensureChannel(this);
        NotificationHelper.scheduleDailyEcoTips(this);
        maybeRequestNotificationPermission();

        MaterialButton roomsBtn = findViewById(R.id.roomsBtn);
        MaterialButton activitiesBtn = findViewById(R.id.activitiesBtn);
        MaterialButton bookingsBtn = findViewById(R.id.bookingsBtn);
        MaterialButton activityCalendarBtn = findViewById(R.id.activityCalendarBtn);
        MaterialButton profileBtn = findViewById(R.id.profileBtn);
        MaterialButton infoBtn = findViewById(R.id.infoBtn);
        MaterialButton logoutBtn = findViewById(R.id.logoutBtn);

        // Screen implementations come next; for now these are stubs to be wired to Activities.
        roomsBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, RoomsActivity.class));
        });
        activitiesBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivitiesActivity.class));
        });
        bookingsBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, BookingsActivity.class));
        });
        activityCalendarBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityCalendarActivity.class));
        });
        profileBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
        infoBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ResortInfoActivity.class));
        });

        logoutBtn.setOnClickListener(v -> {
            session.logout();
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }

    private void maybeRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33) return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIFICATIONS);
    }
}

