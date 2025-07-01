package com.scriptme.story.engine.app.tasks;

public interface TaskListener<T> {
    void onCompleted();

    void onSuccess(T result);

    void onError(Exception e);
}
