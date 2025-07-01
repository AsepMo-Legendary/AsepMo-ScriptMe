package com.scriptme.story.engine.app.settings.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.scriptme.story.engine.Engine;

public class ThemePreference {

  private static ThemePreference instance;
  private SharedPreferences SP;

  private ThemePreference() {
	SP = PreferenceManager.getDefaultSharedPreferences(Engine.getContext());
  }

  public static ThemePreference getInstance() {
	if (instance == null) {
	  synchronized (ThemePreference.class) {
		if (instance == null)
		  instance = new ThemePreference();
	  }
	}
	return instance;
  }

  public SharedPreferences.Editor getEditor() {
	return SP.edit();
  }

  public void putString(String key, String value) {
	getEditor().putString(key, value).commit();
  }

  public String getString(String key, String defValue) {
	return SP.getString(key, defValue);
  }

  public void putInt(String key, int value) {
	getEditor().putInt(key, value).commit();
  }

  public int getInt(String key, int defValue) {
	return SP.getInt(key, defValue);
  }

  public void putBoolean(String key, boolean value) {
	getEditor().putBoolean(key, value).commit();
  }

  public boolean getBoolean(String key, boolean defValue) {
	return SP.getBoolean(key, defValue);
  }


  public void remove(String key) {
	getEditor().remove(key).commit();
  }

  public void clearPreferences() {
	getEditor().clear().commit();
  }
}
