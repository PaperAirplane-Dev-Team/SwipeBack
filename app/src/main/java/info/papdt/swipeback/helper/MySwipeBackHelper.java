package info.papdt.swipeback.helper;

import android.app.Activity;
import android.content.Context;

import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

public class MySwipeBackHelper extends SwipeBackActivityHelper
{
	
	public MySwipeBackHelper(Activity activity) {
		super(activity);
	}
	
	@Override
	protected Context getGlobalContext() {
		try {
			return mActivity.createPackageContext("info.papdt.swipeback", Context.CONTEXT_IGNORE_SECURITY);
		} catch (Exception e) {
			return super.getGlobalContext();
		}
	}
}
