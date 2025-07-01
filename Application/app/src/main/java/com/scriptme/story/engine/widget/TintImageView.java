package com.scriptme.story.engine.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class TintImageView extends AppCompatImageView {
    private ColorStateList colorList;

    public TintImageView(Context context) {
        super(context);
        init(context, null);
    }

    public TintImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TintImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray arr =
                    context.obtainStyledAttributes(attrs, new int[]{android.R.attr.textColor});
            colorList = arr.getColorStateList(0);
            if (colorList != null) {
                setColorFilter(colorList.getDefaultColor());
            }
            arr.recycle();
        }
    }

    @Override
    public void dispatchDrawableHotspotChanged(float x, float y) {
        super.dispatchDrawableHotspotChanged(x, y);
        clearColorFilter();
        setColorFilter(colorList.getColorForState(getDrawableState(), colorList.getDefaultColor()));
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
    }
}
