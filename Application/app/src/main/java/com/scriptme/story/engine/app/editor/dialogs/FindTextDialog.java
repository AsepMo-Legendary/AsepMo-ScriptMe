package com.scriptme.story.engine.app.editor.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.text.SearchResult;

// ...
public class FindTextDialog extends DialogFragment {

    private EditText textToFind, textToReplace;
    private CheckBox regexCheck, replaceCheck, matchCaseCheck;

    public static FindTextDialog newInstance(String allText) {
        final FindTextDialog f = new FindTextDialog();
        final Bundle args = new Bundle();
        args.putString("allText", allText);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_find_text, null);

        View view = new DialogHelper.Builder(getActivity())
                .setTitle(R.string.engine_editor_find)
                .setView(R.layout.dialog_fragment_find_text)
                .createSkeletonView();

        this.textToFind = (EditText) view.findViewById(R.id.text_to_find);
        this.textToReplace = (EditText) view.findViewById(R.id.text_to_replace);
        this.regexCheck = (CheckBox) view.findViewById(R.id.regex_check);
        this.replaceCheck = (CheckBox) view.findViewById(R.id.replace_check);
        this.matchCaseCheck = (CheckBox) view.findViewById(R.id.match_case_check);

        replaceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textToReplace.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
            .setPositiveButton(R.string.engine_editor_find,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setText(getString(R.string.engine_editor_find));
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnData();
                }
            });

            Button negativeButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setText(getString(android.R.string.cancel));
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    void returnData() {
        if (textToFind.getText().toString().isEmpty()) {
            this.dismiss();
        } else {
            // we disable the okButton while we search
            new SearchTask().execute();
        }
    }

    public interface SearchDialogInterface {
        void onSearchDone(SearchResult searchResult);
    }

    private class SearchTask extends AsyncTask<Void, Void, Void> {

        LinkedList<Integer> foundIndex;
        boolean foundSomething;

        @Override
        protected Void doInBackground(Void... params) {
            String allText = getArguments().getString("allText");
            String whatToSearch = textToFind.getText().toString();
            boolean caseSensitive = matchCaseCheck.isChecked();
            boolean isRegex = regexCheck.isChecked();
            foundIndex = new LinkedList<>();
            Matcher matcher = null;
            foundSomething = false;

            if (isRegex) {
                try {
                    if (caseSensitive)
                        matcher = Pattern.compile(whatToSearch, Pattern.MULTILINE).matcher(allText);
                    else
                        matcher = Pattern.compile(whatToSearch, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(allText);
                } catch (Exception e) {
                    isRegex = false;
                }
            }

            if (isRegex) {
                while (matcher.find()) {
                    foundSomething = true;

                    foundIndex.add(matcher.start());
                }
            } else {
                if (caseSensitive == false) { // by default is case sensitive
                    whatToSearch = whatToSearch.toLowerCase();
                    allText = allText.toLowerCase();
                }
                int index = allText.indexOf(whatToSearch);
                while (index >= 0) {
                    foundSomething = true;

                    foundIndex.add(index);

                    index = allText.indexOf(whatToSearch, index + 1);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (foundSomething) {
                // the class that called this Dialog should implement the SearchDialogIterface
                SearchDialogInterface searchDialogInterface;
                searchDialogInterface = ((SearchDialogInterface) getTargetFragment());
                if (searchDialogInterface == null)
                    searchDialogInterface = ((SearchDialogInterface) getActivity());

                // if who called this has not implemented the interface we return nothing
                if (searchDialogInterface == null)
                    return;
                    // else we return positions and other things
                else {
                    SearchResult searchResult = new SearchResult(foundIndex, textToFind.length(), replaceCheck.isChecked(), textToReplace.getText().toString());
                    searchDialogInterface.onSearchDone(searchResult);
                }
            } else {

            }
            Toast.makeText(getActivity(), String.format(getString(R.string.engine_editor_occurrences_found), foundIndex.size()), Toast.LENGTH_SHORT).show();
            // dismiss the dialog
            FindTextDialog.this.dismiss();
        }
    }
}
