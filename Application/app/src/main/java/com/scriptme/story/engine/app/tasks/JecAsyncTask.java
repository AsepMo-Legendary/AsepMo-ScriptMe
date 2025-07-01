package com.scriptme.story.engine.app.tasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public abstract class JecAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private Exception exception;
    private TaskListener<Result> listener;
    private ProgressDialog progressInterface;
    private boolean complete = false;

    public JecAsyncTask<Params, Progress, Result> setTaskListener(TaskListener<Result> listener) {
        this.listener = listener;
        return this;
    }

    public ProgressDialog getProgress() {
        return progressInterface;
    }

    public void setProgress(ProgressDialog progressInterface) {
        this.progressInterface = progressInterface;
        if (progressInterface != null) {
            progressInterface.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (!complete) {
                        cancel(true);
                    }
                }
            });
        }
    }

    @Override
    protected void onPreExecute() {
        if (progressInterface != null) {
            progressInterface.show();
        }
    }

    @Override
    protected final Result doInBackground(Params... params) {
        TaskResult<Result> taskResult = new TaskResult<>();

        try {
            onRun(taskResult, params);
            taskResult.waitResult();
            return taskResult.getResult();
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected final void onPostExecute(Result result) {
        if (!isCancelled()) {
            onComplete();
            if (exception == null) {
                onSuccess(result);
            } else {
                onError(exception);
            }
        }
    }

    @Override
    protected void onCancelled() {
        onComplete();
    }

    protected void onComplete() {
        complete = true;
        if (listener != null)
            listener.onCompleted();

        if (progressInterface != null) {
            progressInterface.dismiss();
        }
    }

    protected void onSuccess(Result result) {
        if (listener != null)
            listener.onSuccess(result);
    }

    protected void onError(Exception e) {
        if (listener != null)
            listener.onError(e);
    }

    protected abstract void onRun(TaskResult<Result> taskResult, Params... params) throws Exception;

}
