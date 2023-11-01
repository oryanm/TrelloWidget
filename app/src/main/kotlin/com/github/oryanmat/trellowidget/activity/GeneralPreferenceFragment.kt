package com.github.oryanmat.trellowidget.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.annotation.StringRes
import com.github.oryanmat.trellowidget.R
import com.rarepebble.colorpicker.ColorPreference
import com.github.oryanmat.trellowidget.widget.updateWidgets
import com.github.oryanmat.trellowidget.widget.updateWidgetsData
import com.github.oryanmat.trellowidget.util.Constants.T_WIDGET_TAG
import android.util.Log

const val COLOR_FORMAT = "#%08X"

class GeneralPreferenceFragment : PreferenceFragmentCompat() {
    private val listener = SharedPreferences
        .OnSharedPreferenceChangeListener { _, key ->
            try {
                setPreferenceChanges(key)
            } catch (e: NullPointerException) {
                Log.e(T_WIDGET_TAG, "Can't find corresponding preference to key $key\n${e.stackTraceToString()}")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is ColorPreference)
            preference.showDialog(this, 0)
        else
            super.onDisplayPreferenceDialog(preference)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(listener)
        requireActivity().updateWidgets()
        requireActivity().updateWidgetsData()
    }

    private fun setPreferenceChanges(key: String) {
        when (key) {
            getString(R.string.pref_update_interval_key) -> {
                val preference = findPreference<ListPreference>(key)!!
                val index = preference.findIndexOfValue(preference.value)
                preference.summary = String.format(
                    getString(
                        R.string.pref_update_interval_value_desc
                    ), preference.entries[index]
                )
            }

            getString(R.string.pref_text_size_key) -> {
                val preference = findPreference<ListPreference>(key)!!
                val index = preference.findIndexOfValue(preference.value)
                preference.summary = preference.entries[index]
            }

            getString(R.string.pref_back_color_key) -> {
                val preference = findPreference<ColorPreference>(key)!!
                preference.summary = String.format(COLOR_FORMAT, preference.color)
            }

            getString(R.string.pref_fore_color_key) -> {
                val preference = findPreference<ColorPreference>(key)!!
                preference.summary = String.format(COLOR_FORMAT, preference.color)
            }

            getString(R.string.pref_title_back_color_key) -> {
                val preference = findPreference<ColorPreference>(key)!!
                preference.summary = String.format(COLOR_FORMAT, preference.color)
            }

            getString(R.string.pref_title_fore_color_key) -> {
                val preference = findPreference<ColorPreference>(key)!!
                preference.summary = String.format(COLOR_FORMAT, preference.color)
            }

            getString(R.string.pref_title_use_unique_color_key) -> {
                val preference = findPreference<SwitchPreference>(key)!!
                with(preference) {
                    summary = getString(R.string.pref_title_use_unique_color_desc)
                    colorPreference(R.string.pref_title_fore_color_key).isEnabled = isChecked
                    colorPreference(R.string.pref_title_back_color_key).isEnabled = isChecked
                }
            }

            getString(R.string.pref_display_board_name_key) -> {
                val preference = findPreference<SwitchPreference>(key)!!
                preference.summary = getString(R.string.pref_display_board_name_desc)
            }
        }
    }

    private fun colorPreference(@StringRes key: Int) =
        findPreference<ColorPreference>(getString(key))!!
}