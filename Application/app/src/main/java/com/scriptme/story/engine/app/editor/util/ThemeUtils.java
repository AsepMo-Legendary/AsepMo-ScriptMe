package com.scriptme.story.engine.app.editor.util;

import android.support.v4.content.ContextCompat;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.settings.PreferenceHelper;

public class ThemeUtils {

    public static void setTheme(Activity activity){
        boolean light = PreferenceHelper.getLightTheme(activity);
        if (light) {
            activity.setTheme(R.style.ScriptMe_Theme_Light);
        } else {
            activity.setTheme(R.style.ScriptMe_Theme_Dark);
        }
    }

    public static void setPreferenceTheme(Activity activity){
        boolean light = PreferenceHelper.getLightTheme(activity);
        if (light) {
            activity.setTheme(R.style.ScriptMe_Theme_Light);
        } else {
            activity.setTheme(R.style.ScriptMe_Theme_Dark);
        }
    }

    public static void setTextColor(Context context, TextView view) {
        boolean light = PreferenceHelper.getLightTheme(context);
        if (light) {
            view.setTextColor(ContextCompat.getColor(context, R.color.engine_light_color_text));
        } else {
            view.setTextColor(ContextCompat.getColor(context, R.color.engine_dark_color_text));
        }
    }
    
    public static void setIconColor(Context context, ImageView view) {
        boolean light = PreferenceHelper.getLightTheme(context);
        if (light) {
            view.setColorFilter(ContextCompat.getColor(context, R.color.engine_light_color_text));
        } else {
            view.setColorFilter(ContextCompat.getColor(context, R.color.engine_dark_color_text));
        }
    }
    
    public static void setBackground(Context context, View view) {
        boolean light = PreferenceHelper.getLightTheme(context);
        if (light) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.engine_light_color_background));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.engine_dark_color_background));
        }
    }
    
    public static void setWindowsBackground(Activity activity) {
        boolean light = PreferenceHelper.getLightTheme(activity);
        if (light) {
            activity.getWindow().setBackgroundDrawableResource(R.color.engine_light_color_background);
        } else {
            activity.getWindow().setBackgroundDrawableResource(R.color.engine_dark_color_background);
        }
    }
}
