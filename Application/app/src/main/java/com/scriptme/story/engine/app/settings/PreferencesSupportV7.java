package com.scriptme.story.engine.app.settings;

import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by Duy on 29-Dec-17.
 */

public class PreferencesSupportV7 {

    private static final String TAG = "PreferencesCompat";
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListenerV7 =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    String stringValue = value.toString();

                    if (preference instanceof ListPreference) {
                        // For list preferences, look up the correct display value in
                        // the preference's 'entries' list.
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        // Set the summary to reflect the new value.
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                    } else if (preference instanceof EditTextPreference) {
                        EditTextPreference editTextPreference = (EditTextPreference) preference;
                        editTextPreference.setSummary(editTextPreference.getText());
                    } else {
                        // For all other preferences, set the summary to the value's
                        // simple string representation.
                        preference.setSummary(stringValue);
                    }
                    return true;
                }
            };


    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListenerV7
     */
    public static void bindPreferenceSummaryToValue(Preference preference) {
        if (preference == null) return;
        if (preference.getOnPreferenceChangeListener() == null) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListenerV7);
        }
        try {
            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListenerV7.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListenerV7
     */
    public static void bindPreferenceSummaryToValue(Preference preference, Object value) {
        if (preference == null) return;
        try {
            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListenerV7.onPreferenceChange(preference, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
