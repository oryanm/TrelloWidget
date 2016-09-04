package com.github.oryanmat.trellowidget.util

import android.content.Context
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.support.annotation.ColorInt
import com.github.oryanmat.trellowidget.R

object PrefUtil {
    fun getPrefTextScale(context: Context) =
            java.lang.Float.parseFloat(getDefaultSharedPreferences(context).getString(
                    context.getString(R.string.pref_text_size_key),
                    context.getString(R.string.pref_text_size_default)))

    fun getInterval(context: Context) =
            Integer.parseInt(getDefaultSharedPreferences(context)
                    .getString(context.getString(R.string.pref_update_interval_key),
                            context.getString(R.string.pref_update_interval_default)))

    @ColorInt fun getBackgroundColor(context: Context) =
            getColor(context, context.getString(R.string.pref_back_color_key),
                    context.resources.getInteger(R.integer.pref_back_color_default))

    @ColorInt fun getForegroundColor(context: Context) =
            getColor(context, context.getString(R.string.pref_fore_color_key),
                    context.resources.getInteger(R.integer.pref_fore_color_default))

    @ColorInt fun getColor(context: Context, key: String, defValue: Int) =
            getDefaultSharedPreferences(context).getInt(key, defValue)

    fun isTitleEnabled(context: Context) =
            getDefaultSharedPreferences(context).getBoolean(
                    context.getString(R.string.pref_title_onclick_key),
                    context.resources.getBoolean(R.bool.pref_title_onclick_default))
}