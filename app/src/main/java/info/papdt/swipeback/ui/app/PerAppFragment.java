package info.papdt.swipeback.ui.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.BaseAdapter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.adapter.AppAdapter;
import info.papdt.swipeback.ui.base.BaseListFragment;
import info.papdt.swipeback.ui.base.GlobalActivity;
import info.papdt.swipeback.ui.model.AppModel;

public class PerAppFragment extends BaseListFragment
{
	private AppAdapter mAdapter;
	private ArrayList<AppModel> mAppList = new ArrayList<AppModel>();

	@Override
	protected BaseAdapter buildAdapter() {
		mAdapter = new AppAdapter(getActivity(), mAppList);
		return mAdapter;
	}

	@Override
	protected void loadData() {
		mAppList.clear();
		PackageManager pm = getActivity().getPackageManager();
		List<ApplicationInfo> la = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		
		for (ApplicationInfo info : la) {
			AppModel app = new AppModel();
			app.packageName = info.packageName;
			app.title = pm.getApplicationLabel(info).toString();
			app.icon = pm.getApplicationIcon(info);
			mAppList.add(app);
		}
		
		Collections.sort(mAppList, new Comparator<AppModel>() {
			@Override
			public int compare(AppModel p1, AppModel p2) {
				return Collator.getInstance().compare(p1.title, p2.title);
			}
		});
		

		// Add the Global config entry
		AppModel global = new AppModel();
		global.packageName = "global";
		global.title = getString(R.string.global);
		global.icon = null;
		mAppList.add(0, global);
	}

	@Override
	protected void onItemClick(int pos) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.setClass(getActivity(), GlobalActivity.class);
		i.putExtra("fragment", "peract");
		i.putExtra("pass", mAppList.get(pos).packageName);
		startActivity(i);
	}

}
