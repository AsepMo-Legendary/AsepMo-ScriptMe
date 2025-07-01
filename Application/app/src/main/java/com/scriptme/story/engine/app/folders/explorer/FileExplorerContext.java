package com.scriptme.story.engine.app.folders.explorer;

import android.content.Context;
import android.content.res.Resources;

public class FileExplorerContext {
    private final Context context;

    public FileExplorerContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public Resources getResources() {
        return context.getResources();
    }
}
