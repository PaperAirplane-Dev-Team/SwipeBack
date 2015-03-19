package info.papdt.swipeback.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import de.robv.android.xposed.XSharedPreferences;

import static info.papdt.swipeback.BuildConfig.DEBUG;

public abstract class Settings
{
	private static final String TAG = Settings.class.getSimpleName();
	
	public static final String PREF = "pref";
	public static final String GLOBAL = "global";
	public static final String ENABLE = "enable";
	public static final String EDGE = "edge";
	public static final String SENSITIVITY = "sensitivity";
	public static final String LOLLIPOP_HACK = "lollipop_hack";
	
	private static XSettings sXSettings;
	private static AppSettings sAppSettings;
	
	public static Settings getInstance(Context context) {
		if (context != null) {
			if (sAppSettings == null) {
				sAppSettings = new AppSettings(context.getApplicationContext());
			}
			
			return sAppSettings;
		} else {
			if (sXSettings == null) {
				sXSettings = new XSettings();
			}
			
			return sXSettings;
		}
	}
	
	public void reload() {
		
	}
	
	public abstract boolean getBoolean(String packageName, String className, String key, boolean defValue);
	public abstract int getInt(String packageName, String className, String key, int defValue);
	public abstract void putBoolean(String packageName, String className, String key, boolean value);
	public abstract void putInt(String packageName, String className, String key, int value);
	public abstract void remove(String packageName, String className, String key);
	protected abstract boolean contains(String key);
	
	protected String fallback(String packageName, String className, String key) {
		String keyStr = buildKey(packageName, className, key);
		if (contains(keyStr)) {
			return keyStr;
		}
		
		if (!className.equals(GLOBAL)) {
			keyStr = buildKey(packageName, GLOBAL, key);
			
			if (DEBUG) {
				Log.d(TAG, "falling back to " + keyStr);
			}
			
			if (contains(keyStr)) {
				return keyStr;
			}
		}
		
		if (!packageName.equals(GLOBAL)) {
			keyStr = buildKey(GLOBAL, GLOBAL, key);
			
			if (DEBUG) {
				Log.d(TAG, "falling back to " + keyStr);
			}
		}
		
		return keyStr;
	}
	
	private static String buildKey(String packageName, String className, String key) {
		return packageName + ":" + className + ":" + key;
	}
	
	private static class XSettings extends Settings {
		private XSharedPreferences mPref;
		
		XSettings() {
			mPref = new XSharedPreferences("info.papdt.swipeback", PREF);
			mPref.makeWorldReadable();
		}

		@Override
		public void reload() {
			mPref.reload();
		}

		@Override
		public void remove(String packageName, String className, String key) {
			throwException();
		}

		@Override
		protected boolean contains(String key) {
			return mPref.contains(key);
		}

		@Override
		public boolean getBoolean(String packageName, String className, String key, boolean defValue) {
			return mPref.getBoolean(fallback(packageName, className, key), defValue);
		}

		@Override
		public int getInt(String packageName, String className, String key, int defValue) {
			return mPref.getInt(fallback(packageName, className, key), defValue);
		}

		@Override
		public void putBoolean(String packageName, String className, String key, boolean value) {
			throwException();
		}

		@Override
		public void putInt(String packageName, String className, String key, int value) {
			throwException();
		}
		
		private void throwException() {
			throw new RuntimeException(new IllegalAccessException("Cannot access editor with XSettings"));
		}
	}
	
	private static class AppSettings extends Settings {
		private SharedPreferences mPref;
		
		AppSettings(Context context) {
			mPref = context.getSharedPreferences(PREF, Context.MODE_WORLD_READABLE);
		}

		@Override
		public void remove(String packageName, String className, String key) {
			mPref.edit().remove(buildKey(packageName, className, key)).commit();
		}

		@Override
		protected boolean contains(String key) {
			return mPref.contains(key);
		}

		@Override
		public boolean getBoolean(String packageName, String className, String key, boolean defValue) {
			return mPref.getBoolean(fallback(packageName, className, key), defValue);
		}

		@Override
		public int getInt(String packageName, String className, String key, int defValue) {
			return mPref.getInt(fallback(packageName, className, key), defValue);
		}

		@Override
		public void putBoolean(String packageName, String className, String key, boolean value) {
			mPref.edit().putBoolean(buildKey(packageName, className, key), value).commit();
		}

		@Override
		public void putInt(String packageName, String className, String key, int value) {
			mPref.edit().putInt(buildKey(packageName, className, key), value).commit();
		}
	}
}
