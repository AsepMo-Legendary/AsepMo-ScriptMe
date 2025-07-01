package com.scriptme.story.engine.app.editor.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.editor.activity.EditorActivity;
import com.scriptme.story.engine.app.editor.dialogs.EncodingDialog;
import com.scriptme.story.engine.app.editor.dialogs.NumberPickerDialog;
import com.scriptme.story.engine.app.editor.dialogs.ThemeDialog;
import com.scriptme.story.engine.app.editor.util.ProCheckUtils;
import com.scriptme.story.engine.app.editor.util.ViewUtils;
import com.scriptme.story.engine.app.editor.dialogs.DialogHelper;

import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.ENCODING;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.FONT_SIZE;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.LINE_NUMERS;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.MONOSPACE;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.READ_ONLY;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.SYNTAX;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.TEXT_SUGGESTIONS;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.THEME_CHANGE;
import static com.scriptme.story.engine.app.editor.util.EventBusEvents.APreferenceValueWasChanged.Type.WRAP_CONTENT;

public class SettingsFragment extends Fragment implements NumberPickerDialog.INumberPickerDialog, EncodingDialog.DialogListener {

    // Editor Variables
    private boolean sLineNumbers;
    private boolean sColorSyntax;
    private boolean sWrapContent;
    private boolean sUseMonospace;
    private boolean sReadOnly;

    private boolean sLightTheme;
    private boolean sSuggestions;
    private boolean sAutoSave;
    private boolean sIgnoreBackButton;
    private boolean sSplitText;
    private boolean sErrorReports;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sUseMonospace = PreferenceHelper.getUseMonospace(getActivity());
        sColorSyntax = PreferenceHelper.getSyntaxHighlight(getActivity());
        sWrapContent = PreferenceHelper.getWrapContent(getActivity());
        sLineNumbers = PreferenceHelper.getLineNumbers(getActivity());
        sReadOnly = PreferenceHelper.getReadOnly(getActivity());

        sLightTheme = PreferenceHelper.getLightTheme(getActivity());
        sSuggestions = PreferenceHelper.getSuggestionActive(getActivity());
        sAutoSave = PreferenceHelper.getAutoSave(getActivity());
        sIgnoreBackButton = PreferenceHelper.getIgnoreBackButton(getActivity());
        sSplitText = PreferenceHelper.getSplitText(getActivity());
        sErrorReports = PreferenceHelper.getSendErrorReports(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Our custom layout
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        final SwitchCompat swLineNumbers, swSyntax, swWrapContent, swMonospace, swReadOnly;
        final SwitchCompat swLightTheme, swSuggestions, swAutoSave, swIgnoreBackButton, swSplitText, swErrorReports;
        
        swLineNumbers = (SwitchCompat) rootView.findViewById(R.id.switch_line_numbers);
        swSyntax = (SwitchCompat) rootView.findViewById(R.id.switch_syntax);
        swWrapContent = (SwitchCompat) rootView.findViewById(R.id.switch_wrap_content);
        swMonospace = (SwitchCompat) rootView.findViewById(R.id.switch_monospace);
        swReadOnly = (SwitchCompat) rootView.findViewById(R.id.switch_read_only);

        swLightTheme = (SwitchCompat) rootView.findViewById(R.id.switch_light_theme);
        swSuggestions = (SwitchCompat) rootView.findViewById(R.id.switch_suggestions_active);
        swAutoSave = (SwitchCompat) rootView.findViewById(R.id.switch_auto_save);
        swIgnoreBackButton = (SwitchCompat) rootView.findViewById(R.id.switch_ignore_backbutton);
        swSplitText = (SwitchCompat) rootView.findViewById(R.id.switch_page_system);
        swErrorReports = (SwitchCompat) rootView.findViewById(R.id.switch_send_error_reports);

        swLineNumbers.setChecked(sLineNumbers);
        swSyntax.setChecked(sColorSyntax);
        swWrapContent.setChecked(sWrapContent);
        swMonospace.setChecked(sUseMonospace);
        swReadOnly.setChecked(sReadOnly);

        swLightTheme.setChecked(sLightTheme);
        swSuggestions.setChecked(sSuggestions);
        swAutoSave.setChecked(sAutoSave);
        swIgnoreBackButton.setChecked(sIgnoreBackButton);
        swSplitText.setChecked(sSplitText);
        swErrorReports.setChecked(sErrorReports);

        TextView fontSizeView = (TextView) rootView.findViewById(R.id.drawer_button_font_size);
        TextView encodingView = (TextView) rootView.findViewById(R.id.drawer_button_encoding);
        TextView extraOptionsView = (TextView) rootView.findViewById(R.id.drawer_button_extra_options);
        TextView donateView = (TextView) rootView.findViewById(R.id.drawer_button_go_pro);

        if(ProCheckUtils.isPro(getActivity(), false))
            ViewUtils.setVisible(donateView, false);

        swLineNumbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setLineNumbers(getActivity(), isChecked);
                ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(LINE_NUMERS));
            }
        });

        swSyntax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sColorSyntax = isChecked;
                PreferenceHelper.setSyntaxHighlight(getActivity(), isChecked);
                ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(SYNTAX));

            }
        });

        swWrapContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setWrapContent(getActivity(), isChecked);
                ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(WRAP_CONTENT));
            }
        });

        swMonospace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sUseMonospace = isChecked;
                PreferenceHelper.setUseMonospace(getActivity(), isChecked);
                ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(MONOSPACE));

            }
        });

        swReadOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setReadOnly(getActivity(), isChecked);
                ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(READ_ONLY));
            }
        });

        fontSizeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fontMax = 36;
                int fontCurrent = //(int) (mEditor.getTextSize() / scaledDensity);
                        //fontMax / 2;
                        PreferenceHelper.getFontSize(getActivity());
                NumberPickerDialog dialogFrag = NumberPickerDialog.newInstance(NumberPickerDialog
                        .Actions
                        .FontSize, 1, fontCurrent, fontMax);
                dialogFrag.setTargetFragment(SettingsFragment.this, 0);
                dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");
            }
        });

        encodingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EncodingDialog dialogFrag = EncodingDialog.newInstance();
                dialogFrag.setTargetFragment(SettingsFragment.this, 0);
                dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");
            }
        });

        final View otherOptions = rootView.findViewById(R.id.other_options);       
        extraOptionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isVisible = otherOptions.getVisibility() == View.VISIBLE;
                ViewUtils.setVisible(otherOptions, !isVisible);
            }
        });

        donateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // DialogHelper.showDonateDialog(getActivity());
            }
        });

        swLightTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //PreferenceHelper.setLightTheme(getActivity(), isChecked);
                //((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(THEME_CHANGE));
                ThemeDialog dialogFrag = ThemeDialog.newInstance();
                dialogFrag.setTargetFragment(SettingsFragment.this, 0);
                dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");
         
            }
        });

        swSuggestions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setSuggestionsActive(getActivity(), isChecked);
                ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(TEXT_SUGGESTIONS));
            }
        });

        swAutoSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setAutoSave(getActivity(), isChecked);
            }
        });

        swIgnoreBackButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setIgnoreBackButton(getActivity(), isChecked);
            }
        });

        swSplitText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setSplitText(getActivity(), isChecked);
            }
        });

        swErrorReports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceHelper.setSendErrorReport(getActivity(), isChecked);
            }
        });

        return rootView;
    }

    @Override
    public void onNumberPickerDialogDismissed(NumberPickerDialog.Actions action, int value) {
        PreferenceHelper.setFontSize(getActivity(), value);
        ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(FONT_SIZE));

    }

    @Override
    public void onEncodingSelected(String result) {
        PreferenceHelper.setEncoding(getActivity(), result);
        ((EditorActivity) getActivity()).onEvent(new APreferenceValueWasChanged(ENCODING));
    }
}
