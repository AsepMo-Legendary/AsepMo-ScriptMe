package com.scriptme.story.engine.app.folders.explorer;

import android.support.v7.view.ActionMode;

public interface FileExplorerView {
    ActionMode startActionMode(ActionMode.Callback callback);

    void setSelectAll(boolean checked);

    void refresh();

    void finish();

    void filter(String query);
}
