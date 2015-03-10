package info.papdt.swipeback.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;

import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class MySwipeBackHelper extends SwipeBackActivityHelper
{
	
	public MySwipeBackHelper(Activity activity) {
		super(activity);
	}

	@Override
	public void onActivityCreate() {
		super.onActivityCreate();
		
		// Set pixel format on start
		mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);
		
		// Set background on swiped
		getSwipeBackLayout().addSwipeListener(new SwipeBackLayout.SwipeListener() {
			@Override
			public void onScrollStateChange(int state, float scrollPercent) {
				
			}

			@Override
			public void onEdgeTouch(int edgeFlag) {
				mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			}

			@Override
			public void onScrollOverThreshold() {
				
			}		
		});
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
