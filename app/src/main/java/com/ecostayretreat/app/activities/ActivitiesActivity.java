package com.ecostayretreat.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.models.EcoActivity;

import java.util.Arrays;
import java.util.List;

public class ActivitiesActivity extends AppCompatActivity implements EcoActivityAdapter.OnActivityClickListener {
    private EcoStayRepository repo;
    private EcoActivityAdapter adapter;
    private Spinner categorySpinner;
    private String selectedDateFromCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        repo = new EcoStayRepository(this);

        categorySpinner = findViewById(R.id.categorySpinner);
        List<String> categories = Arrays.asList("All", "Hike", "Tour", "Workshop");
        categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        RecyclerView rv = findViewById(R.id.activitiesRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EcoActivityAdapter();
        adapter.setListener(this);
        rv.setAdapter(adapter);

        findViewById(R.id.applyCategoryBtn).setOnClickListener(v -> load());

        selectedDateFromCalendar = getIntent().getStringExtra(ActivityCalendarActivity.EXTRA_SELECTED_DATE);

        load();
    }

    private void load() {
        String cat = (String) categorySpinner.getSelectedItem();
        List<EcoActivity> list = repo.getActivities(cat);
        adapter.submitList(list);
    }

    @Override
    public void onActivityClick(EcoActivity activity) {
        Intent i = new Intent(this, ActivityDetailActivity.class);
        i.putExtra(ActivityDetailActivity.EXTRA_ACTIVITY_ID, activity.id);
        if (selectedDateFromCalendar != null && !selectedDateFromCalendar.isEmpty()) {
            i.putExtra(ActivityDetailActivity.EXTRA_DEFAULT_DATE, selectedDateFromCalendar);
        }
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load(); // refresh slots after reservations/cancellations
    }
}

