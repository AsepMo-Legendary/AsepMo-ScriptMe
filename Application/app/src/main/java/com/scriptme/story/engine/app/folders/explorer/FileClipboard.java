package com.scriptme.story.engine.app.folders.explorer;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.folders.explorer.file.BaseFile;
import com.scriptme.story.engine.app.folders.explorer.file.FileUtils;
import com.scriptme.story.engine.app.folders.explorer.listener.OnClipboardDataChangedListener;
import com.scriptme.story.engine.app.folders.explorer.listener.OnClipboardPasteFinishListener;
import com.scriptme.story.engine.app.tasks.JecAsyncTask;
import com.scriptme.story.engine.app.tasks.TaskResult;
import com.scriptme.story.engine.app.utils.UIUtils;

public class FileClipboard {
    private List<BaseFile> clipList = new ArrayList<>();
    private boolean isCopy;
    private OnClipboardDataChangedListener onClipboardDataChangedListener;

    public boolean canPaste() {
        return !clipList.isEmpty();
    }

    public void setData(boolean isCopy, List<BaseFile> data) {
        this.isCopy = isCopy;
        clipList.clear();
        clipList.addAll(data);
        if (onClipboardDataChangedListener != null)
            onClipboardDataChangedListener.onClipboardDataChanged();
    }

    public void paste(Context context, BaseFile currentDirectory, OnClipboardPasteFinishListener listener) {
        if (!canPaste())
            return;

        ProgressDialog dlg = new ProgressDialog(context);
        PasteTask task = new PasteTask(listener);
        task.setProgress(dlg);
        task.execute(currentDirectory);
    }

    public void showPasteResult(Context context, int count, String error) {
        if (TextUtils.isEmpty(error)) {
            UIUtils.toast(context, R.string.engine_explore_x_items_completed, count);
        } else {
            UIUtils.toast(context, R.string.engine_explore_x_items_completed_and_error_x, count, error);
        }
    }

    private class PasteTask extends JecAsyncTask<BaseFile, BaseFile, Integer> {
        private final OnClipboardPasteFinishListener listener;
        private StringBuilder errorMsg = new StringBuilder();

        public PasteTask(OnClipboardPasteFinishListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onProgressUpdate(BaseFile... values) {
            getProgress().setMessage(values[0].getPath());
        }

        @Override
        protected void onRun(TaskResult<Integer> taskResult, BaseFile... params) throws Exception {
            BaseFile currentDirectory = params[0];
            int count = 0;
            for (BaseFile file : clipList) {
                publishProgress(file);
                try {
                    if (file.isDirectory()) {
                        FileUtils.copyDirectory(file, currentDirectory, !isCopy);
                    } else {
                        FileUtils.copyFile(file, currentDirectory.newFile(file.getName()), !isCopy);
                    }
                    count++;
                } catch (Exception e) {
                    errorMsg.append(e.getMessage()).append("\n");
                }
            }
            clipList.clear();
            taskResult.setResult(count);
        }

        @Override
        protected void onSuccess(Integer integer) {
            if (listener != null) {
                listener.onFinish(integer, errorMsg.toString());
            }
        }

        @Override
        protected void onError(Exception e) {
            if (listener != null) {
                listener.onFinish(0, e.getMessage());
            }
        }
    }

    public void setOnClipboardDataChangedListener(OnClipboardDataChangedListener onClipboardDataChangedListener) {
        this.onClipboardDataChangedListener = onClipboardDataChangedListener;
    }
}
