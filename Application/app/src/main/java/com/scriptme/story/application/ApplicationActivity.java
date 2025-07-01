package com.scriptme.story.application;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.CoordinatorLayout;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scriptme.story.R;
import com.scriptme.story.engine.Engine;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.app.fragments.EbookFragment;
import com.scriptme.story.engine.app.fragments.TerminalFragment;
import com.scriptme.story.engine.app.settings.SettingsFragment;
import com.scriptme.story.engine.graphics.calligraphy.CalligraphyContextWrapper;
import com.scriptme.story.engine.view.ResideMenu;
import com.scriptme.story.engine.view.ResideMenuItem;
import com.scriptme.story.engine.widget.FrameView;

public class ApplicationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "ApplicationScriptMe";
	public static void start(Context c) {
        Intent mIntent = new Intent(c, ApplicationActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(mIntent);
    }
	private Theme theme;
    private Toolbar mToolbar;
	private CoordinatorLayout mCoordinatorLayout;
	private FrameView mFrameView;
	
	/*This APPLICATION SCRIPTME*/
    private ResideMenu resideMenu;
    private ResideMenuItem itemScript;
    private ResideMenuItem itemEbook;
    private ResideMenuItem itemEditor;
    private ResideMenuItem itemRunner;
	
	private LinearLayout mFooterLayout;
	private ImageView mMenuLeft;
	private ImageView mMenuCenter;
	private ImageView mMenuRight;
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            String pkg = getPackageName();
            PowerManager pm = getSystemService(PowerManager.class);

            if (!pm.isIgnoringBatteryOptimizations(pkg)) {
                Intent i = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).setData(Uri.parse("package:" + pkg));
                startActivity(i);
            }
		}
        theme = Theme.with(this);
		theme.setTheme();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application_scriptme);	
		mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mCoordinatorLayout.setBackgroundColor(theme.getBackgroundColor());

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {		
			mToolbar.setPopupTheme(theme.getPopupToolbarStyle());
			mToolbar.setBackgroundColor(theme.getBackgroundColor());
			mToolbar.setTitle(getString(R.string.engine_app_name));
			setSupportActionBar(mToolbar);
		}	
		
		// attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(theme.getBackgroundDrawable());
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

		mFrameView = (FrameView)findViewById(R.id.frame_view);
		mFrameView.setBackgroundColor(theme.getBackgroundColor());
		
        // create menu items;
        itemScript    = new ResideMenuItem(this, android.R.drawable.ic_input_add,     "Script File");
        itemEbook  = new ResideMenuItem(this, android.R.drawable.ic_input_add,  "Ebook");
        itemEditor = new ResideMenuItem(this, android.R.drawable.ic_input_add,    "Editor");
        itemRunner = new ResideMenuItem(this, android.R.drawable.ic_input_add,  "Terminal");

        itemScript.setOnClickListener(this);
        itemEbook.setOnClickListener(this);
        itemEditor.setOnClickListener(this);
        itemRunner.setOnClickListener(this);

        resideMenu.addMenuItem(itemScript, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemEbook, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemEditor, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemRunner, ResideMenu.DIRECTION_RIGHT);

		mFooterLayout = findViewById(R.id.footer_layout);
		mFooterLayout.setBackgroundColor(theme.getBackgroundColor());
	    mMenuLeft = findViewById(R.id.icon_menu_left);
		mMenuLeft.setColorFilter(theme.getIconColor());
	    mMenuCenter = findViewById(R.id.icon_menu_home);
		mMenuCenter.setColorFilter(theme.getIconColor());
	    mMenuRight = findViewById(R.id.icon_menu_right);
		mMenuRight.setColorFilter(theme.getIconColor());

        
		findViewById(R.id.action_menu_left).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
				}
			});
		findViewById(R.id.action_menu_home).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
		findViewById(R.id.action_menu_right).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
				}
			});
		theme.setStatusBarColor(theme.getBackgroundColor());
        theme.setNavBarColor(theme.getBackgroundColor());
        theme.setRecentApp(getString(R.string.engine_app_name));
	}

	@Override
    public void onResume() {
        super.onResume();
		theme.getRecreate();	
	}
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemScript) {
			ApplicationScriptFile.start(ApplicationActivity.this);	
        } else if (view == itemEbook) {
			ApplicationEbook.start(ApplicationActivity.this);
        } else if (view == itemEditor) {
			ApplicationEditor.start(ApplicationActivity.this);
	    } else if (view == itemRunner) {
			ApplicationTerminal.start(ApplicationActivity.this);
        }
		resideMenu.clearIgnoredViewList();
        resideMenu.closeMenu();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_application, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_about:
				ApplicationAbout.start(ApplicationActivity.this);
				break;
           case R.id.action_alarm:
                ApplicationReminder.start(ApplicationActivity.this);
				break;     
            case R.id.action_folder_archiver:
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();   
                Engine.selectFile(ApplicationActivity.this, path);
				break;
			case R.id.action_settings:
				ApplicationPreference.start(ApplicationActivity.this);
				break;
			case R.id.action_exit:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
    
	
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }


	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
