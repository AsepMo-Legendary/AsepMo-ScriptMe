package com.scriptme.story.engine.app.folders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FolderMe {
    
    public static final String TAG = FolderMe.class.getSimpleName();
    private Context context;
	private ProgressDialog mProgressDialog;
    private ExecutorService mExecutor;
	private File installDir;
	
	private FolderMe(Context context){
		this.context = context;
	}
	
	public static FolderMe with(Context context){
		return new FolderMe(context);
	}
	
	public void update(){
		mProgressDialog = new ProgressDialog(context);
        mExecutor = Executors.newSingleThreadExecutor();
        
		File outFile = context.getExternalFilesDir("tmp");
        if (outFile == null || !outFile.exists()) {
            outFile = context.getFilesDir();
        }
        installDir = outFile;
	}
	
	public void shutdown(){
		mExecutor.shutdownNow();
	}
	
	public static String getFiles(String scripts){
		File file = new File(scripts);
		String dirPath = file.isDirectory() ? scripts : file.getParent();
		return "cd " + quoteForBash(dirPath);
	}
	 /**
     *  Quote a string so it can be used as a parameter in bash and similar shells.
     */
    public static String quoteForBash(String s) {
        StringBuilder builder = new StringBuilder();
        String specialChars = "\"\\$`!";
        builder.append('"');
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if (specialChars.indexOf(c) >= 0) {
                builder.append('\\');
            }
            builder.append(c);
        }
        builder.append('"');
        return builder.toString();
    }

    public static String getExtension(String name) {
        String ext;

        if (name.lastIndexOf(".") == -1) {
            ext = "";

        } else {
            int index = name.lastIndexOf(".");
            ext = name.substring(index + 1, name.length());
        }
        return ext;
    }

    public static boolean isSupportedArchive(File file) {
        String ext = getExtension(file.getName());
        return ext.equalsIgnoreCase("zip");
    }
}
