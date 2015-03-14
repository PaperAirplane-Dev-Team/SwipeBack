package info.papdt.swipeback.ui.app;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.adapter.ActivityAdapter;
import info.papdt.swipeback.ui.base.BaseListFragment;
import info.papdt.swipeback.ui.model.ActivityModel;

public class PerActivityFragment extends BaseListFragment
{
	private ArrayList<ActivityModel> mActivityList = new ArrayList<ActivityModel>();
	private BaseAdapter mAdapter;

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
			ai = pm.getPackageInfo(getExtraPass(), PackageManager.GET_ACTIVITIES).activities;
		} catch (Exception e) {
			throw new RuntimeException(e);
			//return;
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

}
