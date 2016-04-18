package com.github.oryanmat.trellowidget.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.util.color.ColorPreference
import com.github.oryanmat.trellowidget.widget.updateWidgets
import com.github.oryanmat.trellowidget.widget.updateWidgetsData

class GeneralPreferenceFragment : PreferenceFragment() {
    val COLOR_FORMAT = "#%08X"
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key -> setPreferenceChanges(key) }

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

    fun setPreferenceChanges(key: String) {
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
                setSummary(if (isChecked)
                    R.string.pref_title_use_unique_color_enabled_desc else
                    R.string.pref_title_use_unique_color_disabled_desc)
                val titleFgPref = findPreference(getString(R.string.pref_title_fore_color_key)) as ColorPreference
                val titleBgPref = findPreference(getString(R.string.pref_title_back_color_key)) as ColorPreference
                titleFgPref.isEnabled = isChecked
                titleBgPref.isEnabled = isChecked
            }
        }
    }
}