package com.scriptme.story;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Calendar;

import com.scriptme.story.application.ApplicationActivity;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.app.fragments.TerminalFragment;
import com.scriptme.story.engine.graphics.calligraphy.CalligraphyContextWrapper;

public class StoryActivity extends AppCompatActivity {
    
    public static final String TAG = "StoryActivity";
    private Toolbar mToolbar;
	private CoordinatorLayout mCoordinatorLayout;
	private ImageView mCover;
	private ImageView mFrameView;
    private TextView mMessage;
    private TextView mCopyRight;
	private Theme theme;
	private Handler mHandler = new Handler();
	private Runnable runner = new Runnable(){
		@Override
		public void run() {
			ApplicationActivity.start(StoryActivity.this);
			StoryActivity.this.finish();
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {   
		theme = Theme.with(this);
		theme.setTheme();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
	    mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mCoordinatorLayout.setBackgroundColor(theme.getBackgroundColor());
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {		
			mToolbar.setPopupTheme(theme.getPopupToolbarStyle());
			mToolbar.setBackgroundColor(theme.getBackgroundColor());
			mToolbar.setTitle(getString(R.string.engine_app_name));
			setSupportActionBar(mToolbar);
		}
		mFrameView = (ImageView)findViewById(R.id.iframe_view);
		mFrameView.setColorFilter(theme.getIconColor());
		mCover = (ImageView) findViewById(R.id.cover);
		mCover.setImageResource(R.drawable.engine_scriptme_cover);
		mMessage = (TextView) findViewById(R.id.text_message);
		StringBuilder sb = new StringBuilder();
		sb.append("This Is ScriptMe Application").append("\n");
		sb.append("For Edit And Run Script").append("\n");
		mMessage.setText(sb.toString());
		mMessage.setTextColor(theme.getTextColor());
		
		mCopyRight = (TextView) findViewById(R.id.app_copy_right);
	    String copyrights = String.format(getString(R.string.engine_app_copy_right), Calendar.getInstance().get(Calendar.YEAR));
		mCopyRight.setText(copyrights);
		mCopyRight.setTextColor(theme.getTextColor());
		
		theme.setStatusBarColor(theme.getBackgroundColor());
        theme.setNavBarColor(theme.getBackgroundColor());
        theme.setRecentApp(getString(R.string.engine_app_name));
		mHandler.postDelayed(runner, 3000);
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(runner);
	}
	
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
