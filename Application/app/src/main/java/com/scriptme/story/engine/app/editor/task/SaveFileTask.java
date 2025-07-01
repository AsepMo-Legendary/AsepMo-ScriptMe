package com.scriptme.story.engine.app.editor.task;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.activity.EditorActivity;
import com.scriptme.story.engine.app.editor.root.RootUtils;
import com.scriptme.story.engine.app.editor.shell.Shell;
import com.scriptme.story.engine.app.editor.shell.Toolbox;
import com.scriptme.story.engine.app.editor.util.EventBusEvents;

public class SaveFileTask extends AsyncTask<Void, Void, Void> {

    private final EditorActivity activity;
    private final String filePath;
    private final String text;
    private final String encoding;
    private File file;
    private String message;
    private String positiveMessage;

    public SaveFileTask(EditorActivity activity, String filePath, String text, String encoding) {
        this.activity = activity;
        this.filePath = filePath;
        this.text = text;
        this.encoding = encoding;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        file = new File(filePath);
        positiveMessage = String.format(activity.getString(R.string.engine_editor_file_saved_with_success), file.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(final Void... voids) {

        try {

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            boolean isRoot = false;
            if (!file.canWrite()) {
                try {
                    Shell shell = null;
                    shell = Shell.startRootShell();
                    Toolbox tb = new Toolbox(shell);
                    isRoot = tb.isRootAccessGiven();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                    isRoot = false;
                }
            }

            RootUtils.writeFile(activity, file.getAbsolutePath(), text, encoding, isRoot);

            message = positiveMessage;
        } catch (Exception e) {
            message = e.getMessage();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(final Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        if (message.equals(positiveMessage))
            activity.onEvent(new EventBusEvents.SavedAFile(filePath));
    }
}
