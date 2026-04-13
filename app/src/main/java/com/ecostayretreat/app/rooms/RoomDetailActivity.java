package com.ecostayretreat.app.rooms;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.models.Room;
import com.ecostayretreat.app.notifications.NotificationHelper;
import com.ecostayretreat.app.session.SessionManager;
import com.ecostayretreat.app.util.DateUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class RoomDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ROOM_ID = "room_id";

    private EcoStayRepository repo;
    private SessionManager session;

    private long roomId;
    private Room room;

    private ImageView image;
    private TextView name;
    private TextView type;
    private TextView description;
    private TextView price;
    private TextInputLayout checkInLayout;
    private TextInputLayout checkOutLayout;
    private TextInputEditText checkInEdit;
    private TextInputEditText checkOutEdit;
    private MaterialButton bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        repo = new EcoStayRepository(this);
        session = new SessionManager(this);

        roomId = getIntent().getLongExtra(EXTRA_ROOM_ID, -1L);
        room = repo.getRoomById(roomId);
        if (room == null) {
            finish();
            return;
        }

        image = findViewById(R.id.roomImage);
        name = findViewById(R.id.roomName);
        type = findViewById(R.id.roomType);
        description = findViewById(R.id.roomDescription);
        price = findViewById(R.id.roomPrice);
        checkInLayout = findViewById(R.id.checkInLayout);
        checkOutLayout = findViewById(R.id.checkOutLayout);
        checkInEdit = findViewById(R.id.checkInEdit);
        checkOutEdit = findViewById(R.id.checkOutEdit);
        bookBtn = findViewById(R.id.bookRoomBtn);

        image.setImageResource(room.imageRes);
        name.setText(room.name);
        type.setText(room.type);
        description.setText(room.description);
        price.setText(String.format(Locale.US, "$%.2f / night", room.pricePerNight));

        checkInEdit.setText(DateUtil.todayIso());
        checkOutEdit.setText(DateUtil.todayIso());

        checkInEdit.setOnClickListener(v -> pickDate(checkInEdit));
        checkOutEdit.setOnClickListener(v -> pickDate(checkOutEdit));

        bookBtn.setOnClickListener(v -> book());
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

    private void book() {
        checkInLayout.setError(null);
        checkOutLayout.setError(null);

        String start = checkInEdit.getText() == null ? "" : checkInEdit.getText().toString().trim();
        String end = checkOutEdit.getText() == null ? "" : checkOutEdit.getText().toString().trim();

        boolean ok = true;
        if (start.isEmpty()) {
            checkInLayout.setError("Select a check-in date");
            ok = false;
        }
        if (end.isEmpty()) {
            checkOutLayout.setError("Select a check-out date");
            ok = false;
        }
        if (!ok) return;

        long userId = session.getUserId();
        long bookingId = repo.createRoomBooking(userId, roomId, start, end);
        if (bookingId <= 0) {
            Snackbar.make(bookBtn, "Could not create booking", Snackbar.LENGTH_LONG).show();
            return;
        }

        NotificationHelper.showBookingConfirmation(
                this,
                "Room booked",
                room.name + " (" + start + " → " + end + ")"
        );
        Snackbar.make(bookBtn, "Room booked successfully", Snackbar.LENGTH_LONG).show();
        finish();
    }
}

