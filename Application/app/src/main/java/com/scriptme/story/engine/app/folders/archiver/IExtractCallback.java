package com.scriptme.story.engine.app.folders.archiver;

public interface IExtractCallback {
    void onStart();

    void onGetFileNum(int fileNum);

    void onProgress(String name, long size);

    void onError(int errorCode, String message);

    void onSucceed();
}
