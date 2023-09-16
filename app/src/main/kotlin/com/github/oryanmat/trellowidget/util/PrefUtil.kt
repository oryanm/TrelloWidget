package com.github.oryanmat.trellowidget.util

import android.content.Context
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.annotation.ColorInt
import com.github.oryanmat.trellowidget.R

internal fun Context.getPrefTextScale(): Float {
    val def: String = getString(R.string.pref_text_size_default)
    val string: String = sharedPreferences().getString(
        getString(R.string.pref_text_size_key), def)!!
    return java.lang.Float.parseFloat(string)
}

internal fun Context.getInterval() =
        Integer.parseInt(sharedPreferences().getString(
                getString(R.string.pref_update_interval_key),
                getString(R.string.pref_update_interval_default)))

@ColorInt
internal fun Context.getCardBackgroundColor() = getColor(
        getString(R.string.pref_back_color_key),
        resources.getInteger(R.integer.pref_back_color_default))

@ColorInt
internal fun Context.getCardForegroundColor() = getColor(
        getString(R.string.pref_fore_color_key),
        resources.getInteger(R.integer.pref_fore_color_default))

internal fun Context.displayBoardName() = sharedPreferences().getBoolean(
        getString(R.string.pref_display_board_name_key),
        resources.getBoolean(R.bool.pref_display_board_name_default))

internal fun Context.isTitleUniqueColor() = sharedPreferences().getBoolean(
        getString(R.string.pref_title_use_unique_color_key),
        resources.getBoolean(R.bool.pref_title_use_unique_color_default))

@ColorInt
internal fun Context.getTitleBackgroundColor(): Int = when {
    isTitleUniqueColor() -> getColor(
            getString(R.string.pref_title_back_color_key),
            resources.getInteger(R.integer.pref_title_back_color_default))
    else -> getCardBackgroundColor()
}

@ColorInt
internal fun Context.getTitleForegroundColor(): Int = when {
    isTitleUniqueColor() -> getColor(
            getString(R.string.pref_title_fore_color_key),
            resources.getInteger(R.integer.pref_title_fore_color_default))
    else -> getCardForegroundColor()
}

@ColorInt
private fun Context.getColor(key: String, defValue: Int) =
        sharedPreferences().getInt(key, defValue)

internal fun Context.isTitleEnabled() = sharedPreferences().getBoolean(
        getString(R.string.pref_title_onclick_key),
        resources.getBoolean(R.bool.pref_title_onclick_default))

private fun Context.sharedPreferences() = getDefaultSharedPreferences(this)