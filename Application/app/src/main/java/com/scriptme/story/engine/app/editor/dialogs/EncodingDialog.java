package com.scriptme.story.engine.app.editor.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import org.mozilla.universalchardet.Constants;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.settings.PreferenceHelper;

public class EncodingDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private final String[] encodings = new String[]{
            Constants.CHARSET_BIG5,
            Constants.CHARSET_EUC_JP,
            Constants.CHARSET_EUC_KR,
            Constants.CHARSET_EUC_TW,
            Constants.CHARSET_GB18030,
            "GB2312",
            Constants.CHARSET_IBM855,
            Constants.CHARSET_IBM866,
            Constants.CHARSET_ISO_2022_CN,
            Constants.CHARSET_ISO_2022_JP,
            Constants.CHARSET_ISO_2022_KR,
            Constants.CHARSET_ISO_8859_5,
            Constants.CHARSET_ISO_8859_7,
            Constants.CHARSET_ISO_8859_8,
            Constants.CHARSET_KOI8_R,
            Constants.CHARSET_MACCYRILLIC,
            Constants.CHARSET_SHIFT_JIS,
            Constants.CHARSET_UTF_16BE,
            Constants.CHARSET_UTF_16LE,
            Constants.CHARSET_UTF_32BE,
            Constants.CHARSET_UTF_32LE,
            Constants.CHARSET_UTF_8,
            Constants.CHARSET_WINDOWS_1251,
            Constants.CHARSET_WINDOWS_1252,
            Constants.CHARSET_WINDOWS_1253,
            Constants.CHARSET_WINDOWS_1255
    };
    private ListView list;

    public static EncodingDialog newInstance() {
        final EncodingDialog f = new EncodingDialog();
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_encoding_list, null);
        list = (ListView) view.findViewById(android.R.id.list);
        SwitchCompat autoencoding = (SwitchCompat) view.findViewById(android.R.id.checkbox);
        autoencoding.setChecked(PreferenceHelper.getAutoEncoding(getActivity()));

        autoencoding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setAutoencoding(getActivity(), isChecked);
            }
        });

        list.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_single_choice, encodings));
        list.setOnItemClickListener(this);

        String currentEncoding = PreferenceHelper.getEncoding(getActivity());

        for (int i = 0; i < encodings.length; i++) {
            if (currentEncoding.equals(encodings[i])) {
                list.setItemChecked(i, true);
            }

        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogListener target = (DialogListener) getTargetFragment();
        if (target == null) {
            target = (DialogListener) getActivity();
        }
        target.onEncodingSelected(encodings[position]);
        this.dismiss();
    }

    public interface DialogListener {
        void onEncodingSelected(String result);
    }
}
