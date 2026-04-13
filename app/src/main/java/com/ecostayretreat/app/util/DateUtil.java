package com.ecostayretreat.app.util;

import java.util.Calendar;
import java.util.Locale;

public final class DateUtil {
    private DateUtil() {}

    public static String todayIso() {
        Calendar c = Calendar.getInstance();
        return toIso(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public static String toIso(int year, int monthZeroBased, int dayOfMonth) {
        int month = monthZeroBased + 1;
        return String.format(Locale.US, "%04d-%02d-%02d", year, month, dayOfMonth);
    }
}

