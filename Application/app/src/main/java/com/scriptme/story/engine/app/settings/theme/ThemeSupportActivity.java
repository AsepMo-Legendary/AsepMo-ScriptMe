package com.scriptme.story.engine.app.settings.theme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.utils.log.DLog;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public abstract class ThemeSupportActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, IThemeActivity {
    private static final String TAG = "ThemeSupportActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setTheme(getThemeId());
        //setFullScreenMode(isFullScreenMode());
    }

    @Override
    @StyleRes
    public int getThemeId() {
        return Theme.getInstance().getAppTheme();
    }

    /*protected boolean isFullScreenMode() {
        return Preferences.getInstance(this).isFullScreenMode();
    }*/

    private void setFullScreenMode(boolean fullScreenMode) {
        if (fullScreenMode) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
								 WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    @CallSuper
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /*if (key.equals(getString(R.string.pref_key_fullscreen))) {
            setFullScreenMode(isFullScreenMode());
        } else if (key.equals(getString(R.string.pref_app_theme))) {
            if (DLog.DEBUG) DLog.d(TAG, "onSharedPreferenceChanged: change theme");
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

}

