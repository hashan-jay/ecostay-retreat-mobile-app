package com.ecostayretreat.app.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.bookings.BookingsActivity;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.models.User;
import com.ecostayretreat.app.session.SessionManager;
import com.ecostayretreat.app.util.DateUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    private EcoStayRepository repo;
    private SessionManager session;

    private TextView nameText;
    private TextView emailText;
    private TextInputLayout preferencesLayout;
    private TextInputLayout startLayout;
    private TextInputLayout endLayout;
    private TextInputEditText preferencesEdit;
    private TextInputEditText startEdit;
    private TextInputEditText endEdit;
    private MaterialButton saveBtn;
    private MaterialButton viewBookingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        repo = new EcoStayRepository(this);
        session = new SessionManager(this);

        nameText = findViewById(R.id.profileName);
        emailText = findViewById(R.id.profileEmail);
        preferencesLayout = findViewById(R.id.preferencesLayout);
        startLayout = findViewById(R.id.travelStartLayout);
        endLayout = findViewById(R.id.travelEndLayout);
        preferencesEdit = findViewById(R.id.preferencesEdit);
        startEdit = findViewById(R.id.travelStartEdit);
        endEdit = findViewById(R.id.travelEndEdit);
        saveBtn = findViewById(R.id.saveProfileBtn);
        viewBookingsBtn = findViewById(R.id.viewBookingsBtn);

        startEdit.setOnClickListener(v -> pickDate(startEdit));
        endEdit.setOnClickListener(v -> pickDate(endEdit));

        saveBtn.setOnClickListener(v -> save());
        viewBookingsBtn.setOnClickListener(v -> startActivity(new Intent(this, BookingsActivity.class)));

        load();
    }

    private void load() {
        User u = repo.getUserById(session.getUserId());
        if (u == null) {
            finish();
            return;
        }

        nameText.setText(u.name);
        emailText.setText(u.email);
        preferencesEdit.setText(u.preferences == null ? "" : u.preferences);
        startEdit.setText(u.travelStartDate == null || u.travelStartDate.isEmpty() ? DateUtil.todayIso() : u.travelStartDate);
        endEdit.setText(u.travelEndDate == null || u.travelEndDate.isEmpty() ? DateUtil.todayIso() : u.travelEndDate);
    }

    private void pickDate(TextInputEditText target) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog d = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> target.setText(DateUtil.toIso(year, month, dayOfMonth)),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        d.show();
    }

    private void save() {
        preferencesLayout.setError(null);
        startLayout.setError(null);
        endLayout.setError(null);

        String prefs = preferencesEdit.getText() == null ? "" : preferencesEdit.getText().toString().trim();
        String start = startEdit.getText() == null ? "" : startEdit.getText().toString().trim();
        String end = endEdit.getText() == null ? "" : endEdit.getText().toString().trim();

        if (start.isEmpty()) {
            startLayout.setError("Select a start date");
            return;
        }
        if (end.isEmpty()) {
            endLayout.setError("Select an end date");
            return;
        }

        boolean ok = repo.updateUserPreferences(session.getUserId(), prefs, start, end);
        if (!ok) {
            Snackbar.make(saveBtn, "Could not save profile", Snackbar.LENGTH_LONG).show();
            return;
        }
        Snackbar.make(saveBtn, "Profile saved", Snackbar.LENGTH_LONG).show();
    }
}

