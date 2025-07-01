package com.scriptme.story.engine.app.folders.archiver;

public abstract class UnzipCallback implements IExtractCallback {
    @Override
    public void onStart() {
    }

    @Override
    public void onGetFileNum(int fileNum) {
    }

    @Override
    public void onProgress(String name, long size) {
    }

    @Override
    public void onError(int errorCode, String message) {
    }

    @Override
    public void onSucceed() {
    }
}
