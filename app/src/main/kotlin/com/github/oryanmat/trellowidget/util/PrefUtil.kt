package com.github.oryanmat.trellowidget.util

import android.content.Context
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.support.annotation.ColorInt
import com.github.oryanmat.trellowidget.R

internal fun Context.getPrefTextScale() =
        java.lang.Float.parseFloat(sharedPreferences().getString(
                getString(R.string.pref_text_size_key),
                getString(R.string.pref_text_size_default)))

internal fun Context.getInterval() =
        Integer.parseInt(sharedPreferences().getString(
                getString(R.string.pref_update_interval_key),
                getString(R.string.pref_update_interval_default)))

internal @ColorInt fun Context.getBackgroundColor() = getColor(
        getString(R.string.pref_back_color_key),
        resources.getInteger(R.integer.pref_back_color_default))

internal @ColorInt fun Context.getForegroundColor() = getColor(
        getString(R.string.pref_fore_color_key),
        resources.getInteger(R.integer.pref_fore_color_default))

private @ColorInt fun Context.getColor(key: String, defValue: Int) =
        sharedPreferences().getInt(key, defValue)

internal fun Context.isTitleEnabled() = sharedPreferences().getBoolean(
        getString(R.string.pref_title_onclick_key),
        resources.getBoolean(R.bool.pref_title_onclick_default))

private fun Context.sharedPreferences() = getDefaultSharedPreferences(this)