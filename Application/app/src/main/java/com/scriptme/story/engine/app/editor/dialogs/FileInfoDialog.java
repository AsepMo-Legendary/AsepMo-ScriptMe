package com.scriptme.story.engine.app.editor.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Date;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.adapter.AdapterTwoItem;

// ...
public class FileInfoDialog extends DialogFragment {

    public static FileInfoDialog newInstance(String filePath) {
        final FileInfoDialog f = new FileInfoDialog();
        final Bundle args = new Bundle();
        args.putString("filePath", filePath);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = new DialogHelper.Builder(getActivity())
                .setTitle(R.string.engine_editor_info)
                .setView(R.layout.dialog_fragment_file_info)
                .createSkeletonView();
        //final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_file_info, null);

        ListView list = (ListView) view.findViewById(android.R.id.list);

        File file = new File(getArguments().getString("filePath"));

        // Get the last modification information.
        Long lastModified = file.lastModified();

        // Create a new date object and pass last modified information
        // to the date object.
        Date date = new Date(lastModified);

        String[] lines1 = {
                getString(R.string.engine_editor_name),
            getString(R.string.engine_editor_folder),
            getString(R.string.engine_editor_size),
            getString(R.string.engine_editor_modification_date)
        };
        String[] lines2 = {
                file.getName(),
                file.getParent(),
                FileUtils.byteCountToDisplaySize(file.length()),
                date.toString()
        };

        list.setAdapter(new AdapterTwoItem(getActivity(), lines1, lines2));


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                )
                .create();
    }
}
