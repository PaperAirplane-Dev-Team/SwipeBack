package info.papdt.swipeback.ui.app;

import android.preference.Preference;
import android.preference.SwitchPreference;

import info.papdt.swipeback.R;
import info.papdt.swipeback.helper.Settings;
import info.papdt.swipeback.ui.base.BasePreferenceFragment;
import static info.papdt.swipeback.ui.utils.UiUtility.*;

public class SettingsFragment extends BasePreferenceFragment
{
	private Settings mSettings;
	
	private SwitchPreference mEnable;
	
	private String mPackageName, mClassName;

	@Override
	protected int getPreferenceXml() {
		return R.xml.pref;
	}

	@Override
	protected void onPreferenceLoaded() {
		initPackage();
		mSettings = Settings.getInstance(getActivity());
		
		// Obtain preferences
		mEnable = $(this, Settings.ENABLE);
		
		// Default values
		mEnable.setChecked(getBoolean(Settings.ENABLE, true));
		
		// Bind
		$$(mEnable);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mEnable) {
			putBoolean(Settings.ENABLE, Boolean.valueOf(newValue));
			return true;
		} else {
			return false;
		}
	}
	
	private void initPackage() {
		String[] str = getExtraPass().split(",");
		mPackageName = str[0];
		mClassName = str[1];
		
		if (str[2].startsWith("Global")) {
			str[2] = getString(R.string.global_short);
		}
		
		setTitle(str[2] + " - " + str[3]);
		showHomeAsUp();
	}
	
	private boolean getBoolean(String key, boolean defValue) {
		return mSettings.getBoolean(mPackageName, mClassName, key, defValue);
	}
	
	private void putBoolean(String key, boolean value) {
		mSettings.putBoolean(mPackageName, mClassName, key, value);
	}
}
