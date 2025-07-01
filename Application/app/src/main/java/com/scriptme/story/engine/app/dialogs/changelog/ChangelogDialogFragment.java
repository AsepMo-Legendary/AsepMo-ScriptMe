package com.scriptme.story.engine.app.dialogs.changelog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.scriptme.story.R;

public class ChangelogDialogFragment extends DialogFragment {

    public static void showChangeLogDialog(FragmentManager fragmentManager) {
        ChangelogDialogFragment changelogDialogFragment = new ChangelogDialogFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("changelogdemo_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        changelogDialogFragment.show(ft, "changelogdemo_dialog");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ChangeLogListView chgList = (ChangeLogListView) layoutInflater.inflate(R.layout.demo_changelog_fragment_dialogstandard, null);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.engine_editor_changelog)
                .setView(chgList)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setPositiveButton(R.string.engine_editor_rate, new DialogInterface.OnClickListener() {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        try {
                            if (ChangeLog.FOR_AMAZON) {
                                String url = "amzn://apps/android?p=com.maskyn.fileeditor";
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.maskyn.fileeditor"))
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        } catch (Exception e) {
                        }
                    }
                })
                .create();

    }
}
