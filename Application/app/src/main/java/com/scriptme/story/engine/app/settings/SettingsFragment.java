package com.scriptme.story.engine.app.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scriptme.story.R;
import com.scriptme.story.application.ApplicationActivity;
import com.scriptme.story.engine.app.settings.theme.StylePreference;

public class SettingsFragment extends PreferenceFragmentCompat {

    private AppCompatActivity mActivity;
    private Context mContext;
	private StylePreference mStylePreference;
	@Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        // Set an empty screen so getPreferenceScreen doesn't return null -
        // so we can create fake headers from the get-go.
        setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext()));

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_application);

	}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mActivity = (AppCompatActivity)getActivity();
        mStylePreference = (StylePreference)getPreferenceManager().findPreference("theme");
        mStylePreference.onAttact(mActivity);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
