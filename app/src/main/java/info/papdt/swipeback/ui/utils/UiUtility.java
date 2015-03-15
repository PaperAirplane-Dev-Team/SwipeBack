package info.papdt.swipeback.ui.utils;

import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

public class UiUtility
{
	public static <T extends View> T $(View v, int id) {
		return (T) v.findViewById(id);
	}
	
	public static <T extends View> T $(Activity activity, int id) {
		return (T) activity.findViewById(id);
	}
	
	public static <T extends Preference> T $(PreferenceFragment preference, String key) {
		return (T) preference.findPreference(key);
	}
	
	public static <T> T $(Object obj) {
		return (T) obj;
	}
}
