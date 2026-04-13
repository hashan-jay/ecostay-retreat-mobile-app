package com.ecostayretreat.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ecostayretreat.app.R;

public class EcoStayDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "ecostay.db";
    public static final int DB_VERSION = 1;

    public EcoStayDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + DbContract.Users.TABLE + " (" +
                        DbContract.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DbContract.Users.COL_NAME + " TEXT NOT NULL, " +
                        DbContract.Users.COL_EMAIL + " TEXT NOT NULL UNIQUE, " +
                        DbContract.Users.COL_PASSWORD_HASH + " TEXT NOT NULL, " +
                        DbContract.Users.COL_PREFERENCES + " TEXT, " +
                        DbContract.Users.COL_TRAVEL_START_DATE + " TEXT, " +
                        DbContract.Users.COL_TRAVEL_END_DATE + " TEXT, " +
                        DbContract.Users.COL_CREATED_AT + " INTEGER NOT NULL" +
                        ");"
        );

        db.execSQL(
                "CREATE TABLE " + DbContract.Rooms.TABLE + " (" +
                        DbContract.Rooms._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DbContract.Rooms.COL_NAME + " TEXT NOT NULL, " +
                        DbContract.Rooms.COL_TYPE + " TEXT NOT NULL, " +
                        DbContract.Rooms.COL_DESCRIPTION + " TEXT NOT NULL, " +
                        DbContract.Rooms.COL_PRICE_PER_NIGHT + " REAL NOT NULL, " +
                        DbContract.Rooms.COL_IMAGE_RES + " INTEGER NOT NULL" +
                        ");"
        );

        db.execSQL(
                "CREATE TABLE " + DbContract.Activities.TABLE + " (" +
                        DbContract.Activities._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DbContract.Activities.COL_NAME + " TEXT NOT NULL, " +
                        DbContract.Activities.COL_CATEGORY + " TEXT NOT NULL, " +
                        DbContract.Activities.COL_DESCRIPTION + " TEXT NOT NULL, " +
                        DbContract.Activities.COL_PRICE + " REAL NOT NULL, " +
                        DbContract.Activities.COL_IMAGE_RES + " INTEGER NOT NULL, " +
                        DbContract.Activities.COL_AVAILABLE_SLOTS + " INTEGER NOT NULL" +
                        ");"
        );

        db.execSQL(
                "CREATE TABLE " + DbContract.Bookings.TABLE + " (" +
                        DbContract.Bookings._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DbContract.Bookings.COL_USER_ID + " INTEGER NOT NULL, " +
                        DbContract.Bookings.COL_ROOM_ID + " INTEGER, " +
                        DbContract.Bookings.COL_ACTIVITY_ID + " INTEGER, " +
                        DbContract.Bookings.COL_START_DATE + " TEXT NOT NULL, " +
                        DbContract.Bookings.COL_END_DATE + " TEXT, " +
                        DbContract.Bookings.COL_STATUS + " TEXT NOT NULL, " +
                        DbContract.Bookings.COL_CREATED_AT + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + DbContract.Bookings.COL_USER_ID + ") REFERENCES " + DbContract.Users.TABLE + "(" + DbContract.Users._ID + "), " +
                        "FOREIGN KEY(" + DbContract.Bookings.COL_ROOM_ID + ") REFERENCES " + DbContract.Rooms.TABLE + "(" + DbContract.Rooms._ID + "), " +
                        "FOREIGN KEY(" + DbContract.Bookings.COL_ACTIVITY_ID + ") REFERENCES " + DbContract.Activities.TABLE + "(" + DbContract.Activities._ID + ")" +
                        ");"
        );

        seedRooms(db);
        seedActivities(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.Bookings.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.Activities.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.Rooms.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.Users.TABLE);
        onCreate(db);
    }

    private void seedRooms(SQLiteDatabase db) {
        insertRoom(db,
                "Bamboo Garden Suite",
                "Suite",
                "Spacious suite built with bamboo and reclaimed wood. Solar-powered lighting and natural ventilation.",
                189.0,
                R.drawable.bamboogarden_suite
        );
        insertRoom(db,
                "Rainforest Eco-pod",
                "Standard",
                "Compact and comfortable eco-pod surrounded by lush rainforest. Built with sustainable materials and equipped with solar lighting and natural ventilation.",
                79.0,
                R.drawable.rainforest_ecopod
                );
        insertRoom(db,
                "Earth Lodge Cabin",
                "Cabin",
                "Cozy earth-sheltered cabin with clay finishes and rainwater harvesting. Perfect for quiet weekends.",
                129.0,
                R.drawable.earth_lodge_cabin
        );
        insertRoom(db,
                "Forest Canopy Room",
                "Standard",
                "Tree-line views, organic linens, and low-VOC finishes with a private pool. Designed for comfort with a minimal footprint.",
                99.0,
                R.drawable.forest_canopy_room
        );
        insertRoom(db,
                "Solar Vista Villa",
                "Villa",
                "Private villa powered by on-site solar. Includes composting-friendly amenities and a native-plant patio.",
                249.0,
                R.drawable.solar_vista_villa
        );
        insertRoom(db,
                "Highland Mountain Cabin",
                "Cabin",
                "Cozy cabin perched on a hillside with panoramic mountain views. Crafted with natural timber and stone, featuring solar lighting and eco-friendly amenities.",
                159.0,
                R.drawable.highland_mountain_cabin
                );
    }

    private void insertRoom(SQLiteDatabase db, String name, String type, String description, double price, int imageRes) {
        ContentValues v = new ContentValues();
        v.put(DbContract.Rooms.COL_NAME, name);
        v.put(DbContract.Rooms.COL_TYPE, type);
        v.put(DbContract.Rooms.COL_DESCRIPTION, description);
        v.put(DbContract.Rooms.COL_PRICE_PER_NIGHT, price);
        v.put(DbContract.Rooms.COL_IMAGE_RES, imageRes);
        db.insert(DbContract.Rooms.TABLE, null, v);
    }

    private void seedActivities(SQLiteDatabase db) {
        insertActivity(db,
                "Guided Sunrise Hike",
                "Hike",
                "A low-impact guided hike to a scenic lookout. Learn about local flora and trail ethics.",
                25.0,
                R.drawable.guided_hike,
                12
        );
        insertActivity(db,
                "Bird-Watching Session",
                "Tour",
                "A peaceful guided session to observe native bird species in their natural habitat. Learn basic bird identification and responsible wildlife observation.",
                10.0,
                R.drawable.bird_watching,
                100
        );
        insertActivity(db,
                "Native Plant Workshop",
                "Workshop",
                "Hands-on workshop: native plants, pollinators, and how to build a small balcony habitat.",
                15.0,
                R.drawable.nativeplant_workshop,
                18
        );
        insertActivity(db,
                "Eco-Farm Tour",
                "Tour",
                "Visit our partner regenerative farm. Composting, soil health, and seasonal tasting included.",
                20.0,
                R.drawable.eco_farm_tour,
                20
        );
        insertActivity(db,
                "WildLife Safari Experience",
                "Tour",
                "A guided safari through nearby nature trails to spot local wildlife. Discover the region’s ecosystems while practicing safe and respectful wildlife viewing.",
                30.0,
                R.drawable.wildlife_safari,
                20
                );
    }

    private void insertActivity(SQLiteDatabase db, String name, String category, String description, double price, int imageRes, int slots) {
        ContentValues v = new ContentValues();
        v.put(DbContract.Activities.COL_NAME, name);
        v.put(DbContract.Activities.COL_CATEGORY, category);
        v.put(DbContract.Activities.COL_DESCRIPTION, description);
        v.put(DbContract.Activities.COL_PRICE, price);
        v.put(DbContract.Activities.COL_IMAGE_RES, imageRes);
        v.put(DbContract.Activities.COL_AVAILABLE_SLOTS, slots);
        db.insert(DbContract.Activities.TABLE, null, v);
    }
}

