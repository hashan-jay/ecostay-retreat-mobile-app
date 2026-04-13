package com.ecostayretreat.app.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EcoTipReceiver extends BroadcastReceiver {
    private static final String[] TIPS = new String[]{
            "Eco tip: Reuse towels to save water and energy.",
            "Eco tip: Carry a reusable bottle—refill at our filtered stations.",
            "Eco tip: Choose the guided hike—learn Leave No Trace basics.",
            "Eco tip: Turn off lights when leaving your room (solar helps, but every bit counts).",
            "Eco tip: Join the native plant workshop and support local biodiversity.",
            "Eco event: Sunset bird‑watching tour tonight—limited eco‑guide spots available.",
            "Eco offer: 15% off tomorrow’s sustainability workshop when you book before 6pm.",
            "Eco offer: Mid‑week discount on forest eco‑tours—ask at reception for details.",
            "Eco event: Green cinema night featuring nature documentaries in the lounge.",
            "Eco offer: Stay an extra night and receive a complimentary eco‑tour upgrade."
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        long now = System.currentTimeMillis();
        int idx = (int) Math.abs(now % TIPS.length);
        NotificationHelper.showBookingConfirmation(context, "EcoStay tip of the day", TIPS[idx]);
    }
}

