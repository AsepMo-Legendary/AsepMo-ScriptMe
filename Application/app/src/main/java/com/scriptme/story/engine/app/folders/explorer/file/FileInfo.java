package com.scriptme.story.engine.app.folders.explorer.file;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.Serializable;

public class FileInfo implements Serializable {

    /**
     * When info isn't accessible (e.g. /persist)
     */
    public boolean readAvailable;

    public String name;

    //These are null if type isn't symlink
    public String linkedPath;
    public File realFile;

    public String permissions;
    public String owner;
    public String group;
    public long size;
    public long lastModified;
    public String directoryFileCount;

    public boolean isSymlink;
    public boolean isDirectory;

    /**
     * Should be used by LsParser only
     */
    public FileInfo(boolean readAvailable, @NonNull String name) {
        this.name = name;
        this.permissions = "";
        this.owner = "";
        this.group = "";
        this.size = -1;
        this.lastModified = -1;
        this.directoryFileCount = "";
        this.readAvailable = readAvailable;
    }

    /**
     * Should be used by FileAdapter
     */
    public FileInfo(FileInfo info) {
        name = info.name;
        permissions = info.permissions;
        owner = info.owner;
        group = info.group;
        size = info.size;
        lastModified = info.lastModified;
        directoryFileCount = info.directoryFileCount;
        readAvailable = info.readAvailable;
        isSymlink = info.isSymlink;
        isDirectory = info.isDirectory;
        realFile = info.realFile;
        linkedPath = info.linkedPath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof FileInfo) {
            FileInfo other = (FileInfo) o;
            if (other.readAvailable && readAvailable) {
                try {
                    return (((other.name == null) == (name == null)) ||
                            (other.name != null && other.name.equals(name))) &&
                            other.permissions.equals(permissions) &&
                            other.owner.equals(owner) &&
                            other.group.equals(group) &&
                            other.size == size &&
                            other.lastModified == lastModified &&
                            other.directoryFileCount.equals(directoryFileCount);
                } catch (NullPointerException e) {
                    throw new IllegalStateException("Unexpected null values when comparing FileInfos.\t\n" +
                            "Name: " + other.name + "\t\nPermissions: " + other.permissions + "\t\n" +
                            "Owner: " + other.owner + "\t\nGroup: " + other.group + "\t\nSize: " + other.size + "\t\n" +
                            "Last modified: " + other.lastModified + "\t\nFile count: " + other.directoryFileCount);
                }
            } else if (!other.readAvailable && !readAvailable) {
                return other.name.equals(name);
            }
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

}
