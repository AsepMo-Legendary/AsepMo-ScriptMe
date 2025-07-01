package com.scriptme.story.engine.app.settings;

import android.support.v7.preference.Preference;


public class PreferencesCompat {

    private static final String TAG = "PreferencesCompat";

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see PreferencesSupportV7
     */
    public static void bindPreferenceSummaryToValue(Preference preference) {
        PreferencesSupportV7.bindPreferenceSummaryToValue(preference);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see PreferencesNative
     */
    public static void bindPreferenceSummaryToValue(Preference preference, Object value) {
        PreferencesSupportV7.bindPreferenceSummaryToValue(preference, value);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see PreferencesSupportV7
     */
    public static void bindPreferenceSummaryToValue(android.preference.Preference preference) {
        PreferencesNative.bindPreferenceSummaryToValue(preference);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see PreferencesNative
     */
    public static void bindPreferenceSummaryToValue(android.preference.Preference preference, Object value) {
        PreferencesNative.bindPreferenceSummaryToValue(preference, value);
    }

}
