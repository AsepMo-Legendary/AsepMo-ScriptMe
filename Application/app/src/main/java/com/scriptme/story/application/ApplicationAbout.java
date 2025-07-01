package com.scriptme.story.application;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build;
import android.view.View;

import com.scriptme.story.R;
import com.scriptme.story.engine.Engine;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.graphics.calligraphy.CalligraphyContextWrapper;

public class ApplicationAbout extends AppCompatActivity {
    
    public static final String TAG = "ApplicationAbout";
    public static void start(Context c) {
        Intent mIntent = new Intent(c, ApplicationAbout.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(mIntent);
    }
    private Theme theme;
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = Theme.with(this);
        theme.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_terminal); 
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mCoordinatorLayout.setBackgroundColor(theme.getBackgroundColor());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {     
            mToolbar.setPopupTheme(theme.getPopupToolbarStyle());
            mToolbar.setBackgroundColor(theme.getBackgroundColor());
            mToolbar.setTitle(getString(R.string.engine_app_about));
            Drawable home = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            if (home != null) {
                home.setColorFilter(theme.getIconColor(), PorterDuff.Mode.SRC_ATOP);
            }
            mToolbar.setNavigationIcon(home);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            setSupportActionBar(mToolbar);
        }   
        
      
        theme.setStatusBarColor(theme.getBackgroundColor());
        theme.setNavBarColor(theme.getBackgroundColor());
        theme.setRecentApp(getString(R.string.engine_app_name));
    }
    
     
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
