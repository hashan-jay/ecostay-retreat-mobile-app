package com.ecostayretreat.app.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.models.EcoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EcoActivityAdapter extends RecyclerView.Adapter<EcoActivityAdapter.VH> {
    public interface OnActivityClickListener {
        void onActivityClick(EcoActivity activity);
    }

    private final List<EcoActivity> items = new ArrayList<>();
    private OnActivityClickListener listener;

    public void setListener(OnActivityClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<EcoActivity> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        EcoActivity a = items.get(position);
        h.name.setText(a.name);
        h.category.setText(a.category);
        h.price.setText(String.format(Locale.US, "$%.2f", a.price));
        h.slots.setText("Slots: " + a.availableSlots);
        h.image.setImageResource(a.imageRes);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onActivityClick(a);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView name;
        final TextView category;
        final TextView price;
        final TextView slots;

        VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.activityImage);
            name = itemView.findViewById(R.id.activityName);
            category = itemView.findViewById(R.id.activityCategory);
            price = itemView.findViewById(R.id.activityPrice);
            slots = itemView.findViewById(R.id.activitySlots);
        }
    }
}

