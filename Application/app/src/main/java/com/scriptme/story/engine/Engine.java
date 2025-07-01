package com.scriptme.story.engine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.preference.PreferenceManager;
import com.scriptme.story.engine.app.folders.explorer.FileExplorerActivity;

public class Engine {
    
    public static final String TAG = "Engine";
    public static final String SUPPORT_DIR_NAME = "support";
	public static final String SUPPORT_APPLETS_DIR_NAME = "applets";

    public static final int RC_OPEN_EXAMPLE = 1237;

    private static Context mContext;
	
    private Engine() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(@NonNull Context context) {
        Engine.mContext = context;     
    }

    public static Context getContext() {
        synchronized (Engine.class) {
            if (Engine.mContext == null)
                throw new NullPointerException("Call Base.initialize(context) within your Application onCreate() method.");

            return Engine.mContext.getApplicationContext();
        }
    }
	
    public static void selectFile(Activity activity, String path) {
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath();  
        FileExplorerActivity.startPickFileActivity(activity, path, path, RC_OPEN_EXAMPLE);
    }
}
