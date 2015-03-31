package info.papdt.swipeback.mod;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;

import static de.robv.android.xposed.XposedHelpers.*;
import static info.papdt.swipeback.helper.Utility.*;

/**
 * Fix rotation failure
 */
public class ModRotationFix {
	static void fixOnActivityCreate(Activity activity) {
		boolean isRotationLocked = System.getInt(activity.getContentResolver(), System.ACCELEROMETER_ROTATION, 0) == 0;
		if (!isRotationLocked) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
		ContentObserver observer = new RotateObserver(activity, new RotateHandler(activity));
		activity.getContentResolver().registerContentObserver(System.getUriFor(System.ACCELEROMETER_ROTATION), false, observer);
		setAdditionalInstanceField(activity, "rotateObserver", observer);
	}

	static void fixOnActivityFinish(Activity activity) {
		ContentObserver observer = $(getAdditionalInstanceField(activity, "rotateObserver"));
		if (observer != null)
			activity.getContentResolver().unregisterContentObserver(observer);
	}

	private static class RotateObserver extends ContentObserver {
		private Activity mActivity;
		private Handler mHandler;

		public RotateObserver(Activity activity, Handler handler) {
			super(handler);
			mActivity = activity;
			mHandler = handler;
		}

		@Override
		public void onChange(boolean selfChange) {
			boolean isRotationLocked = (System.getInt(mActivity.getContentResolver(), System.ACCELEROMETER_ROTATION, 0) == 0);
			mHandler.sendMessage(mHandler.obtainMessage(0, isRotationLocked));
		}
	}

	private static class RotateHandler extends Handler {
		private Activity mActivity;

		public RotateHandler(Activity activity) {
			mActivity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (!(Boolean) msg.obj) {
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				} else {
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
				}
			}
		}
	}
}
