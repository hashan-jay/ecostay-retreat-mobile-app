package com.ecostayretreat.app.data;

import android.provider.BaseColumns;

public final class DbContract {
    private DbContract() {}

    public static final class Users implements BaseColumns {
        private Users() {}
        public static final String TABLE = "users";
        public static final String COL_NAME = "name";
        public static final String COL_EMAIL = "email";
        public static final String COL_PASSWORD_HASH = "password_hash";
        public static final String COL_PREFERENCES = "preferences";
        public static final String COL_TRAVEL_START_DATE = "travel_start_date";
        public static final String COL_TRAVEL_END_DATE = "travel_end_date";
        public static final String COL_CREATED_AT = "created_at";
    }

    public static final class Rooms implements BaseColumns {
        private Rooms() {}
        public static final String TABLE = "rooms";
        public static final String COL_NAME = "name";
        public static final String COL_TYPE = "type";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_PRICE_PER_NIGHT = "price_per_night";
        public static final String COL_IMAGE_RES = "image_res";
    }

    public static final class Activities implements BaseColumns {
        private Activities() {}
        public static final String TABLE = "activities";
        public static final String COL_NAME = "name";
        public static final String COL_CATEGORY = "category";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_PRICE = "price";
        public static final String COL_IMAGE_RES = "image_res";
        public static final String COL_AVAILABLE_SLOTS = "available_slots";
    }

    public static final class Bookings implements BaseColumns {
        private Bookings() {}
        public static final String TABLE = "bookings";

        public static final String COL_USER_ID = "user_id";
        public static final String COL_ROOM_ID = "room_id";
        public static final String COL_ACTIVITY_ID = "activity_id";

        // Stored as ISO-8601 dates (yyyy-MM-dd) for easy sorting.
        public static final String COL_START_DATE = "start_date";
        public static final String COL_END_DATE = "end_date";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_STATUS = "status"; // active|cancelled
    }
}

