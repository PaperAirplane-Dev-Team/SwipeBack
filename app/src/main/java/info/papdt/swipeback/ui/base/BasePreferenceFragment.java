package info.papdt.swipeback.ui.base;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import android.support.v7.widget.Toolbar;

public abstract class BasePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener
{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(getPreferenceXml());
		onPreferenceLoaded();
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return false;
	}
	
	protected GlobalActivity getGlobalActivity() {
		return (GlobalActivity) getActivity();
	}

	protected Toolbar getToolbar() {
		return getGlobalActivity().getToolbar();
	}

	protected String getExtraPass() {
		return getGlobalActivity().getExtraPass();
	}

	protected void setTitle(String title) {
		getGlobalActivity().getSupportActionBar().setTitle(title);
	}

	protected void showHomeAsUp() {
		getGlobalActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	protected void $$(Preference preference) {
		preference.setOnPreferenceClickListener(this);
		preference.setOnPreferenceChangeListener(this);
	}
	
	protected abstract int getPreferenceXml();
	protected abstract void onPreferenceLoaded();
}
