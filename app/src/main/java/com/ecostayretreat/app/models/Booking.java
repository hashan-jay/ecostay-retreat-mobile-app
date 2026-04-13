package com.ecostayretreat.app.models;

public class Booking {
    public long id;
    public long userId;
    public Long roomId;      // nullable
    public Long activityId;  // nullable
    public String startDate; // yyyy-MM-dd
    public String endDate;   // yyyy-MM-dd (nullable for activities)
    public String status;    // active|cancelled
    public long createdAt;

    // Joined display fields (for bookings list UI)
    public String itemName;
    public String itemTypeLabel; // "Room" or "Activity"
}

