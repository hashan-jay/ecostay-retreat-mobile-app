package com.ecostayretreat.app.bookings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.models.Booking;
import com.ecostayretreat.app.session.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingActionListener {
    private EcoStayRepository repo;
    private SessionManager session;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        repo = new EcoStayRepository(this);
        session = new SessionManager(this);

        RecyclerView rv = findViewById(R.id.bookingsRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingAdapter();
        adapter.setListener(this);
        rv.setAdapter(adapter);

        load();
    }

    private void load() {
        long userId = session.getUserId();
        List<Booking> list = repo.getBookingsForUser(userId);
        adapter.submitList(list);
        findViewById(R.id.emptyState).setVisibility(list.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public void onCancel(Booking booking) {
        boolean ok = repo.cancelBooking(booking.id);
        if (!ok) {
            Snackbar.make(findViewById(R.id.bookingsRoot), "Could not cancel booking", Snackbar.LENGTH_LONG).show();
            return;
        }
        Snackbar.make(findViewById(R.id.bookingsRoot), "Booking cancelled", Snackbar.LENGTH_LONG).show();
        load();
    }
}

