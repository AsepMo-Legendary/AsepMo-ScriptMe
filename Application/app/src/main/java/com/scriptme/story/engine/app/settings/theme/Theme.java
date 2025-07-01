package com.scriptme.story.engine.app.settings.theme;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.scriptme.story.R;
import com.scriptme.story.engine.Engine;
import com.scriptme.story.engine.app.settings.colors.ColorPalette;

public class Theme {
	public static final int DARK_THEME = 2;
	public static final int LIGHT_THEME = 1;
	public static final int AMOLED_THEME = 3;

	private AppCompatActivity activity;
	private Context context;
	private ThemePreference SP;

	private int baseTheme;
	private int primaryColor;
	private int accentColor;

	public Theme(AppCompatActivity activity) {
		this.activity = activity;
		this.context = Engine.getContext();
		this.SP = ThemePreference.getInstance();

		this.primaryColor = SP.getInt(context.getString(R.string.engine_settings_pref_primary_color), getColor(R.color.engine_color_blue_grey_500));
		this.accentColor = SP.getInt(context.getString(R.string.engine_settings_pref_accent_color), getColor(R.color.engine_color_blue_grey_100));
		baseTheme = SP.getInt(context.getString(R.string.engine_settings_pref_base_theme), LIGHT_THEME);
	}

	public static Theme with(AppCompatActivity activity) {
        return new Theme(activity);
    }

	public Theme() {
		this.context = Engine.getContext();
		this.SP = ThemePreference.getInstance();
		
		updateTheme();
	}

	public static Theme getInstance() {
		return new Theme();
	}

	public static Theme getInstanceLoaded() {
		Theme instance = getInstance();
		instance.updateTheme();
		return instance;
	}

	public void setTheme() {
        int theme;
		switch (baseTheme) {
			case DARK_THEME:
				theme = R.style.ScriptMe_Theme_Dark;
				break;
			case AMOLED_THEME:
				theme = R.style.ScriptMe_Theme_Black;
				break;
			case LIGHT_THEME:
			default:
				theme = R.style.ScriptMe_Theme_Light;
		}
        activity.setTheme(theme);
    }

	public void setRecreate(boolean recreate) {
		SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor e = mSharedPreference.edit();
		e.putBoolean("recreate", recreate);
		e.apply();
	}

    public boolean isRecreate() {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean keepSceenOn = mSharedPreference.getBoolean("recreate", true);    
        return keepSceenOn;
    }
    
    public boolean isKeepScreenOn() {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean keepSceenOn = mSharedPreference.getBoolean("pref_keep_screen_on", true);	
        return keepSceenOn;
    }
    
    public boolean isFullScreenMode() {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(activity);
        return mSharedPreference.getBoolean("fullscreen_mode", true);	
    }

    public void setFullScreenMode(boolean fullscreen) {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor e = mSharedPreference.edit();
        e.putBoolean("fullscreen_mode", fullscreen);
		e.apply();
	}
    
    public void getRecreate() {
		final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(activity);
		activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					boolean recreate = mSharedPreference.getBoolean("recreate", true);
					if (recreate) {
						setRecreate(false);
						new CountDownTimer(200, 200){
							@Override
							public void onTick(long l) {

							}

							@Override
							public void onFinish() {
								activity.recreate();
							}  
						}.start();
					}
				}
			});	
    }

    public String getLastOpenPath() {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        String lastOpenPath = mSharedPreference.getString("last_open_path", Environment.getExternalStorageDirectory().getPath());    
        return lastOpenPath;
    }

    public void setLastOpenPath(String path) {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = mSharedPreference.edit();
        e.putString("last_open_path", path);
        e.apply();
    }
    
    public boolean isShowHiddenFiles() {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        return mSharedPreference.getBoolean("show_hidden_files", true);    
    }

    public void setShowHiddenFiles(boolean showHiddenFile) {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = mSharedPreference.edit();
        e.putBoolean("show_hidden_files", showHiddenFile);
        e.apply();
    }

    public int getFileSortType() {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        return mSharedPreference.getInt("show_file_sort", 0);  
    }

    public void setFileSortType(int type) {
        SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = mSharedPreference.edit();
        e.putInt("show_file_sort", type);
        e.apply();
	}
    
	public void updateTheme() {
		this.primaryColor = SP.getInt(context.getString(R.string.engine_settings_pref_primary_color), getColor(R.color.engine_color_blue_grey_500));
		this.accentColor = SP.getInt(context.getString(R.string.engine_settings_pref_accent_color), getColor(R.color.engine_color_blue_grey_100));
		baseTheme = SP.getInt(context.getString(R.string.engine_settings_pref_base_theme), LIGHT_THEME);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void setNavBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (isNavigationBarColored()) activity.getWindow().setNavigationBarColor(color);
			else
				activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.engine_color_black));
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (isTranslucentStatusBar())
				activity.getWindow().setStatusBarColor(ColorPalette.getObscuredColor(color));
			else
				activity.getWindow().setStatusBarColor(color);
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void setNavBarColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (isNavigationBarColored()) activity.getWindow().setNavigationBarColor(getPrimaryColor());
			else
				activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.engine_color_black));
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	protected void setStatusBarColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (isTranslucentStatusBar())
				activity.getWindow().setStatusBarColor(ColorPalette.getObscuredColor(getPrimaryColor()));
			else
				activity.getWindow().setStatusBarColor(getPrimaryColor());
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void setRecentApp(String text) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			BitmapDrawable drawable = ((BitmapDrawable) activity.getDrawable(R.drawable.engine_asepmo));
			activity.setTaskDescription(new ActivityManager.TaskDescription(text, drawable.getBitmap(), getPrimaryColor()));
		}
	}

	public boolean isNavigationBarColored() {
		return SP.getBoolean(activity.getString(R.string.engine_settings_pref_colored_nav_bar), false);
	}

	public boolean isTranslucentStatusBar() {
		return SP.getBoolean(activity.getString(R.string.engine_settings_pref_translucent_status_bar), true);
	}

	public int getPrimaryColor() {
		return primaryColor;
	}

	public int getAccentColor() {
		return accentColor;
	}

	public int getBaseTheme() { 
		return baseTheme; 
	}

	public void setBaseTheme(int baseTheme, boolean permanent) {
		if (permanent) {
			// TODO: 09/08/16 to
		} else this.baseTheme = baseTheme;
	}


	public static int getPrimaryColor(Context context) {
		ThemePreference SP = ThemePreference.getInstance();
		return SP.getInt(context.getString(R.string.engine_settings_pref_primary_color), ContextCompat.getColor(context, R.color.engine_color_blue_grey_500));
	}

	public boolean isPrimaryEqualAccent() {
		return (this.primaryColor == this.accentColor);
	}

	public static int getAccentColor(Context context) {
		ThemePreference SP = ThemePreference.getInstance();
		return SP.getInt(context.getString(R.string.engine_settings_pref_accent_color), getColor(context, R.color.engine_color_blue_grey_100));
	}

	public static void setApplicationTheme(int theme) {
		Context context = Engine.getContext();
		ThemePreference SP = ThemePreference.getInstance();
		SP.putInt(context.getString(R.string.engine_settings_pref_base_theme), theme);
	}

	public static int getApplicationTheme() {
		Context context = Engine.getContext();
		ThemePreference SP = ThemePreference.getInstance();
		return SP.getInt(context.getString(R.string.engine_settings_pref_base_theme), LIGHT_THEME);
	}


	public int getColor(@ColorRes int color) {
		return ContextCompat.getColor(context, color);
	}

	private static int getColor(Context context, @ColorRes int color) {
		return ContextCompat.getColor(context, color);
	}

	public void themeSeekBar(SeekBar bar) {
		bar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(getAccentColor(), PorterDuff.Mode.SRC_IN));
		bar.getThumb().setColorFilter(new PorterDuffColorFilter(getAccentColor(), PorterDuff.Mode.SRC_IN));
	}

	public int getAppTheme() {
		int theme;
		switch (baseTheme) {
			case DARK_THEME:
				theme = R.style.ScriptMe_Theme_Dark;
				break;
			case AMOLED_THEME:
				theme = R.style.ScriptMe_Theme_Black;
				break;
			case LIGHT_THEME:
			default:
				theme = R.style.ScriptMe_Theme_Light;
		}
		return theme;
	}

	public int getBackgroundColor() {
		int color;
		switch (baseTheme) {
			case DARK_THEME:
				color = getColor(R.color.engine_dark_color_background);
				break;
			case AMOLED_THEME:
				color = getColor(R.color.engine_black_color_background);
				break;
			case LIGHT_THEME:
			default:
				color = getColor(R.color.engine_light_color_background);
		}
		return color;
	}
    
    public int getBackgroundRow() {
        int color;
        switch (baseTheme) {
            case DARK_THEME:
                color = getColor(R.color.engine_dark_color_background_row);
                break;
            case AMOLED_THEME:
                color = getColor(R.color.engine_black_color_background_row);
                break;
            case LIGHT_THEME:
            default:
                color = getColor(R.color.engine_light_color_background_row);
        }
        return color;
    }
    

	public Drawable getBackgroundDrawable() {
		Drawable drawable = null;
		switch (baseTheme) {
			case DARK_THEME:
				drawable =  ContextCompat.getDrawable(context, R.drawable.engine_color_drawable_dark);
				break;
			case AMOLED_THEME:
				drawable =  ContextCompat.getDrawable(context, R.drawable.engine_color_drawable_black);
				break;
			case LIGHT_THEME:
			default:
				drawable =  ContextCompat.getDrawable(context, R.drawable.engine_color_drawable_white);
		}
		return drawable;
	}

	public int getInvertedBackgroundColor() {
		int color;
		switch (baseTheme) {
			case DARK_THEME:color = getColor(R.color.engine_dark_color_background);
				break;
			case AMOLED_THEME:color = getColor(R.color.engine_light_color_background);
				break;
			case LIGHT_THEME:
			default:color = getColor(R.color.engine_color_black);
		}
		return color;
	}

	public int getTextColor() {
		int color;
		switch (baseTheme) {
			case DARK_THEME:color = getColor(R.color.engine_color_grey_200);
				break;
			case AMOLED_THEME:color = getColor(R.color.engine_color_grey_200);
				break;
			case LIGHT_THEME:
			default:color = getColor(R.color.engine_color_grey_800);
		}
		return color;
	}

	public int getSubTextColor() {
		int color;
		switch (baseTheme) {
			case DARK_THEME:
				color = getColor(R.color.engine_color_grey_400);
				break;
			case AMOLED_THEME:
				color = getColor(R.color.engine_color_grey_400);
				break;
			case LIGHT_THEME:
			default:
				color = getColor(R.color.engine_color_grey_600);
		}
		return color;
	}


	public int getCardBackgroundColor() {
		int color;
		switch (baseTheme) {
			case DARK_THEME:
				color = getColor(R.color.engine_dark_color_background);
				break;
			case AMOLED_THEME:
				color = getColor(R.color.engine_dark_color_background);
				break;
			case LIGHT_THEME:
			default:
				color = getColor(R.color.engine_light_color_background);
		}
		return color;
	}

	public int getIconColor() {
		int color;
		switch (baseTheme) {
			case DARK_THEME: 
				color = getColor(R.color.engine_dark_color_icons);
				break;
			case AMOLED_THEME:
				color = getColor(R.color.engine_black_color_icons);
				break;
			case LIGHT_THEME:
			default:
				color = getColor(R.color.engine_light_color_icons);
		}
		return color;
	}

	public int getButtonBackgroundColor() {
		switch (baseTheme) {
			case DARK_THEME: 
				return getColor(R.color.engine_color_grey_700);
			case AMOLED_THEME: 
				return getColor(R.color.engine_color_grey_900);
			case LIGHT_THEME:
			default:
				return getColor(R.color.engine_color_grey_200);
		}
	}

	public static Drawable getToolbarIcon(int icon) {
		Theme theme = Theme.getInstance();
		Drawable drawable =  ContextCompat.getDrawable(Engine.getContext(), icon);
		return Theme.getIcon(drawable, theme.getIconColor());
	}

	public Drawable getIcon(int icon) {
		Drawable drawable =  ContextCompat.getDrawable(Engine.getContext(), icon);
		return Theme.getIcon(drawable, getIconColor());
	}

    /**
	 * Tint the drawable resource with color
	 *
	 * @param res resources location
	 * @param resId drawable resource id
	 * @param tint color to apply to the image
	 * @return the drawable after tint as a new copy, original resource will not be changed
	 */
    public static Drawable getIcon(Resources res, int resId, int tint) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        // make a copy of the drawable object
        Drawable bitmapDrawable = new BitmapDrawable(res, bitmap);
        // setup color filter for tinting
        ColorFilter cf = new PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN);
        bitmapDrawable.setColorFilter(cf);

        return bitmapDrawable;
    }
    /**
     * Tint the drawable with color
     *
     * @param drawable to be tint
     * @param tint color to apply to the image
     * @return the drawable after tint as a new copy, original resource will not be changed
     */
    public static Drawable getIcon(Drawable drawable, int tint) {
        // clone the drawable
        Drawable clone = drawable.mutate();
        // setup color filter for tinting
        ColorFilter cf = new PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN);
        if (clone != null)
            clone.setColorFilter(cf);

        return clone;
    }

	public int getDrawerBackground() {
		switch (baseTheme) {
			case DARK_THEME:
				return getColor(R.color.engine_dark_color_background_drawer);
			case AMOLED_THEME:
				return getColor(R.color.engine_black_color_background_drawer);
			case LIGHT_THEME:
			default:
				return getColor(R.color.engine_light_color_background_drawer);
		}
	}

	public Drawable getPlaceHolder() {
		switch (baseTheme) {
			case DARK_THEME:
				return ContextCompat.getDrawable(context, R.drawable.engine_empty_dark);
			case AMOLED_THEME:
				return ContextCompat.getDrawable(context, R.drawable.engine_empty_amoled);
			case LIGHT_THEME:
				return ContextCompat.getDrawable(context, R.drawable.engine_empty_white);
		}
		return null;
	}

	/*public CardViewStyle getCardViewStyle() {
	 return CardViewStyle.fromValue(Hawk.get("card_view_style", CardViewStyle.MATERIAL.getValue()));
	 }*/

	public static Drawable getPlaceHolder(Context context) {
		switch (getApplicationTheme()) {
			case DARK_THEME:
				return ContextCompat.getDrawable(context, R.drawable.engine_empty_dark);
			case AMOLED_THEME:
				return ContextCompat.getDrawable(context, R.drawable.engine_empty_amoled);
			case LIGHT_THEME:
				return ContextCompat.getDrawable(context, R.drawable.engine_empty_white);
		}
		return null;
	}

	public int getDialogStyle() {
		switch (baseTheme) {
			case DARK_THEME: return R.style.AlertDialog_Dark;
			case AMOLED_THEME: return R.style.AlertDialog_Dark_Amoled;
			case LIGHT_THEME: default: return R.style.AlertDialog_Light;
		}
	}

	public int getToolbarTheme() {
		switch (baseTheme) {
			case DARK_THEME: return R.style.ThemeOverlay_AppCompat_Light;
			case AMOLED_THEME: return R.style.ThemeOverlay_AppCompat_Light;
			case LIGHT_THEME: default: return R.style.ThemeOverlay_AppCompat_Dark_ActionBar;
		}
	}

	public int getPopupToolbarStyle() {
		switch (baseTheme) {
			case DARK_THEME: return R.style.DarkActionBarMenu;
			case AMOLED_THEME: return R.style.AmoledDarkActionBarMenu;
			case LIGHT_THEME: default: return R.style.LightActionBarMenu;
		}
	}

	public ArrayAdapter<String> getSpinnerAdapter(ArrayList<String> items) {
		switch (baseTheme) {
			case AMOLED_THEME:
			case DARK_THEME:
				return new ArrayAdapter<String>(context, R.layout.spinner_item_light, items);
			case LIGHT_THEME:
			default: return new ArrayAdapter<String>(context, R.layout.spinner_item_dark, items);
		}
	}

	public int getDefaultThemeToolbarColor3th() {
		switch (baseTheme) {
			case DARK_THEME:
				return getColor(R.color.engine_color_black);
			case LIGHT_THEME:
			default:
			case AMOLED_THEME:
				return getColor(R.color.engine_color_blue_grey_800);
		}
	}

	public ColorStateList getTintList() {
		return new ColorStateList(
			new int[][]{
				new int[]{ -android.R.attr.state_enabled }, //disabled
				new int[]{ android.R.attr.state_enabled } //enabled
			}, new int[] { getTextColor(), getAccentColor() });
	}

	private ColorStateList getRadioButtonColor() {
		return new ColorStateList(
			new int[][]{
				new int[]{ -android.R.attr.state_enabled }, //disabled
				new int[]{ android.R.attr.state_enabled } //enabled
			}, new int[] { getTextColor(), getAccentColor() });
	}


	public void updateRadioButtonColor(RadioButton radioButton) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			radioButton.setButtonTintList(getRadioButtonColor());
			radioButton.setTextColor(getTextColor());
		}
	}

	public void setRadioTextButtonColor(RadioButton radioButton, int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			radioButton.setButtonTintList(getRadioButtonColor());
			radioButton.setTextColor(color);
		}
	}

	public void themeCheckBox(CheckBox chk) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			chk.setButtonTintList(getTintList());
			chk.setTextColor(getTextColor());
		}
	}

	public void themeButton(Button btn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			btn.setTextColor(getTextColor());
			btn.setBackgroundColor(getButtonBackgroundColor());
		}
	}

	public void updateSwitchColor(SwitchCompat sw, int color) {
		sw.getThumbDrawable().setColorFilter(sw.isChecked() ? color : getSubTextColor(), PorterDuff.Mode.MULTIPLY);
		sw.getTrackDrawable().setColorFilter(sw.isChecked() ? ColorPalette.getTransparentColor(color, 100): getBackgroundColor(), PorterDuff.Mode.MULTIPLY);
	}


	public void setTextViewColor(TextView txt, int color) {
		txt.setTextColor(color);
	}

	public void setScrollViewColor(ScrollView scr) {
		try {
			Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
			mScrollCacheField.setAccessible(true);
			Object mScrollCache = mScrollCacheField.get(scr); // scr is your Scroll View

			Field scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
			scrollBarField.setAccessible(true);
			Object scrollBar = scrollBarField.get(mScrollCache);

			Method method = scrollBar.getClass().getDeclaredMethod("setVerticalThumbDrawable", Drawable.class);
			method.setAccessible(true);

			ColorDrawable ColorDraw = new ColorDrawable(getPrimaryColor());
			method.invoke(scrollBar, ColorDraw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setColorScrollBarDrawable(Drawable drawable) {
		drawable.setColorFilter(new PorterDuffColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP));
	}

	public static void setCursorDrawableColor(EditText editText, int color) {
		try {
			Field fCursorDrawableRes =
				TextView.class.getDeclaredField("mCursorDrawableRes");
			fCursorDrawableRes.setAccessible(true);
			int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
			Field fEditor = TextView.class.getDeclaredField("mEditor");
			fEditor.setAccessible(true);
			Object editor = fEditor.get(editText);
			Class<?> clazz = editor.getClass();
			Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
			fCursorDrawable.setAccessible(true);

			Drawable[] drawables = new Drawable[2];
			drawables[0] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
			drawables[1] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
			drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
			drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
			fCursorDrawable.set(editor, drawables);
		} catch (final Throwable ignored) {  }
	}


}
