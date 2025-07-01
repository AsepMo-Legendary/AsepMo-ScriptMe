package com.scriptme.story.application;

import android.content.Intent;
import android.content.Context;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.activity.EditorActivity;
import com.scriptme.story.engine.graphics.calligraphy.CalligraphyContextWrapper;

public class ApplicationEditor extends EditorActivity {
    
    public static final String TAG = "ApplicationEditor";
    public static void start(Context c) {
        Intent mIntent = new Intent(c, ApplicationEditor.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(mIntent);
    }
     
    
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
