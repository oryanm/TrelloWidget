package com.github.oryanmat.trellowidget.util;

import android.content.Context;
import android.support.annotation.ColorInt;

import com.github.oryanmat.trellowidget.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PrefUtil {

    public static float getPrefTextScale(Context context) {
        return Float.parseFloat(getDefaultSharedPreferences(context).getString(
                context.getString(R.string.pref_text_size_key),
                context.getString(R.string.pref_text_size_default)));
    }

    public static int getInterval(Context context) {
        return Integer.parseInt(getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_update_interval_key),
                        context.getString(R.string.pref_update_interval_default)));
    }

    public static @ColorInt int getBackgroundColor(Context context) {
        return getColor(context, context.getString(R.string.pref_back_color_key),
                context.getResources().getInteger(R.integer.pref_back_color_default));
    }

    public static @ColorInt int getForegroundColor(Context context) {
        return getColor(context, context.getString(R.string.pref_fore_color_key),
                context.getResources().getInteger(R.integer.pref_fore_color_default));
    }

    public static @ColorInt int getColor(Context context, String key, int defValue) {
        return getDefaultSharedPreferences(context).getInt(key, defValue);
    }
}
