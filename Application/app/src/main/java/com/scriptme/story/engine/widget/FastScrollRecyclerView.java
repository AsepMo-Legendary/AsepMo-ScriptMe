package com.scriptme.story.engine.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FastScrollRecyclerView extends RecyclerView implements RecyclerView.OnItemTouchListener {

    private FastScroller mScrollbar;

    /**
     * The current scroll state of the recycler view.  We use this in onUpdateScrollbar()
     * and scrollToPositionAtProgress() to determine the scroll position of the recycler view so
     * that we can calculate what the scroll bar looks like, and where to jump to from the fast
     * scroller.
     */
    public static class ScrollPositionState {
        // The index of the first visible row
        public int rowIndex;
        // The offset of the first visible row
        public int rowTopOffset;
        // The height of a given row (they are currently all the same height)
        public int rowHeight;
    }

    private ScrollPositionState mScrollPosState = new ScrollPositionState();

    private int mDownX;
    private int mDownY;
    private int mLastY;

    public FastScrollRecyclerView(Context context) {
        this(context, null);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScrollbar = new FastScroller(context, this, attrs);
    }

    //Todo: move
    // move where and why?
    public int getScrollBarWidth() {
        return mScrollbar.getWidth();
    }

    public int getScrollBarThumbHeight() {
        return mScrollbar.getThumbHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addOnItemTouchListener(this);
    }

    /**
     * We intercept the touch handling only to support fast scrolling when initiated from the
     * scroll bar.  Otherwise, we fall back to the default RecyclerView touch handling.
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent ev) {
        return handleTouchEvent(ev);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent ev) {
        handleTouchEvent(ev);
    }

    /**
     * Handles the touch event and determines whether to show the fast scroller (or updates it if
     * it is already showing).
     */
    private boolean handleTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Keep track of the down positions
                mDownX = x;
                mDownY = mLastY = y;
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = y;
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mScrollbar.handleTouchEvent(ev, mDownX, mDownY, mLastY);
                break;
        }
        return mScrollbar.isDragging();
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * Returns the available scroll height:
     * AvailableScrollHeight = Total height of the all items - last page height
     *
     * This assumes that all rows are the same height.
     *
     * @param yOffset the offset from the top of the recycler view to start tracking.
     */
    protected int getAvailableScrollHeight(int rowCount, int rowHeight, int yOffset) {
        int visibleHeight = getHeight();
        int scrollHeight = getPaddingTop() + yOffset + rowCount * rowHeight + getPaddingBottom();
        int availableScrollHeight = scrollHeight - visibleHeight;
        return availableScrollHeight;
    }

    /**
     * Returns the available scroll bar height:
     * AvailableScrollBarHeight = Total height of the visible view - thumb height
     */
    protected int getAvailableScrollBarHeight() {
        int visibleHeight = getHeight();
        int availableScrollBarHeight = visibleHeight - mScrollbar.getThumbHeight();
        return availableScrollBarHeight;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        onUpdateScrollbar();
        mScrollbar.draw(canvas);
    }

    /**
     * Updates the scrollbar thumb offset to match the visible scroll of the recycler view.  It does
     * this by mapping the available scroll area of the recycler view to the available space for the
     * scroll bar.
     *
     * @param scrollPosState the current scroll position
     * @param rowCount       the number of rows, used to calculate the total scroll height (assumes that
     *                       all rows are the same height)
     * @param yOffset        the offset to start tracking in the recycler view (only used for all apps)
     */
    protected void synchronizeScrollBarThumbOffsetToViewScroll(ScrollPositionState scrollPosState, int rowCount, int yOffset) {
        int availableScrollHeight = getAvailableScrollHeight(rowCount, scrollPosState.rowHeight, yOffset);
        int availableScrollBarHeight = getAvailableScrollBarHeight();

        // Only show the scrollbar if there is height to be scrolled
        if (availableScrollHeight <= 0) {
            mScrollbar.setThumbPosition(-1, -1);
            return;
        }

        // Calculate the current scroll position, the scrollY of the recycler view accounts for the
        // view padding, while the scrollBarY is drawn right up to the background padding (ignoring
        // padding)
        int scrollY = getPaddingTop() + yOffset + (scrollPosState.rowIndex * scrollPosState.rowHeight) - scrollPosState.rowTopOffset;
        int scrollBarY = (int) (((float) scrollY / availableScrollHeight) * availableScrollBarHeight);

        // Calculate the position and size of the scroll bar
        int scrollBarX;
        if (FastScrollUtils.isRtl(getResources())) {
            scrollBarX = 0;
        } else {
            scrollBarX = getWidth() - mScrollbar.getWidth();
        }
        mScrollbar.setThumbPosition(scrollBarX, scrollBarY);
    }

    /**
     * Maps the touch (from 0..1) to the adapter position that should be visible.
     */
    public String scrollToPositionAtProgress(float touchFraction) {
        int itemCount = getAdapter().getItemCount();
        if (itemCount == 0) {
            return "";
        }
        int spanCount = 1;
        int rowCount = itemCount;
        if (getLayoutManager() instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) getLayoutManager()).getSpanCount();
            rowCount = (int) Math.ceil((double) rowCount / spanCount);
        }

        // Stop the scroller if it is scrolling
        stopScroll();

        getCurScrollState(mScrollPosState);

        float itemPos = itemCount * touchFraction;

        int availableScrollHeight = getAvailableScrollHeight(rowCount, mScrollPosState.rowHeight, 0);

        //The exact position of our desired item
        int exactItemPos = (int) (availableScrollHeight * touchFraction);

        //Scroll to the desired item. The offset used here is kind of hard to explain.
        //If the position we wish to scroll to is, say, position 10.5, we scroll to position 10,
        //and then offset by 0.5 * rowHeight. This is how we achieve smooth scrolling.
        LinearLayoutManager layoutManager = ((LinearLayoutManager) getLayoutManager());
        layoutManager.scrollToPositionWithOffset(spanCount * exactItemPos / mScrollPosState.rowHeight,
                -(exactItemPos % mScrollPosState.rowHeight));

        if (!(getAdapter() instanceof SectionedAdapter)) {
            return "";
        }

        int posInt = (int) ((touchFraction == 1) ? itemPos - 1 : itemPos);

        SectionedAdapter sectionedAdapter = (SectionedAdapter) getAdapter();
        return sectionedAdapter.getSectionName(posInt);
    }

    /**
     * Updates the bounds for the scrollbar.
     */
    public void onUpdateScrollbar() {

        if (getAdapter() == null) {
            return;
        }

        int rowCount = getAdapter().getItemCount();
        if (getLayoutManager() instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) getLayoutManager()).getSpanCount();
            rowCount = (int) Math.ceil((double) rowCount / spanCount);
        }
        // Skip early if, there are no items.
        if (rowCount == 0) {
            mScrollbar.setThumbPosition(-1, -1);
            return;
        }

        // Skip early if, there no child laid out in the container.
        getCurScrollState(mScrollPosState);
        if (mScrollPosState.rowIndex < 0) {
            mScrollbar.setThumbPosition(-1, -1);
            return;
        }

        synchronizeScrollBarThumbOffsetToViewScroll(mScrollPosState, rowCount, 0);
    }

    /**
     * Returns the current scroll state of the apps rows.
     */
    private void getCurScrollState(ScrollPositionState stateOut) {
        stateOut.rowIndex = -1;
        stateOut.rowTopOffset = -1;
        stateOut.rowHeight = -1;

        int itemCount = getAdapter().getItemCount();

        // Return early if there are no items, or no children.
        if (itemCount == 0 || getChildCount() == 0) {
            return;
        }
        
        View child = getChildAt(0);

        stateOut.rowIndex = getChildAdapterPosition(child);
        if (getLayoutManager() instanceof GridLayoutManager) {
            stateOut.rowIndex = stateOut.rowIndex / ((GridLayoutManager) getLayoutManager()).getSpanCount();
        }
        stateOut.rowTopOffset = getLayoutManager().getDecoratedTop(child);
        stateOut.rowHeight = child.getHeight();
    }

    public void setThumbColor(@ColorInt int color) {
        mScrollbar.setThumbColor(color);
    }

    public void setTrackColor(@ColorInt int color) {
        mScrollbar.setTrackColor(color);
    }

    public void setPopupBgColor(@ColorInt int color) {
        mScrollbar.setPopupBgColor(color);
    }

    public void setPopupTextColor(@ColorInt int color) {
        mScrollbar.setPopupTextColor(color);
    }

    public void setAutoHideDelay(int hideDelay) {
        mScrollbar.setAutoHideDelay(hideDelay);
    }

    public void setAutoHideEnabled(boolean autoHideEnabled) {
        mScrollbar.setAutoHideEnabled(autoHideEnabled);
    }

    public interface SectionedAdapter {
        @NonNull
        String getSectionName(int position);
    }
}
