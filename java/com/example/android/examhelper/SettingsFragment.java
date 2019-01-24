package com.example.android.examhelper;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        PreferenceManager.setDefaultValues(getContext(), R.xml.settings_visualizer, false);
        addPreferencesFromResource(R.xml.settings_visualizer);
    }
}
