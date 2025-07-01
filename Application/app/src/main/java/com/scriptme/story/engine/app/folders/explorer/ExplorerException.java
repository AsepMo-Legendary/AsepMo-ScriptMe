package com.scriptme.story.engine.app.folders.explorer;

public class ExplorerException extends RuntimeException {
    public ExplorerException(String detailMessage) {
        super(detailMessage);
    }

    public ExplorerException(Throwable throwable) {
        super(throwable);
    }

    public ExplorerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ExplorerException() {
    }
}
