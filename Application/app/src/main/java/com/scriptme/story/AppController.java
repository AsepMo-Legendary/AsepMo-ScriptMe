package com.scriptme.story;

import android.app.Application;
import android.content.Context;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import com.scriptme.story.engine.Engine;
import com.scriptme.story.engine.app.analytics.CrashHandler;

public class AppController extends Application {

    private static AppController isInstance;
    private static Context mContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        isInstance = this;
        mContext = this;
        // force to sow the overflow menu icon
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        CrashHandler.init(this);
        Engine.init(this);
    }
    
    public static synchronized AppController getInstance() {
        return isInstance;
    }

    public static Context getContext() {
        return mContext;
    }
}

