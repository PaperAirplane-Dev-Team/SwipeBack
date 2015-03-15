package info.papdt.swipeback.ui.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.adapter.ActivityAdapter;
import info.papdt.swipeback.ui.base.BaseListFragment;
import info.papdt.swipeback.ui.base.GlobalActivity;
import info.papdt.swipeback.ui.model.ActivityModel;

public class PerActivityFragment extends BaseListFragment
{
	private ArrayList<ActivityModel> mActivityList = new ArrayList<ActivityModel>();
	private BaseAdapter mAdapter;
	private String mTitle = "";

	@Override
	protected BaseAdapter buildAdapter() {
		mAdapter = new ActivityAdapter(getActivity(), mActivityList);
		return mAdapter;
	}

	@Override
	protected void loadData() {
		mActivityList.clear();
		PackageManager pm = getActivity().getPackageManager();
		
		ActivityInfo[] ai;
		
		try {
			PackageInfo pkg = pm.getPackageInfo(getExtraPass(), PackageManager.GET_ACTIVITIES);
			ai = pkg.activities;
			mTitle = pm.getApplicationLabel(pkg.applicationInfo).toString();
		} catch (Exception e) {
			ai = new ActivityInfo[0];
			mTitle = getString(R.string.global_short);
		}
		
		// Add the default one
		ActivityModel global = new ActivityModel();
		global.className = "global";
		global.title = getString(R.string.global);
		mActivityList.add(global);
		
		for (ActivityInfo info : ai) {
			ActivityModel activity = new ActivityModel();
			activity.className = info.name;
			activity.title = info.loadLabel(pm).toString();
			mActivityList.add(activity);
		}
	}

	@Override
	protected void onDataLoaded() {
		if (!mTitle.equals("")) {
			showHomeAsUp();
			setTitle(mTitle + " - " + getString(R.string.app_name));
		}
	}

	@Override
	protected void onItemClick(int pos) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.setClass(getActivity(), GlobalActivity.class);
		i.putExtra("fragment", "settings");
		
		ActivityModel activity = mActivityList.get(pos);
		
		i.putExtra("pass", getExtraPass() + "," + activity.className + "," + activity.title + "," + mTitle);
		startActivity(i);
	}

}
