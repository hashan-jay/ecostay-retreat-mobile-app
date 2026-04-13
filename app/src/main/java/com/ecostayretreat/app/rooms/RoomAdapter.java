package com.ecostayretreat.app.rooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.models.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.VH> {

    public interface OnRoomClickListener {
        void onRoomClick(Room room);
    }

    private final List<Room> items = new ArrayList<>();
    private OnRoomClickListener listener;

    public void setListener(OnRoomClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Room> rooms) {
        items.clear();
        if (rooms != null) items.addAll(rooms);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Room r = items.get(position);
        h.name.setText(r.name);
        h.type.setText(r.type);
        h.price.setText(String.format(Locale.US, "$%.2f / night", r.pricePerNight));
        h.image.setImageResource(r.imageRes);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRoomClick(r);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView name;
        final TextView type;
        final TextView price;

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.roomImage);
            name = itemView.findViewById(R.id.roomName);
            type = itemView.findViewById(R.id.roomType);
            price = itemView.findViewById(R.id.roomPrice);
        }
    }
}

