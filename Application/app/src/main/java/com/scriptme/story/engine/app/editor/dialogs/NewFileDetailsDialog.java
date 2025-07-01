package com.scriptme.story.engine.app.editor.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.File;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.activity.EditorActivity;
import com.scriptme.story.engine.app.editor.settings.PreferenceHelper;
import com.scriptme.story.engine.app.editor.task.SaveFileTask;

// ...
public class NewFileDetailsDialog extends DialogFragment {

    private EditText mName;
    private EditText mFolder;

    public static NewFileDetailsDialog newInstance(String fileText, String fileEncoding) {
        final NewFileDetailsDialog f = new NewFileDetailsDialog();
        final Bundle args = new Bundle();
        args.putString("fileText", fileText);
        args.putString("fileEncoding", fileEncoding);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = new DialogHelper.Builder(getActivity())
                .setTitle(R.string.engine_editor_file)
                .setView(R.layout.dialog_fragment_new_file_details)
                .createSkeletonView();

        this.mName = (EditText) view.findViewById(android.R.id.text1);
        this.mFolder = (EditText) view.findViewById(android.R.id.text2);

        this.mName.setText(".txt");
        this.mFolder.setText(PreferenceHelper.getWorkingFolder(getActivity()));

        // Show soft keyboard automatically
        this.mName.requestFocus();
        this.mName.setSelection(0);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!mName.getText().toString().isEmpty() && !mFolder.getText().toString().isEmpty()) {
                                    File file = new File(mFolder.getText().toString(), mName.getText().toString());
                                    new SaveFileTask((EditorActivity) getActivity(), file.getPath(), getArguments().getString("fileText"), getArguments().getString("fileEncoding")).execute();
                                    PreferenceHelper.setWorkingFolder(getActivity(), file.getParent());
                                }
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }
                )
                .create();
    }

}
