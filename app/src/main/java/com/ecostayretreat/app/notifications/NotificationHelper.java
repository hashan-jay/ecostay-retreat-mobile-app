package com.ecostayretreat.app.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ecostayretreat.app.R;

import java.util.concurrent.atomic.AtomicInteger;

public final class NotificationHelper {
    private NotificationHelper() {}

    public static final String CHANNEL_ID = "ecostay_general";
    private static final String CHANNEL_NAME = "EcoStay Updates";
    private static final AtomicInteger COUNTER = new AtomicInteger(1000);

    public static void ensureChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        if (nm == null) return;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Eco tips, booking confirmations, and resort updates");
        nm.createNotificationChannel(channel);
    }

    public static void showBookingConfirmation(Context context, String title, String message) {
        ensureChannel(context);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(COUNTER.incrementAndGet(), b.build());
    }

    public static void scheduleDailyEcoTips(Context context) {
        ensureChannel(context);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(context, EcoTipReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                42,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= 23 ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        long interval = AlarmManager.INTERVAL_DAY;
        long first = System.currentTimeMillis() + 10_000L; // first tip shortly after enabling
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, first, interval, pi);
    }
}

