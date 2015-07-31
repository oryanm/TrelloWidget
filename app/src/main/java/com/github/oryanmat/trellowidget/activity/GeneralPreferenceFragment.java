package com.github.oryanmat.trellowidget.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import com.github.oryanmat.trellowidget.R;
import com.github.oryanmat.trellowidget.util.ColorPreference;
import com.github.oryanmat.trellowidget.widget.TrelloWidgetProvider;

public class GeneralPreferenceFragment extends PreferenceFragment {
    static final String COLOR_FORMAT = "#%08X";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_text_size_key));
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_back_color_key));
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_fore_color_key));
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_update_interval_key));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(listener);
        TrelloWidgetProvider.updateWidgets(getActivity());
        TrelloWidgetProvider.updateWidgetsData(getActivity());
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    setPreferenceChanges(key);
                }
            };

    private void setPreferenceChanges(String key) {
        if (key.equals(getString(R.string.pref_update_interval_key))) {
            ListPreference preference = (ListPreference) findPreference(key);
            int index = preference.findIndexOfValue(preference.getValue());
            preference.setSummary(String.format(getActivity()
                    .getString(R.string.pref_update_interval_value_desc), preference.getEntries()[index]));
        } else if (key.equals(getString(R.string.pref_text_size_key))) {
            ListPreference preference = (ListPreference) findPreference(key);
            int index = preference.findIndexOfValue(preference.getValue());
            preference.setSummary(preference.getEntries()[index]);
        } else if (key.equals(getString(R.string.pref_back_color_key))) {
            ColorPreference preference = (ColorPreference) findPreference(key);
            preference.setSummary(String.format(COLOR_FORMAT, preference.getColor()));
        } else if (key.equals(getString(R.string.pref_fore_color_key))) {
            ColorPreference preference = (ColorPreference) findPreference(key);
            preference.setSummary(String.format(COLOR_FORMAT, preference.getColor()));
        }
    }
}

