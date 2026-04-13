package com.ecostayretreat.app.rooms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.models.Room;

import java.util.Arrays;
import java.util.List;

public class RoomsActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {
    private EcoStayRepository repo;
    private RoomAdapter adapter;

    private Spinner typeSpinner;
    private Spinner sortSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        repo = new EcoStayRepository(this);

        typeSpinner = findViewById(R.id.typeSpinner);
        sortSpinner = findViewById(R.id.sortSpinner);

        List<String> types = Arrays.asList("All", "Standard", "Cabin", "Suite", "Villa");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        typeSpinner.setAdapter(typeAdapter);

        List<String> sorts = Arrays.asList("Name", "Price: Low to High", "Price: High to Low");
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sorts);
        sortSpinner.setAdapter(sortAdapter);

        RecyclerView rv = findViewById(R.id.roomsRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomAdapter();
        adapter.setListener(this);
        rv.setAdapter(adapter);

        findViewById(R.id.applyFiltersBtn).setOnClickListener(v -> loadRooms());

        loadRooms();
    }

    private void loadRooms() {
        String type = (String) typeSpinner.getSelectedItem();
        String sort = (String) sortSpinner.getSelectedItem();
        List<Room> rooms = repo.getRooms(type, sort);
        adapter.submitList(rooms);
    }

    @Override
    public void onRoomClick(Room room) {
        Intent i = new Intent(this, RoomDetailActivity.class);
        i.putExtra(RoomDetailActivity.EXTRA_ROOM_ID, room.id);
        startActivity(i);
    }
}

