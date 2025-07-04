package com.scriptme.story.engine.app.listeners;

import android.util.Log;
import android.support.v7.widget.RecyclerView;

public abstract class OnRecyclerScrollListener extends RecyclerView.OnScrollListener {
    int scrollDist = 0;
    boolean isVisible = true;
    static final float MINIMUM = 5;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
//        Log.d("OskarSchindler", "Scroll Distance "+scrollDist);

        if (isVisible && scrollDist > MINIMUM) {
            Log.d("OskarSchindler", "Hide " + scrollDist);
            hide();
            scrollDist = 0;
            isVisible = false;
        } else if (!isVisible && scrollDist < -MINIMUM) {
            Log.d("OskarSchindler", "Show " + scrollDist);
            show();
            scrollDist = 0;
            isVisible = true;
        }
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            Log.d("OskarSchindler", "Add Up " + scrollDist);
            scrollDist += dy;
        }
    }

    public abstract void show();

    public abstract void hide();
}

