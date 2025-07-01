package com.scriptme.story.engine.app.terminal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Toast;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.settings.theme.Theme;

public class TerminalListAdapter extends BaseAdapter {

	// Declare Variables
    private Context context;
    private String[] title,message;
    private int[] image;
    
	private View mLineView;
	private TextView mTitleText, mSummaryText;
	private LinearLayout mPointFrame;
	private LinearLayout mRightContainer;
	private ImageView mDoneIconView;
	private View mMarginBottomView;
	private Theme theme;

    public TerminalListAdapter(Context context, String[] title, String[] message, int[] image) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.image = image;
		this.theme = Theme.getInstance();
    }

    @Override
    public int getCount() {
        return title.length;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflateView = inflater.inflate(R.layout.item_terminal, parent, false);

		mLineView = inflateView.findViewById(R.id.stepper_line);
		mTitleText = inflateView.findViewById(R.id.stepper_title);
		mSummaryText = inflateView.findViewById(R.id.stepper_summary);
		mPointFrame = inflateView.findViewById(R.id.stepper_point_frame);
		mRightContainer = inflateView.findViewById(R.id.stepper_right_layout);
		mDoneIconView = inflateView.findViewById(R.id.stepper_done_icon);
		mMarginBottomView = inflateView.findViewById(R.id.stepper_margin_bottom);

	    // Set title top margin
		mTitleText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					int singleLineHeight = mTitleText.getMeasuredHeight();
					int topMargin = (mPointFrame.getMeasuredHeight() - singleLineHeight) / 2;
					// Only update top margin when it is positive, preventing titles being truncated.
					if (topMargin > 0) {
						ViewGroup.MarginLayoutParams mlp = (MarginLayoutParams) mTitleText.getLayoutParams();
						mlp.topMargin = topMargin;
					}
				}
			});

        // Capture position and set to the ImageView
        mDoneIconView.setImageResource(image[position]);
		mDoneIconView.setColorFilter(theme.getIconColor());
		mTitleText.setText(title[position]);
		mTitleText.setTextColor(theme.getTextColor());
		mSummaryText.setText(message[position]);
		mSummaryText.setTextColor(theme.getSubTextColor());
        return inflateView;
    }

}
