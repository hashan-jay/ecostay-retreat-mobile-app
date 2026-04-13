package com.ecostayretreat.app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.models.EcoActivity;
import com.ecostayretreat.app.notifications.NotificationHelper;
import com.ecostayretreat.app.session.SessionManager;
import com.ecostayretreat.app.util.DateUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class ActivityDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ACTIVITY_ID = "activity_id";
    public static final String EXTRA_DEFAULT_DATE = "default_date";

    private EcoStayRepository repo;
    private SessionManager session;
    private long activityId;
    private EcoActivity activity;

    private ImageView image;
    private TextView name;
    private TextView category;
    private TextView description;
    private TextView price;
    private TextView slots;
    private TextInputLayout dateLayout;
    private TextInputEditText dateEdit;
    private MaterialButton reserveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);

        repo = new EcoStayRepository(this);
        session = new SessionManager(this);

        activityId = getIntent().getLongExtra(EXTRA_ACTIVITY_ID, -1L);
        activity = repo.getActivityById(activityId);
        if (activity == null) {
            finish();
            return;
        }

        image = findViewById(R.id.activityImage);
        name = findViewById(R.id.activityName);
        category = findViewById(R.id.activityCategory);
        description = findViewById(R.id.activityDescription);
        price = findViewById(R.id.activityPrice);
        slots = findViewById(R.id.activitySlots);
        dateLayout = findViewById(R.id.dateLayout);
        dateEdit = findViewById(R.id.dateEdit);
        reserveBtn = findViewById(R.id.reserveBtn);

        image.setImageResource(activity.imageRes);
        name.setText(activity.name);
        category.setText(activity.category);
        description.setText(activity.description);
        price.setText(String.format(Locale.US, "$%.2f", activity.price));
        slots.setText("Available slots: " + activity.availableSlots);

        String defaultDate = getIntent().getStringExtra(EXTRA_DEFAULT_DATE);
        if (defaultDate == null || defaultDate.isEmpty()) {
            defaultDate = DateUtil.todayIso();
        }
        dateEdit.setText(defaultDate);
        dateEdit.setOnClickListener(v -> pickDate());

        reserveBtn.setOnClickListener(v -> reserve());
    }

    private void pickDate() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog d = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> dateEdit.setText(DateUtil.toIso(year, month, dayOfMonth)),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        d.show();
    }

    private void reserve() {
        dateLayout.setError(null);
        String date = dateEdit.getText() == null ? "" : dateEdit.getText().toString().trim();
        if (date.isEmpty()) {
            dateLayout.setError("Select a date");
            return;
        }

        long userId = session.getUserId();
        long bookingId = repo.createActivityBooking(userId, activityId, date);
        if (bookingId <= 0) {
            Snackbar.make(reserveBtn, "No slots available", Snackbar.LENGTH_LONG).show();
            return;
        }

        NotificationHelper.showBookingConfirmation(
                this,
                "Activity reserved",
                activity.name + " (" + date + ")"
        );
        Snackbar.make(reserveBtn, "Activity reserved successfully", Snackbar.LENGTH_LONG).show();
        finish();
    }
}

