package com.scriptme.story.engine.app.folders.ebook.exception;

public class PageRenderingException extends Exception {
    private final int page;

    public PageRenderingException(int page, Throwable cause) {
        super(cause);
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
