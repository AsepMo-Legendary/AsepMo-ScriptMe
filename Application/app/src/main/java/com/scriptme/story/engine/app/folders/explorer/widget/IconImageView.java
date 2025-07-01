package com.scriptme.story.engine.app.folders.explorer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.scriptme.story.R;
import com.scriptme.story.engine.widget.RoundedImageView;

public class IconImageView extends RoundedImageView implements Checkable {
    private int defaultBackgroundColor;
    private int defaultImageResource;
    private boolean checked;

    public IconImageView(Context context) {
        super(context);
    }

    public IconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDefaultBackgroundColor(int color) {
        super.setBackgroundColor(color);
        defaultBackgroundColor = color;
    }

    public void setDefaultImageResource(int drawable) {
        super.setImageResource(drawable);
        defaultImageResource = drawable;
    }

    public void reset() {
        setBackgroundColor(defaultBackgroundColor);
        setImageResource(defaultImageResource);
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked) {
            setBackgroundColor(getResources().getColor(R.color.engine_color_grey_400));
            setImageResource(R.drawable.ic_action_check_white);
        } else {
            setBackgroundColor(defaultBackgroundColor);
            setImageResource(defaultImageResource);
        }
    }

    @Override
    public void toggle() {
        checked = !checked;
        setChecked(checked);
    }
}
