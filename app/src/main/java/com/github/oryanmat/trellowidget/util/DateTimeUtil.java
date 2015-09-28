package com.github.oryanmat.trellowidget.util;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {
    static final String DATE_PARSE_ERROR = "Bad date";

    static final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    static final SimpleDateFormat widgetFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
    static final SimpleDateFormat activityFormat = new SimpleDateFormat("MMM dd 'at' HH:mm a", Locale.getDefault());

    static {
        // it appears that trello returns dates in UTC time zone
        apiFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String parseDate(@NonNull String date) {
        return parse(date, widgetFormat);
    }

    public static String parseDateTime(@NonNull String date) {
        return parse(date, activityFormat);
    }

    private static String parse(@NonNull String date, DateFormat format) {
        try {
            return format.format(apiFormat.parse(date));
        } catch (ParseException e) {
            return DATE_PARSE_ERROR;
        }
    }
}
