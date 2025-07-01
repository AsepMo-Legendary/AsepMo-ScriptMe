package com.scriptme.story.engine.app.editor.dialogs;

import android.support.v7.widget.CardView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.scriptme.story.engine.app.settings.theme.Theme.AMOLED_THEME;
import static com.scriptme.story.engine.app.settings.theme.Theme.DARK_THEME;
import static com.scriptme.story.engine.app.settings.theme.Theme.LIGHT_THEME;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.app.settings.theme.ThemePreference;

public class ThemeDialog extends DialogFragment {

    private Theme theme;
    private ThemePreference mSharedPreference;
    private OnThemeDialogListener mOnThemeDialogListener;
    
    public ThemeDialog() {
        theme = new Theme();
    }

    public static ThemeDialog newInstance() {
        return new ThemeDialog();
    }

    public void setOnThemeDialogListener(OnThemeDialogListener mOnThemeDialogListener){
        this.mOnThemeDialogListener = mOnThemeDialogListener;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();
        mSharedPreference = ThemePreference.getInstance();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final LayoutInflater dialogInflater = LayoutInflater.from(dialogBuilder.getContext());

        final View dialogLayout = dialogInflater.inflate(R.layout.dialog_theme, null, false);

        final TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.basic_theme_title);
        final CardView dialogCardView = (CardView) dialogLayout.findViewById(R.id.basic_theme_card);

        final ImageView whiteSelect = (ImageView) dialogLayout.findViewById(R.id.white_basic_theme_select);
        final ImageView darkSelect = (ImageView) dialogLayout.findViewById(R.id.dark_basic_theme_select);
        final ImageView darkAmoledSelect = (ImageView) dialogLayout.findViewById(R.id.dark_amoled_basic_theme_select);

        switch (theme.getBaseTheme()) {
            case LIGHT_THEME:
                whiteSelect.setVisibility(View.VISIBLE);
                darkSelect.setVisibility(View.GONE);
                darkAmoledSelect.setVisibility(View.GONE);
                break;
            case DARK_THEME:
                whiteSelect.setVisibility(View.GONE);
                darkSelect.setVisibility(View.VISIBLE);
                darkAmoledSelect.setVisibility(View.GONE);
                break;
            case AMOLED_THEME:
                whiteSelect.setVisibility(View.GONE);
                darkSelect.setVisibility(View.GONE);
                darkAmoledSelect.setVisibility(View.VISIBLE);
                break;
        }

        /** SET OBJ THEME **/
        dialogTitle.setBackgroundColor(theme.getPrimaryColor());
        dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());
        dialogLayout.findViewById(R.id.ll_white_basic_theme).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whiteSelect.setVisibility(View.VISIBLE);
                    darkSelect.setVisibility(View.GONE);
                    darkAmoledSelect.setVisibility(View.GONE);
                    theme.setBaseTheme(LIGHT_THEME, false);
                    dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());              
                }
            });
        dialogLayout.findViewById(R.id.ll_dark_basic_theme).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whiteSelect.setVisibility(View.GONE);
                    darkSelect.setVisibility(View.VISIBLE);
                    darkAmoledSelect.setVisibility(View.GONE);
                    theme.setBaseTheme(DARK_THEME, false);
                    dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());                          
                }
            });
        dialogLayout.findViewById(R.id.ll_dark_amoled_basic_theme).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whiteSelect.setVisibility(View.GONE);
                    darkSelect.setVisibility(View.GONE);
                    darkAmoledSelect.setVisibility(View.VISIBLE);
                    theme.setBaseTheme(AMOLED_THEME, false);
                    dialogCardView.setCardBackgroundColor(theme.getCardBackgroundColor());              
                }
            });
        dialogBuilder.setView(dialogLayout);
        dialogBuilder.setPositiveButton(getString(android.R.string.ok).toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    mSharedPreference.putInt(getString(R.string.engine_settings_base_theme), theme.getBaseTheme());     
                    mSharedPreference.putBoolean("recreate", true);
                    if(mOnThemeDialogListener != null){
                        mOnThemeDialogListener.onTheme(theme.getAppTheme(), theme.isRecreate());
                    }             
                }
            });
        dialogBuilder.setNegativeButton(getString(android.R.string.cancel).toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    theme.setBaseTheme(Theme.getApplicationTheme(), false);
                    mSharedPreference.putBoolean("recreate", true);

                }
            });
        dialogBuilder.setView(dialogLayout);        
        return dialogBuilder.create();
    }

    public interface OnThemeDialogListener {
        void onTheme(int theme, boolean isRecreate);
    }
}
