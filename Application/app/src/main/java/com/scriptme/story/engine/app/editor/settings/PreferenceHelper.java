package com.scriptme.story.engine.app.editor.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public final class PreferenceHelper {

    public static final String SD_CARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    private PreferenceHelper() {
    }

    // Getter Methods

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPrefs(context).edit();
    }

    public static boolean getUseMonospace(Context context) {
        return getPrefs(context).getBoolean("use_monospace", true);
    }

    public static boolean getLineNumbers(Context context) {
        return getPrefs(context).getBoolean("editor_line_numbers", true);
    }

    public static boolean getSyntaxHighlight(Context context) {
        return getPrefs(context).getBoolean("editor_syntax_highlight", true);
    }

    public static boolean getWrapContent(Context context) {
        return getPrefs(context).getBoolean("editor_wrap_content", true);
    }

    public static boolean getLightTheme(Context context) {
        return getPrefs(context).getBoolean("light_theme", false);
    }

    public static boolean getSuggestionActive(Context context) {
        return getPrefs(context).getBoolean("suggestion_active", false);
    }

    public static boolean getAutoEncoding(Context context) {
        return getPrefs(context).getBoolean("autoencoding", true);
    }

    public static boolean getSendErrorReports(Context context) {
        return getPrefs(context).getBoolean("send_error_reports", true);
    }

    public static String getEncoding(Context context) {
        return getPrefs(context).getString("editor_encoding", "UTF-8");
    }

    public static int getFontSize(Context context) {
        return getPrefs(context).getInt("font_size", 16);
    }

    public static String getWorkingFolder(Context context) {
        return getPrefs(context).getString("working_folder", SD_CARD_ROOT);
    }

    public static String[] getSavedPaths(Context context) {
        return getPrefs(context).getString("savedPaths", "").split(",");
    }

    public static boolean getPageSystemButtonsPopupShown(Context context) {
        return getPrefs(context).getBoolean("page_system_button_popup_shown", false);
    }

    public static boolean getAutoSave(Context context) {
        return getPrefs(context).getBoolean("auto_save", false);
    }

    public static boolean getReadOnly(Context context) {
        return getPrefs(context).getBoolean("read_only", false);
    }

    public static boolean getIgnoreBackButton(Context context) {
        return getPrefs(context).getBoolean("ignore_back_button", false);
    }

    public static boolean getSplitText(Context context) {
        return getPrefs(context).getBoolean("page_system_active", true);
    }

    public static boolean hasDonated(Context context) {
        return getPrefs(context).getBoolean("has_donated", false);
    }
    // Setter methods

    public static void setUseMonospace(Context context, boolean value) {
        getEditor(context).putBoolean("use_monospace", value).commit();
    }

    public static void setLineNumbers(Context context, boolean value) {
        getEditor(context).putBoolean("editor_line_numbers", value).commit();
    }

    public static void setSyntaxHighlight(Context context, boolean value) {
        getEditor(context).putBoolean("editor_syntax_highlight", value).commit();
    }

    public static void setWrapContent(Context context, boolean value) {
        getEditor(context).putBoolean("editor_wrap_content", value).commit();
    }

    public static void setAutoencoding(Context context, boolean value) {
        getEditor(context).putBoolean("autoencoding", value).commit();
    }

    public static void setFontSize(Context context, int value) {
        getEditor(context).putInt("font_size", value).commit();
    }

    public static void setWorkingFolder(Context context, String value) {
        getEditor(context).putString("working_folder", value).commit();
    }

    public static void setSavedPaths(Context context, StringBuilder stringBuilder) {
        getEditor(context).putString("savedPaths", stringBuilder.toString()).commit();
    }

    public static void setPageSystemButtonsPopupShown(Context context, boolean value) {
        getEditor(context).putBoolean("page_system_button_popup_shown", value).commit();
    }

    public static void setReadOnly(Context context, boolean value) {
        getEditor(context).putBoolean("read_only", value).commit();
    }

    public static void setHasDonated(Context context, boolean value) {
        getEditor(context).putBoolean("has_donated", value).commit();
    }

    public static void setLightTheme(Context context, boolean value) {
        getEditor(context).putBoolean("light_theme", value).commit();
    }

    public static void setSuggestionsActive(Context context, boolean value) {
        getEditor(context).putBoolean("suggestion_active", value).commit();
    }

    public static void setAutoSave(Context context, boolean value) {
        getEditor(context).putBoolean("auto_save", value).commit();
    }

    public static void setIgnoreBackButton(Context context, boolean value) {
        getEditor(context).putBoolean("ignore_back_button", value).commit();
    }

    public static void setSplitText(Context context, boolean value) {
        getEditor(context).putBoolean("page_system_active", value).commit();
    }

    public static void setSendErrorReport(Context context, boolean value) {
        getEditor(context).putBoolean("ignore_back_button", value).commit();
    }

    public static void setEncoding(Context context, String value) {
        getEditor(context).putString("editor_encoding", value).commit();
    }
}
