package com.scriptme.story.engine.app.settings.theme;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.scriptme.story.engine.app.settings.theme.Theme.AMOLED_THEME;
import static com.scriptme.story.engine.app.settings.theme.Theme.DARK_THEME;
import static com.scriptme.story.engine.app.settings.theme.Theme.LIGHT_THEME;

import com.scriptme.story.R;
import com.scriptme.story.application.ApplicationActivity;

public class StylePreference extends Preference {

    public static final String TAG = "StylePreference";
	public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
	public static final int THEME_AMOLED = 2;

	public AppCompatActivity mActivity;
    private Context mContext;

	private int mValue = 0;
    private int mItemLayoutId = R.layout.layout_styles_preference;
    private View mIconView;
	private int theme;
	private Theme themeHelper;

	public StylePreference(Context context) {
        super(context);
        initAttrs(context, null, 0);
    }

    public StylePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs, 0);
    }

    public StylePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
		themeHelper = Theme.getInstanceLoaded();
		TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StylePreference, defStyle, defStyle);
		try {
            mItemLayoutId = a.getResourceId(R.styleable.StylePreference_itemLayout, mItemLayoutId);
            theme = a.getInt(R.styleable.StylePreference_theme_application, themeHelper.getBaseTheme());

        } finally {
            a.recycle();
        }
		
		setWidgetLayoutResource(mItemLayoutId);
	}

	@Override
	public void onBindViewHolder(PreferenceViewHolder holder) {
		super.onBindViewHolder(holder);
		mIconView = holder.findViewById(R.id.icon_view);
		StyleDialogFragment fragment = (StyleDialogFragment) mActivity.getSupportFragmentManager().findFragmentByTag(getFragmentTag());
        if (fragment != null) {
            // re-bind preference to fragment
            fragment.setPreference(this);
            
        }

	}

	public void setValue(int value) {
        if (callChangeListener(value)) {
            mValue = value;
            persistInt(value);
            notifyChanged();
        }
    }

	@Override
	protected void onClick() {
		super.onClick();
		StyleDialogFragment fragment = StyleDialogFragment.newInstance();
        fragment.setPreference(this);
        
        mActivity.getSupportFragmentManager().beginTransaction()
			.add(fragment, getFragmentTag())
			.commit();
	}



	public void onAttact(AppCompatActivity activity) {
        this.mActivity = activity;
    }


    @Override
    public void onAttached() {
        super.onAttached();

    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(0) : (Integer) defaultValue);
    }

	public String getFragmentTag() {
        return "style_" + getKey();
    }

	public static class StyleDialogFragment extends AppCompatDialogFragment {
       
		private Theme theme;
		private StylePreference mPreference;
		private ThemePreference mSharedPreference;
		
		public StyleDialogFragment() {
			theme = new Theme();
		}

        public static StyleDialogFragment newInstance() {
            return new StyleDialogFragment();
        }

        public void setPreference(StylePreference preference) {
            mPreference = preference;
        }

		@Override
        public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
            final Context context = getActivity();
			mSharedPreference = ThemePreference.getInstance();
			
			final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
			final LayoutInflater dialogInflater = LayoutInflater.from(dialogBuilder.getContext());

            final View dialogLayout = dialogInflater.inflate(R.layout.dialog_basic_theme, null, false);

			final TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.basic_theme_title);
			final CardView dialogCardView = (CardView) dialogLayout.findViewById(R.id.basic_theme_card);

			final AppCompatImageView whiteSelect = (AppCompatImageView) dialogLayout.findViewById(R.id.white_basic_theme_select);
			final AppCompatImageView darkSelect = (AppCompatImageView) dialogLayout.findViewById(R.id.dark_basic_theme_select);
			final AppCompatImageView darkAmoledSelect = (AppCompatImageView) dialogLayout.findViewById(R.id.dark_amoled_basic_theme_select);

			switch (theme.getBaseTheme()) {
				case LIGHT_THEME:
					whiteSelect.setVisibility(View.VISIBLE);
					darkSelect.setVisibility(View.GONE);
					darkAmoledSelect.setVisibility(View.GONE);
					break;
				case DARK_THEME:
					whiteSelect.setVisibility(View.GONE);
					darkSelect.setVisibility(View.VISIBLE);
					darkAmoledSelect.setVisibility(View.GONE);
					break;
				case AMOLED_THEME:
					whiteSelect.setVisibility(View.GONE);
					darkSelect.setVisibility(View.GONE);
					darkAmoledSelect.setVisibility(View.VISIBLE);
					break;
			}

			/** SET OBJ THEME **/
			dialogTitle.setBackgroundColor(theme.getPrimaryColor());
			dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());
			dialogLayout.findViewById(R.id.ll_white_basic_theme).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						whiteSelect.setVisibility(View.VISIBLE);
						darkSelect.setVisibility(View.GONE);
						darkAmoledSelect.setVisibility(View.GONE);
						theme.setBaseTheme(LIGHT_THEME, false);
						dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());				
					}
				});
			dialogLayout.findViewById(R.id.ll_dark_basic_theme).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						whiteSelect.setVisibility(View.GONE);
						darkSelect.setVisibility(View.VISIBLE);
						darkAmoledSelect.setVisibility(View.GONE);
						theme.setBaseTheme(DARK_THEME, false);
						dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());							
					}
				});
			dialogLayout.findViewById(R.id.ll_dark_amoled_basic_theme).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						whiteSelect.setVisibility(View.GONE);
						darkSelect.setVisibility(View.GONE);
						darkAmoledSelect.setVisibility(View.VISIBLE);
						theme.setBaseTheme(AMOLED_THEME, false);
						dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());				
					}
				});
			dialogBuilder.setView(dialogLayout);
			dialogBuilder.setPositiveButton(getString(android.R.string.ok).toUpperCase(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						mSharedPreference.putInt(getString(R.string.engine_settings_base_theme), theme.getBaseTheme());		
						mSharedPreference.putBoolean("recreate", true);
						
						mPreference.mActivity.recreate();
					}
				});
			dialogBuilder.setNegativeButton(getString(android.R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						theme.setBaseTheme(Theme.getApplicationTheme(), false);
						mSharedPreference.putBoolean("recreate", true);
						
					}
				});
			dialogBuilder.setView(dialogLayout);		
			return dialogBuilder.create();
		}
	};
	
}
