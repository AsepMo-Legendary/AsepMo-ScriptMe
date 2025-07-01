package com.scriptme.story.engine.app.folders.explorer.file;

import android.os.Parcel;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.scriptme.story.engine.app.folders.explorer.ExplorerException;
import com.scriptme.story.engine.app.folders.explorer.listener.BoolResultListener;
import com.scriptme.story.engine.app.folders.explorer.listener.FileListResultListener;
import com.scriptme.story.engine.app.folders.explorer.listener.ProgressUpdateListener;

public class LocalFile extends BaseFile {
    public static final Creator<LocalFile> CREATOR = new Creator<LocalFile>() {
        @Override
        public LocalFile createFromParcel(Parcel source) {
            return new LocalFile(source);
        }

        @Override
        public LocalFile[] newArray(int size) {
            return new LocalFile[size];
        }
    };
    private File file;

    public LocalFile(BaseFile parent, String child) {
        super(parent, child);
        file = new File(parent.getPath(), child);
    }

    public LocalFile(String parent, String child) {
        super(parent, child);
        file = new File(parent, child);
    }

    public LocalFile(String pathname) {
        super(pathname);
        file = new File(pathname);
    }

    protected LocalFile(Parcel in) {
        this(in.readString());
    }

    private static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] files = fileOrDirectory.listFiles();
            if (files != null) {
                for (File child : files) {
                    deleteRecursive(child);
                }
            }
        }
        return fileOrDirectory.delete();
    }

    @Override
    public BaseFile newFile(String filename) {
        return new LocalFile(getPath(), filename);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getParent() {
        return file.getParent();
    }

    @Override
    public BaseFile getParentFile() {
        String parent = file.getParent();
        if (parent == null)
            return null;
        return new LocalFile(parent);
    }

    @Override
    public String getPath() {
        return file.getPath();
    }

    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    @Override
    public BaseFile getAbsoluteFile() {
        return new LocalFile(file.getAbsoluteFile().getPath());
    }

    @Override
    public boolean canRead() {
        return file.canRead();
    }

    @Override
    public boolean canWrite() {
        return file.canWrite();
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public void delete(BoolResultListener listener) {
        boolean result = deleteRecursive(file);
        if (listener != null) {
            listener.onResult(result);
        }
    }

    @Override
    public void listFiles(FileListResultListener listener) {
        if (listener == null)
            throw new NullPointerException();

        File[] files = file.listFiles();
        if (files.length == 0) {
            listener.onResult(new LocalFile[0]);
            return;
        }
        LocalFile[] localFiles = new LocalFile[files.length];
        for (int i = 0; i < files.length; i++) {
            localFiles[i] = new LocalFile(files[i].getPath());
        }

        listener.onResult(localFiles);
    }

    @Override
    public void mkdirs(BoolResultListener listener) {
        boolean result = file.mkdirs();

        if (listener != null)
            listener.onResult(result);
    }

    @Override
    public void renameTo(BaseFile dest, BoolResultListener listener) {
        boolean result = file.renameTo(new File(dest.getPath()));
        if (listener != null)
            listener.onResult(result);
    }

    @Override
    public void copyTo(BaseFile dest, BoolResultListener listener) {
        if (!(dest instanceof LocalFile)) {
            throw new ExplorerException(dest + " !(dest instanceof LocalFile)");
        }
        try {
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(((LocalFile) dest).file);
            IOUtils.copy(in, out);
            in.close();
            out.close();
            if (listener != null)
                listener.onResult(true);
        } catch (Exception e) {
            if (listener != null) {
                listener.onResult(false);
            }
        }
    }

    @Override
    public void upload(LocalFile file, BoolResultListener resultListener, ProgressUpdateListener progressUpdateListener) {

    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public boolean canExecutable() {
        return file.canExecute();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocalFile))
            return false;
        return file.equals(((LocalFile) o).file);
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(file.getPath());
    }
}
