package com.scriptme.story.engine.app.editor.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.scriptme.story.R;

// ...
public class NumberPickerDialog extends DialogFragment {

    private NumberPicker mSeekBar;

    public static NumberPickerDialog newInstance(final Actions action) {
        return NumberPickerDialog.newInstance(action, 0, 50, 100);
    }

    public static NumberPickerDialog newInstance(final Actions action, final int min, final int current, final int max) {
        final NumberPickerDialog f = new NumberPickerDialog();
        final Bundle args = new Bundle();
        args.putSerializable("action", action);
        args.putInt("min", min);
        args.putInt("current", current);
        args.putInt("max", max);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Actions action = (Actions) getArguments().getSerializable("action");
        int title;
        switch (action){
            case FontSize:
                title = R.string.engine_editor_font_size;
                break;
            case SelectPage:
                title = R.string.engine_editor_goto_page;
                break;
            case GoToLine:
                title = R.string.engine_editor_goto_line;
                break;
            default:
                title = R.string.engine_editor;
                break;
        }

        View view = new DialogHelper.Builder(getActivity())
                .setTitle(title)
                .setView(R.layout.dialog_fragment_seekbar)
                .createSkeletonView();

        this.mSeekBar = (NumberPicker) view.findViewById(android.R.id.input);
        this.mSeekBar.setMaxValue(getArguments().getInt("max"));
        this.mSeekBar.setMinValue(getArguments().getInt("min"));
        this.mSeekBar.setValue(getArguments().getInt("current"));
        return new AlertDialog.Builder(getActivity())
                //.setTitle(title)
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
        INumberPickerDialog target = (INumberPickerDialog) getTargetFragment();
        if (target == null) {
            target = (INumberPickerDialog) getActivity();
        }
        target.onNumberPickerDialogDismissed(
                (Actions) getArguments().getSerializable("action"),
                mSeekBar.getValue()
        );
        this.dismiss();
    }

    public enum Actions {
        FontSize, SelectPage, GoToLine
    }

    public interface INumberPickerDialog {
        void onNumberPickerDialogDismissed(Actions action, int value);
    }
}
