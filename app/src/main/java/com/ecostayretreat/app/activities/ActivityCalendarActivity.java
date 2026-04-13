package com.ecostayretreat.app.activities;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.bookings.BookingAdapter;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.models.Booking;
import com.ecostayretreat.app.session.SessionManager;
import com.ecostayretreat.app.util.DateUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityCalendarActivity extends AppCompatActivity implements BookingAdapter.OnBookingActionListener {

    public static final String EXTRA_SELECTED_DATE = "selected_date";

    private EcoStayRepository repo;
    private SessionManager session;

    private CalendarView calendarView;
    private TextView selectedDateLabel;
    private BookingAdapter adapter;
    private String selectedDateIso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_calendar);

        repo = new EcoStayRepository(this);
        session = new SessionManager(this);

        calendarView = findViewById(R.id.calendarView);
        selectedDateLabel = findViewById(R.id.selectedDateLabel);
        RecyclerView recyclerView = findViewById(R.id.activityBookingsRecycler);
        MaterialButton browseBtn = findViewById(R.id.browseActivitiesBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingAdapter();
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        selectedDateIso = DateUtil.todayIso();
        updateSelectedDateLabel();
        loadForSelectedDate();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDateIso = DateUtil.toIso(year, month, dayOfMonth);
            updateSelectedDateLabel();
            loadForSelectedDate();
        });

        browseBtn.setOnClickListener(v -> {
            android.content.Intent i = new android.content.Intent(this, ActivitiesActivity.class);
            i.putExtra(EXTRA_SELECTED_DATE, selectedDateIso);
            startActivity(i);
        });
    }

    private void updateSelectedDateLabel() {
        selectedDateLabel.setText("Your activity bookings on " + selectedDateIso);
    }

    private void loadForSelectedDate() {
        long userId = session.getUserId();
        List<Booking> all = repo.getBookingsForUser(userId);
        List<Booking> filtered = new ArrayList<>();
        for (Booking b : all) {
            if (b.activityId != null && selectedDateIso.equals(b.startDate)) {
                filtered.add(b);
            }
        }
        adapter.submitList(filtered);
    }

    @Override
    public void onCancel(Booking booking) {
        boolean ok = repo.cancelBooking(booking.id);
        if (!ok) {
            com.google.android.material.snackbar.Snackbar.make(
                    findViewById(R.id.activityBookingsRecycler),
                    "Could not cancel booking",
                    com.google.android.material.snackbar.Snackbar.LENGTH_LONG
            ).show();
            return;
        }
        com.google.android.material.snackbar.Snackbar.make(
                findViewById(R.id.activityBookingsRecycler),
                "Booking cancelled",
                com.google.android.material.snackbar.Snackbar.LENGTH_LONG
        ).show();
        loadForSelectedDate();
    }
}

