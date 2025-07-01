package com.scriptme.story.engine.app.editor.root;

import android.content.Context;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.scriptme.story.engine.app.editor.shell.Shell;
import com.scriptme.story.engine.app.editor.shell.Toolbox;

public class RootUtils {

    public static void writeFile(Context context, String path, String text, String encoding, boolean isRoot) throws Exception {
        File file = new File(path);
        if (!file.canWrite() && isRoot) {
            File appFolder = context.getFilesDir();
            File tempFile = new File(appFolder, "temp.root.file");
            if (!tempFile.exists())
                tempFile.createNewFile();
            FileUtils.write(tempFile, text, encoding);
            Shell shell = Shell.startRootShell();
            Toolbox tb = new Toolbox(shell);
            String mount = tb.getFilePermissions(path);
            tb.copyFile(tempFile.getAbsolutePath(), path, true, false);
            tb.setFilePermissions(path, mount);
            tempFile.delete();
        } else {
            FileUtils.write(file,
                    text,
                    encoding);
        }
    }

    public static ArrayList<File> getFileList(String path, boolean runAtRoot) {
        ArrayList<File> filesList = new ArrayList<File>();
        if (runAtRoot == false) {
            File base = new File(path);
            File[] files = base.listFiles();
            if (files == null)
                return null;
            Collections.addAll(filesList, files);
        } else {
            BufferedReader reader = null; //errReader = null;
            try {
                LinuxShell.execute("ls -a " + LinuxShell.getCommandLineString(path));
                if (reader == null)
                    return null;

                File f;
                String line;
                while ((line = reader.readLine()) != null) {
                    f = new File(line.substring(2));
                    filesList.add(f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filesList;
    }

}
