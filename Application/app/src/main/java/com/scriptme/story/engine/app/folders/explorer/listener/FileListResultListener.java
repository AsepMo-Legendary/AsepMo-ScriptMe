package com.scriptme.story.engine.app.folders.explorer.listener;

import com.scriptme.story.engine.app.folders.explorer.file.BaseFile;

public interface FileListResultListener {
    void onResult(BaseFile[] result);
}
