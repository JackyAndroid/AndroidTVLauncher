package com.jacky.launcher.db;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * @author Droid
 */
public class SharedPreferencesUtil {
	
	private static final String SHAREDPREFERENCE_NAME = "sharedpreferences_droid";
	private static SharedPreferencesUtil mInstance;
	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor mEditor;
	public synchronized static SharedPreferencesUtil getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SharedPreferencesUtil(context);
		}
		return mInstance;
	}

	private SharedPreferencesUtil(Context context) {
		mSharedPreferences = context.getSharedPreferences(
				SHAREDPREFERENCE_NAME, Context.MODE_WORLD_READABLE);
		mEditor = mSharedPreferences.edit();
	}

	public synchronized boolean putString(String key, String value) {
		mEditor.putString(key, value);
		return mEditor.commit();
	}

	public synchronized boolean putInt(String key, int value) {
		mEditor.putInt(key, value);
		return mEditor.commit();
	}

	public synchronized boolean putLong(String key, long value) {
		mEditor.putLong(key, value);
		return mEditor.commit();
	}

	public synchronized boolean putFloat(String key, float value) {
		mEditor.putFloat(key, value);
		return mEditor.commit();
	}

	public synchronized boolean putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
		return mEditor.commit();
	}

	public synchronized boolean putStringSet(String key, Set<String> value) {
		mEditor.putStringSet(key, value);
		return mEditor.commit();
	}

	public String getString(String key, String value) {
		return mSharedPreferences.getString(key, value);
	}

	public int getInt(String key, int value) {
		return mSharedPreferences.getInt(key, value);
	}

	public long getLong(String key, long value) {
		return mSharedPreferences.getLong(key, value);
	}

	public float getFloat(String key, float value) {
		return mSharedPreferences.getFloat(key, value);
	}

	public boolean getBoolean(String key, boolean value) {
		return mSharedPreferences.getBoolean(key, value);
	}

	public Set<String> getStringSet(String key, Set<String> value) {
		return mSharedPreferences.getStringSet(key, value);
	}

	public boolean remove(String key) {
		mEditor.remove(key);
		return mEditor.commit();
	}
}
