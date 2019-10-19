package com.github.oryanmat.trellowidget.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import androidx.annotation.StringRes
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.util.color.ColorPreference
import com.github.oryanmat.trellowidget.widget.updateWidgets
import com.github.oryanmat.trellowidget.widget.updateWidgetsData

const val COLOR_FORMAT = "#%08X"

class GeneralPreferenceFragment : PreferenceFragment() {
    private val listener = SharedPreferences
            .OnSharedPreferenceChangeListener { _, key -> setPreferenceChanges(key) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_general)

        val preferences = preferenceScreen.sharedPreferences
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_text_size_key))
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_back_color_key))
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_fore_color_key))
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_title_back_color_key))
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_title_fore_color_key))
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_title_use_unique_color_key))
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_update_interval_key))
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_display_board_name_key))
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        activity.updateWidgets()
        activity.updateWidgetsData()
    }

    private fun setPreferenceChanges(key: String) {
        if (key == getString(R.string.pref_update_interval_key)) {
            val preference = findPreference(key) as ListPreference
            val index = preference.findIndexOfValue(preference.value)
            preference.summary = String.format(activity.getString(
                    R.string.pref_update_interval_value_desc), preference.entries[index])
        } else if (key == getString(R.string.pref_text_size_key)) {
            val preference = findPreference(key) as ListPreference
            val index = preference.findIndexOfValue(preference.value)
            preference.summary = preference.entries[index]
        } else if (key == getString(R.string.pref_back_color_key)) {
            val preference = findPreference(key) as ColorPreference
            preference.summary = String.format(COLOR_FORMAT, preference.color)
        } else if (key == getString(R.string.pref_fore_color_key)) {
            val preference = findPreference(key) as ColorPreference
            preference.summary = String.format(COLOR_FORMAT, preference.color)
        } else if (key == getString(R.string.pref_title_back_color_key)) {
            val preference = findPreference(key) as ColorPreference
            preference.summary = String.format(COLOR_FORMAT, preference.color)
        } else if (key == getString(R.string.pref_title_fore_color_key)) {
            val preference = findPreference(key) as ColorPreference
            preference.summary = String.format(COLOR_FORMAT, preference.color)
        } else if (key == getString(R.string.pref_title_use_unique_color_key)) {
            val preference = findPreference(key) as SwitchPreference
            with(preference) {
                summary = getString(R.string.pref_title_use_unique_color_desc)
                colorPreference(R.string.pref_title_fore_color_key).isEnabled = isChecked
                colorPreference(R.string.pref_title_back_color_key).isEnabled = isChecked
            }
        } else if (key == getString(R.string.pref_display_board_name_key)) {
            val preference = findPreference(key) as SwitchPreference
            preference.summary = activity.getString(R.string.pref_display_board_name_desc)
        }
    }

    private fun colorPreference(@StringRes key: Int) = findPreference(getString(key)) as ColorPreference
}