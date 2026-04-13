package com.ecostayretreat.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecostayretreat.app.models.Booking;
import com.ecostayretreat.app.models.EcoActivity;
import com.ecostayretreat.app.models.Room;
import com.ecostayretreat.app.models.User;
import com.ecostayretreat.app.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

public class EcoStayRepository {
    private final EcoStayDbHelper dbHelper;

    public EcoStayRepository(Context context) {
        this.dbHelper = new EcoStayDbHelper(context.getApplicationContext());
    }

    // --- Users / Auth ---

    public long registerUser(String name, String email, String rawPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DbContract.Users.COL_NAME, name);
        v.put(DbContract.Users.COL_EMAIL, email.toLowerCase().trim());
        v.put(DbContract.Users.COL_PASSWORD_HASH, SecurityUtil.sha256(rawPassword));
        v.put(DbContract.Users.COL_CREATED_AT, System.currentTimeMillis());
        return db.insert(DbContract.Users.TABLE, null, v);
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(
                DbContract.Users.TABLE,
                new String[]{DbContract.Users._ID},
                DbContract.Users.COL_EMAIL + "=?",
                new String[]{email.toLowerCase().trim()},
                null, null, null
        )) {
            return c.moveToFirst();
        }
    }

    public User login(String email, String rawPassword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String hash = SecurityUtil.sha256(rawPassword);

        try (Cursor c = db.query(
                DbContract.Users.TABLE,
                new String[]{
                        DbContract.Users._ID,
                        DbContract.Users.COL_NAME,
                        DbContract.Users.COL_EMAIL,
                        DbContract.Users.COL_PREFERENCES,
                        DbContract.Users.COL_TRAVEL_START_DATE,
                        DbContract.Users.COL_TRAVEL_END_DATE
                },
                DbContract.Users.COL_EMAIL + "=? AND " + DbContract.Users.COL_PASSWORD_HASH + "=?",
                new String[]{email.toLowerCase().trim(), hash},
                null, null, null
        )) {
            if (!c.moveToFirst()) return null;
            User u = new User();
            u.id = c.getLong(0);
            u.name = c.getString(1);
            u.email = c.getString(2);
            u.preferences = c.getString(3);
            u.travelStartDate = c.getString(4);
            u.travelEndDate = c.getString(5);
            return u;
        }
    }

    public User getUserById(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(
                DbContract.Users.TABLE,
                new String[]{
                        DbContract.Users._ID,
                        DbContract.Users.COL_NAME,
                        DbContract.Users.COL_EMAIL,
                        DbContract.Users.COL_PREFERENCES,
                        DbContract.Users.COL_TRAVEL_START_DATE,
                        DbContract.Users.COL_TRAVEL_END_DATE
                },
                DbContract.Users._ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null
        )) {
            if (!c.moveToFirst()) return null;
            User u = new User();
            u.id = c.getLong(0);
            u.name = c.getString(1);
            u.email = c.getString(2);
            u.preferences = c.getString(3);
            u.travelStartDate = c.getString(4);
            u.travelEndDate = c.getString(5);
            return u;
        }
    }

    public boolean updateUserPreferences(long userId, String preferences, String startDate, String endDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DbContract.Users.COL_PREFERENCES, preferences);
        v.put(DbContract.Users.COL_TRAVEL_START_DATE, startDate);
        v.put(DbContract.Users.COL_TRAVEL_END_DATE, endDate);
        int rows = db.update(DbContract.Users.TABLE, v, DbContract.Users._ID + "=?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // --- Rooms ---

    public List<Room> getRooms(String typeFilter, String sort) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = null;
        String[] args = null;
        if (typeFilter != null && !typeFilter.equalsIgnoreCase("All")) {
            selection = DbContract.Rooms.COL_TYPE + "=?";
            args = new String[]{typeFilter};
        }

        String orderBy;
        if ("Price: Low to High".equals(sort)) orderBy = DbContract.Rooms.COL_PRICE_PER_NIGHT + " ASC";
        else if ("Price: High to Low".equals(sort)) orderBy = DbContract.Rooms.COL_PRICE_PER_NIGHT + " DESC";
        else orderBy = DbContract.Rooms.COL_NAME + " ASC";

        List<Room> list = new ArrayList<>();
        try (Cursor c = db.query(
                DbContract.Rooms.TABLE,
                new String[]{
                        DbContract.Rooms._ID,
                        DbContract.Rooms.COL_NAME,
                        DbContract.Rooms.COL_TYPE,
                        DbContract.Rooms.COL_DESCRIPTION,
                        DbContract.Rooms.COL_PRICE_PER_NIGHT,
                        DbContract.Rooms.COL_IMAGE_RES
                },
                selection,
                args,
                null, null,
                orderBy
        )) {
            while (c.moveToNext()) {
                Room r = new Room();
                r.id = c.getLong(0);
                r.name = c.getString(1);
                r.type = c.getString(2);
                r.description = c.getString(3);
                r.pricePerNight = c.getDouble(4);
                r.imageRes = c.getInt(5);
                list.add(r);
            }
        }
        return list;
    }

    public Room getRoomById(long roomId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(
                DbContract.Rooms.TABLE,
                new String[]{
                        DbContract.Rooms._ID,
                        DbContract.Rooms.COL_NAME,
                        DbContract.Rooms.COL_TYPE,
                        DbContract.Rooms.COL_DESCRIPTION,
                        DbContract.Rooms.COL_PRICE_PER_NIGHT,
                        DbContract.Rooms.COL_IMAGE_RES
                },
                DbContract.Rooms._ID + "=?",
                new String[]{String.valueOf(roomId)},
                null, null, null
        )) {
            if (!c.moveToFirst()) return null;
            Room r = new Room();
            r.id = c.getLong(0);
            r.name = c.getString(1);
            r.type = c.getString(2);
            r.description = c.getString(3);
            r.pricePerNight = c.getDouble(4);
            r.imageRes = c.getInt(5);
            return r;
        }
    }

    // --- Activities ---

    public List<EcoActivity> getActivities(String categoryFilter) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = null;
        String[] args = null;
        if (categoryFilter != null && !categoryFilter.equalsIgnoreCase("All")) {
            selection = DbContract.Activities.COL_CATEGORY + "=?";
            args = new String[]{categoryFilter};
        }

        List<EcoActivity> list = new ArrayList<>();
        try (Cursor c = db.query(
                DbContract.Activities.TABLE,
                new String[]{
                        DbContract.Activities._ID,
                        DbContract.Activities.COL_NAME,
                        DbContract.Activities.COL_CATEGORY,
                        DbContract.Activities.COL_DESCRIPTION,
                        DbContract.Activities.COL_PRICE,
                        DbContract.Activities.COL_IMAGE_RES,
                        DbContract.Activities.COL_AVAILABLE_SLOTS
                },
                selection,
                args,
                null, null,
                DbContract.Activities.COL_NAME + " ASC"
        )) {
            while (c.moveToNext()) {
                EcoActivity a = new EcoActivity();
                a.id = c.getLong(0);
                a.name = c.getString(1);
                a.category = c.getString(2);
                a.description = c.getString(3);
                a.price = c.getDouble(4);
                a.imageRes = c.getInt(5);
                a.availableSlots = c.getInt(6);
                list.add(a);
            }
        }
        return list;
    }

    public EcoActivity getActivityById(long activityId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(
                DbContract.Activities.TABLE,
                new String[]{
                        DbContract.Activities._ID,
                        DbContract.Activities.COL_NAME,
                        DbContract.Activities.COL_CATEGORY,
                        DbContract.Activities.COL_DESCRIPTION,
                        DbContract.Activities.COL_PRICE,
                        DbContract.Activities.COL_IMAGE_RES,
                        DbContract.Activities.COL_AVAILABLE_SLOTS
                },
                DbContract.Activities._ID + "=?",
                new String[]{String.valueOf(activityId)},
                null, null, null
        )) {
            if (!c.moveToFirst()) return null;
            EcoActivity a = new EcoActivity();
            a.id = c.getLong(0);
            a.name = c.getString(1);
            a.category = c.getString(2);
            a.description = c.getString(3);
            a.price = c.getDouble(4);
            a.imageRes = c.getInt(5);
            a.availableSlots = c.getInt(6);
            return a;
        }
    }

    // --- Bookings ---

    public long createRoomBooking(long userId, long roomId, String startDate, String endDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(DbContract.Bookings.COL_USER_ID, userId);
        v.put(DbContract.Bookings.COL_ROOM_ID, roomId);
        v.putNull(DbContract.Bookings.COL_ACTIVITY_ID);
        v.put(DbContract.Bookings.COL_START_DATE, startDate);
        v.put(DbContract.Bookings.COL_END_DATE, endDate);
        v.put(DbContract.Bookings.COL_STATUS, "active");
        v.put(DbContract.Bookings.COL_CREATED_AT, System.currentTimeMillis());
        return db.insert(DbContract.Bookings.TABLE, null, v);
    }

    public long createActivityBooking(long userId, long activityId, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int slots = getActivitySlotsForUpdate(db, activityId);
            if (slots <= 0) return -1L;

            ContentValues dec = new ContentValues();
            dec.put(DbContract.Activities.COL_AVAILABLE_SLOTS, slots - 1);
            db.update(DbContract.Activities.TABLE, dec, DbContract.Activities._ID + "=?", new String[]{String.valueOf(activityId)});

            ContentValues v = new ContentValues();
            v.put(DbContract.Bookings.COL_USER_ID, userId);
            v.putNull(DbContract.Bookings.COL_ROOM_ID);
            v.put(DbContract.Bookings.COL_ACTIVITY_ID, activityId);
            v.put(DbContract.Bookings.COL_START_DATE, date);
            v.putNull(DbContract.Bookings.COL_END_DATE);
            v.put(DbContract.Bookings.COL_STATUS, "active");
            v.put(DbContract.Bookings.COL_CREATED_AT, System.currentTimeMillis());
            long id = db.insert(DbContract.Bookings.TABLE, null, v);

            db.setTransactionSuccessful();
            return id;
        } finally {
            db.endTransaction();
        }
    }

    private int getActivitySlotsForUpdate(SQLiteDatabase db, long activityId) {
        try (Cursor c = db.query(
                DbContract.Activities.TABLE,
                new String[]{DbContract.Activities.COL_AVAILABLE_SLOTS},
                DbContract.Activities._ID + "=?",
                new String[]{String.valueOf(activityId)},
                null, null, null
        )) {
            if (!c.moveToFirst()) return 0;
            return c.getInt(0);
        }
    }

    public List<Booking> getBookingsForUser(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
                "SELECT b." + DbContract.Bookings._ID + ", b." + DbContract.Bookings.COL_USER_ID + ", " +
                        "b." + DbContract.Bookings.COL_ROOM_ID + ", b." + DbContract.Bookings.COL_ACTIVITY_ID + ", " +
                        "b." + DbContract.Bookings.COL_START_DATE + ", b." + DbContract.Bookings.COL_END_DATE + ", " +
                        "b." + DbContract.Bookings.COL_STATUS + ", b." + DbContract.Bookings.COL_CREATED_AT + ", " +
                        "r." + DbContract.Rooms.COL_NAME + " AS room_name, " +
                        "a." + DbContract.Activities.COL_NAME + " AS activity_name " +
                        "FROM " + DbContract.Bookings.TABLE + " b " +
                        "LEFT JOIN " + DbContract.Rooms.TABLE + " r ON r." + DbContract.Rooms._ID + " = b." + DbContract.Bookings.COL_ROOM_ID + " " +
                        "LEFT JOIN " + DbContract.Activities.TABLE + " a ON a." + DbContract.Activities._ID + " = b." + DbContract.Bookings.COL_ACTIVITY_ID + " " +
                        "WHERE b." + DbContract.Bookings.COL_USER_ID + "=? " +
                        "ORDER BY b." + DbContract.Bookings.COL_CREATED_AT + " DESC";

        List<Booking> list = new ArrayList<>();
        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(userId)})) {
            while (c.moveToNext()) {
                Booking b = new Booking();
                b.id = c.getLong(0);
                b.userId = c.getLong(1);

                if (c.isNull(2)) b.roomId = null;
                else b.roomId = c.getLong(2);

                if (c.isNull(3)) b.activityId = null;
                else b.activityId = c.getLong(3);

                b.startDate = c.getString(4);
                b.endDate = c.getString(5);
                b.status = c.getString(6);
                b.createdAt = c.getLong(7);

                String roomName = c.getString(8);
                String activityName = c.getString(9);

                if (b.roomId != null) {
                    b.itemName = roomName;
                    b.itemTypeLabel = "Room";
                } else {
                    b.itemName = activityName;
                    b.itemTypeLabel = "Activity";
                }

                list.add(b);
            }
        }
        return list;
    }

    public boolean cancelBooking(long bookingId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Long activityId = getBookingActivityIdForUpdate(db, bookingId);

            ContentValues v = new ContentValues();
            v.put(DbContract.Bookings.COL_STATUS, "cancelled");
            int rows = db.update(
                    DbContract.Bookings.TABLE,
                    v,
                    DbContract.Bookings._ID + "=? AND " + DbContract.Bookings.COL_STATUS + "=?",
                    new String[]{String.valueOf(bookingId), "active"}
            );
            if (rows <= 0) return false;

            if (activityId != null) {
                int slots = getActivitySlotsForUpdate(db, activityId);
                ContentValues inc = new ContentValues();
                inc.put(DbContract.Activities.COL_AVAILABLE_SLOTS, slots + 1);
                db.update(DbContract.Activities.TABLE, inc, DbContract.Activities._ID + "=?", new String[]{String.valueOf(activityId)});
            }

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    private Long getBookingActivityIdForUpdate(SQLiteDatabase db, long bookingId) {
        try (Cursor c = db.query(
                DbContract.Bookings.TABLE,
                new String[]{DbContract.Bookings.COL_ACTIVITY_ID},
                DbContract.Bookings._ID + "=?",
                new String[]{String.valueOf(bookingId)},
                null, null, null
        )) {
            if (!c.moveToFirst()) return null;
            if (c.isNull(0)) return null;
            return c.getLong(0);
        }
    }
}

