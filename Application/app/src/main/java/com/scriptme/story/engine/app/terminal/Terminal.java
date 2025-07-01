package com.scriptme.story.engine.app.terminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build;
import android.os.Environment;
import android.os.UserManager;
import android.system.Os;
import android.util.Log;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Terminal {
    
    public static final String TAG = Terminal.class.getSimpleName();
	private Activity activity;
	private ProgressDialog mProgressDialog;
    private ExecutorService mExecutor;
	
    private Terminal(Activity context) {
		this.activity = context;
		this.mProgressDialog = new ProgressDialog(context);
        this.mExecutor = Executors.newSingleThreadExecutor();
		
	}
    
	public static Terminal with(Activity context) {
		return new Terminal(context);
	}
}
