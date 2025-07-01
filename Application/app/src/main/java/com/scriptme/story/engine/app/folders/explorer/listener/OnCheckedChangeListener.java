package com.scriptme.story.engine.app.folders.explorer.listener;

import com.scriptme.story.engine.app.folders.explorer.file.BaseFile;

public interface OnCheckedChangeListener {
    void onCheckedChanged(BaseFile file, int position, boolean checked);
    void onCheckedChanged(int checkedCount);
}
