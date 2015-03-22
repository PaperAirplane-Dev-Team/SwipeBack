package info.papdt.swipeback.ui.app;

import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.base.BasePreferenceFragment;
import info.papdt.swipeback.ui.preference.DiscreteSeekBarPreference;
import static info.papdt.swipeback.ui.utils.UiUtility.*;

public class AboutFragment extends BasePreferenceFragment
{
	private static final String VERSION = "version";
	private static final String MADE_BY = "made_by";
	private static final String SOURCE_CODE = "source_code";
	private static final String LICENSE = "license";
	private static final String DONATION = "donation";
	
	private Preference mVersion, mMadeBy, mSourceCode, mLicense;
	private DiscreteSeekBarPreference mDonation;

	@Override
	protected void onPreferenceLoaded() {
		setTitle(getString(R.string.about));
		showHomeAsUp();
		
		// Obtain preferences
		mVersion = $(this, VERSION);
		mMadeBy = $(this, MADE_BY);
		mSourceCode = $(this, SOURCE_CODE);
		mLicense = $(this, LICENSE);
		mDonation = $(this, DONATION);
		
		// Set values
		String ver = "?";
		try {
			ver = getActivity().getPackageManager().getPackageInfo("info.papdt.swipeback", 0).versionName;
		} catch (Exception e) {
			
		}
		mVersion.setSummary(ver);
		
		$$(mVersion, mMadeBy, mSourceCode, mLicense, mDonation);
	}

	@Override
	protected int getPreferenceXml() {
		return R.xml.about;
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		if (pref == mMadeBy) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("https://github.com/PaperAirplane-Dev-Team"));
			startActivity(i);
			return true;
		} else if (pref == mSourceCode) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("https://github.com/PaperAirplane-Dev-Team/SwipeBack"));
			startActivity(i);
			return true;
		} else if (pref == mLicense) {
			// TODO
			return true;
		} else {
			return super.onPreferenceClick(pref);
		}
	}
}
