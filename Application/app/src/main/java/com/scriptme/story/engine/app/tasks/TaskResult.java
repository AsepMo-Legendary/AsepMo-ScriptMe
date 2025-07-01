package com.scriptme.story.engine.app.tasks;

public class TaskResult<T> {
    private T result;
    private boolean waitResult;
    private boolean hasResult;

    void waitResult() throws InterruptedException {
        this.waitResult = true;
        if (!hasResult)
            wait();
    }

    T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.hasResult = true;
        this.result = result;
        if (waitResult)
            notify();
    }
}
