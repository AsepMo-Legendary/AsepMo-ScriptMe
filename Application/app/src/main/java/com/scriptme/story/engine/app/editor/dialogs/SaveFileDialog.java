package com.scriptme.story.engine.app.editor.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.activity.EditorActivity;
import com.scriptme.story.engine.app.editor.task.SaveFileTask;

public class SaveFileDialog extends DialogFragment {

    public static SaveFileDialog newInstance(String filePath, String text, String encoding) {
        return newInstance(filePath, text, encoding, false, "");
    }

    public static SaveFileDialog newInstance(String filePath, String text, String encoding, boolean openNewFileAfter, String pathOfNewFile) {
        SaveFileDialog frag = new SaveFileDialog();
        Bundle args = new Bundle();
        args.putString("filePath", filePath);
        args.putString("text", text);
        args.putString("encoding", encoding);
        args.putBoolean("openNewFileAfter", openNewFileAfter);
        args.putString("pathOfNewFile", pathOfNewFile);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String filePath = getArguments().getString("filePath");
        final String text = getArguments().getString("text");
        final String encoding = getArguments().getString("encoding");
        final String fileName = FilenameUtils.getName(filePath);
        final File file = new File(filePath);

        View view = new DialogHelper.Builder(getActivity())
                .setIcon(getResources().getDrawable(R.drawable.ic_action_save_white))
                .setTitle(R.string.engine_editor_save)
                .setMessage(String.format(getString(R.string.engine_editor_save_changes), fileName))
                .createCommonView();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.engine_editor_save,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!fileName.isEmpty())
                                    new SaveFileTask((EditorActivity) getActivity(), filePath, text,
                                            encoding).execute();
                                else {
                                    NewFileDetailsDialog dialogFrag =
                                            NewFileDetailsDialog.newInstance(text,
                                                    encoding);
                                    dialogFrag.show(getFragmentManager().beginTransaction(),
                                            "dialog");
                                }
                            }
                        }
                )
                .setNeutralButton(android.R.string.cancel, null)
                .setNegativeButton(R.string.engine_editor_no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ISaveDialog target = (ISaveDialog) getTargetFragment();
                                if (target == null) {
                                    target = (ISaveDialog) getActivity();
                                }
                                target.userDoesntWantToSave(
                                        getArguments().getBoolean("openNewFileAfter"),
                                        getArguments().getString("pathOfNewFile")
                                );
                            }
                        }
                )
                .create();
    }

    public interface ISaveDialog {
        void userDoesntWantToSave(boolean openNewFile, String pathOfNewFile);
    }
}
