package com.jacky.launcher.views;

import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class TimeUtil {

    private TimeUtil() throws InstantiationException {
        throw new InstantiationException("This class is not created for instantiation");
    }

    public static String getTime() {
        String date = getFormattedDate();
        return date.substring(11, date.length() - 3);

    }

    public static String getDate() {
        String date = getFormattedDate();
        return date.substring(0, 11);
    }

    private static String getFormattedDate() {

        Time time = new Time();
        time.setToNow();
        DateFormat.getDateInstance();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c.getTime());
    }
}