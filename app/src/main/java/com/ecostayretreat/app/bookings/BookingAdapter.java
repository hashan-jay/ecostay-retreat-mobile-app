package com.ecostayretreat.app.bookings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.models.Booking;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.VH> {
    public interface OnBookingActionListener {
        void onCancel(Booking booking);
    }

    private final List<Booking> items = new ArrayList<>();
    private OnBookingActionListener listener;

    public void setListener(OnBookingActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Booking> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Booking b = items.get(position);
        h.title.setText(b.itemTypeLabel + ": " + b.itemName);
        String dateText;
        if ("Room".equals(b.itemTypeLabel)) {
            dateText = b.startDate + " → " + (b.endDate == null ? "" : b.endDate);
        } else {
            dateText = b.startDate;
        }
        h.date.setText(dateText);
        h.status.setText("Status: " + b.status);

        boolean canCancel = "active".equalsIgnoreCase(b.status);
        h.cancelBtn.setEnabled(canCancel);
        h.cancelBtn.setText(canCancel ? h.cancelBtn.getContext().getString(R.string.cancel) : "Cancelled");
        h.cancelBtn.setOnClickListener(v -> {
            if (!canCancel) return;
            if (listener != null) listener.onCancel(b);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView date;
        final TextView status;
        final MaterialButton cancelBtn;

        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookingTitle);
            date = itemView.findViewById(R.id.bookingDate);
            status = itemView.findViewById(R.id.bookingStatus);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
        }
    }
}

