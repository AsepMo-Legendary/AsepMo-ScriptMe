package com.scriptme.story.engine.widget;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.settings.theme.Theme;

public class FrameView extends RelativeLayout  {

    public static final String TAG = "AboutView";

    private Context mContext;
	private AppCompatActivity activity;
    private View mFrame;
    private RelativeLayout webContainer;
    private FrameLayout mContentFrame;
    private LinearLayout mLoadingView;
	private ImageView mFrameView;
	private TextView mMessage;
	private Integer shortAnimDuration;
	private Theme theme;
	
    public FrameView(Context context) {
        super(context);
        init(context, null);
    }

    public FrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
		this.activity = (AppCompatActivity)context;
		shortAnimDuration = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);
		theme = Theme.getInstance();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setKeepScreenOn(true);


        // Instantiate and add AboutPage for View
        final LayoutInflater li = LayoutInflater.from(getContext());
        mFrame = li.inflate(R.layout.layout_frame_view, this, false);
        addView(mFrame);
		webContainer = mFrame.findViewById(R.id.frame_view_container);
		webContainer.setBackgroundColor(theme.getBackgroundColor());
		mLoadingView = mFrame.findViewById(R.id.frame_view_layout);
		mFrameView = mFrame.findViewById(R.id.iframe_view);
		mFrameView.setColorFilter(theme.getIconColor());
		mMessage = mFrame.findViewById(R.id.text_message);
		StringBuilder sb = new StringBuilder();
		sb.append("This Is ScriptMe Application").append("\n");
		sb.append("For Edit And Run Script").append("\n");
		mMessage.setText(sb.toString());
		mMessage.setTextColor(theme.getTextColor());
		mContentFrame = (FrameLayout) mFrame.findViewById(R.id.content_frame);    
    }
	
	public void addFrame(final Fragment fragment) {
		crossFade(mContentFrame, mLoadingView);
			new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (mLoadingView.getVisibility() == View.VISIBLE) {
							crossFade(mLoadingView, mContentFrame);
							FragmentManager fm = activity.getSupportFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							ft.replace(R.id.content_frame, fragment);
							ft.commit();		      
						}
					}
				}, 500);
	}

	private void crossFade(final View toHide, View toShow) {
        toShow.setAlpha(0f);
        toShow.setVisibility(View.VISIBLE);

        toShow.animate()
			.alpha(1f)
			.setDuration(shortAnimDuration)
			.setListener(null);

        toHide.animate()
			.alpha(0f)
			.setDuration(shortAnimDuration)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					toHide.setVisibility(View.GONE);
				}
			});
    }
}
