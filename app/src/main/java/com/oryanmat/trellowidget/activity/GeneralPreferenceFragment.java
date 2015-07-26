package com.oryanmat.trellowidget.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import com.oryanmat.trellowidget.R;
import com.oryanmat.trellowidget.util.ColorPreference;
import com.oryanmat.trellowidget.widget.TrelloWidgetProvider;

public class GeneralPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_text_size_key));
        listener.onSharedPreferenceChanged(preferences, getString(R.string.pref_back_color_key));
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
        TrelloWidgetProvider.updateAllWidgets(getActivity());
        TrelloWidgetProvider.updateCardList(getActivity());
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
            preference.setSummary(String.format("Refresh lists every %s", preference.getEntries()[index]));
        } else if (key.equals(getString(R.string.pref_text_size_key))) {
            ListPreference preference = (ListPreference) findPreference(key);
            int index = preference.findIndexOfValue(preference.getValue());
            preference.setSummary(preference.getEntries()[index]);
        } else if (key.equals(getString(R.string.pref_back_color_key))) {
            ColorPreference preference = (ColorPreference) findPreference(key);
            preference.setSummary(String.format("#%08X", preference.getColor()));
        }
    }
}

