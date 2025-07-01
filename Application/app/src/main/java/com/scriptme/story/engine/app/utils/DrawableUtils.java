package com.scriptme.story.engine.app.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableUtils {
    public static Drawable tintDrawable(Drawable d, @ColorInt int color) {
        Drawable wd = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wd, color);
        return wd;
    }
}
