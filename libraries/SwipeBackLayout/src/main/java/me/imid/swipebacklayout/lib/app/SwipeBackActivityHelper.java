package me.imid.swipebacklayout.lib.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;

/**
 * @author Yrom
 * @author PeterCxy
 */
public class SwipeBackActivityHelper {
    protected Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mSwipeBackLayout = new SwipeBackLayout(mActivity, getGlobalContext());
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
	
	protected Context getGlobalContext() {
        return mActivity;
    }
}
