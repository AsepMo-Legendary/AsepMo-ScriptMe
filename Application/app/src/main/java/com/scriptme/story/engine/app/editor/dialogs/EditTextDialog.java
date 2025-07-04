package com.scriptme.story.engine.app.editor.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.scriptme.story.R;

// ...
public class EditTextDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;

    public static EditTextDialog newInstance(final Actions action) {
        return EditTextDialog.newInstance(action, "");
    }

    public static EditTextDialog newInstance(final Actions action, final String hint) {
        final EditTextDialog f = new EditTextDialog();
        final Bundle args = new Bundle();
        args.putSerializable("action", action);
        args.putString("hint", hint);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Actions action = (Actions) getArguments().getSerializable("action");
        final String title;
        switch (action) {
            case NewFile:
                title = getString(R.string.engine_editor_file);
                break;
            case NewFolder:
                title = getString(R.string.engine_editor_folder);
                break;
            default:
                title = null;
                break;
        }

        View view = new DialogHelper.Builder(getActivity())
                .setTitle(title)
                .setView(R.layout.dialog_fragment_edittext)
                .createSkeletonView();
        this.mEditText = (EditText) view.findViewById(android.R.id.edit);
        this.mEditText.setHint(R.string.engine_editor_name);

        // Show soft keyboard automatically
        this.mEditText.setText(getArguments().getString("hint"));
        this.mEditText.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        this.mEditText.setOnEditorActionListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                returnData();
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

    void returnData() {
        EditDialogListener target = (EditDialogListener) getTargetFragment();
        if (target == null) {
            target = (EditDialogListener) getActivity();
        }
        target.onFinishEditDialog(this.mEditText.getText().toString(), getArguments().getString("hint"),
                (Actions) getArguments().getSerializable("action"));
        this.dismiss();
    }

    @Override
    public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            returnData();
            return true;
        }
        return false;
    }

    public enum Actions {
        NewFile, NewFolder
    }

    public interface EditDialogListener {
        void onFinishEditDialog(String result, String hint, Actions action);
    }
}
