package info.papdt.swipeback.mod;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedHelpers.*;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

import info.papdt.swipeback.helper.MySwipeBackHelper;
import info.papdt.swipeback.helper.Settings;
import static info.papdt.swipeback.helper.Utility.*;
import static info.papdt.swipeback.BuildConfig.DEBUG;

public class ModSwipeBack implements IXposedHookLoadPackage, IXposedHookZygoteInit
{
	private static final String TAG = ModSwipeBack.class.getSimpleName();
	
	private Settings mSettings = Settings.getInstance(null);

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		if (lpparam.packageName.equals("android")) {
			log("detected android framework");
			try {
				Class<?> ar = findClass("com.android.server.am.ActivityRecord", lpparam.classLoader);

				if (ar != null)
					hookActivityRecord(ar);
			} catch (ClassNotFoundError e) {
				// Exception is thrown in pre-Lollipop system
			}
		}
	}

	@Override
	public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
		
		try {
			Class<?> ar = findClass("com.android.server.am.ActivityRecord", null);
			
			if (ar != null)
				hookActivityRecord(ar);
		} catch (ClassNotFoundError e) {
			// Exception is thrown in Lollipop
		}
		
		findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				Activity activity = (Activity) mhparams.thisObject;
				
				String packageName = activity.getApplicationInfo().packageName;
				String className = activity.getClass().getName();
				
				if (shouldExclude(packageName, className))
					return;
				
				if (isLauncher(activity, packageName))
					return;
				
				SwipeBackActivityHelper helper = new MySwipeBackHelper(activity);
				helper.onActivityCreate();
				helper.getSwipeBackLayout().setEnableGesture(true);
				
				mSettings.reload();
				int edge = mSettings.getInt(packageName, className, Settings.EDGE, SwipeBackLayout.EDGE_LEFT);
				helper.getSwipeBackLayout().setEdgeTrackingEnabled(edge);
				setAdditionalInstanceField(activity, "helper", helper);
			}
		});

		findAndHookMethod(Activity.class, "onPostCreate", Bundle.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				SwipeBackActivityHelper helper = (SwipeBackActivityHelper) getAdditionalInstanceField(mhparams.thisObject, "helper");
				if (helper != null) {
					helper.onPostCreate();
					
					if (Build.VERSION.SDK_INT >= 21)
						setAdditionalInstanceField(((Activity) mhparams.thisObject).getWindow().getDecorView(), "helper", helper);
				}
			}
		});

		findAndHookMethod(Activity.class, "findViewById", int.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				if (mhparams.getResult() == null) {
					SwipeBackActivityHelper helper = (SwipeBackActivityHelper) getAdditionalInstanceField(mhparams.thisObject, "helper");
					if (helper != null) {
						mhparams.setResult(helper.findViewById((int) mhparams.args[0]));
					}
				}
			}
		});
	}
	
	private void hookActivityRecord(Class<?> clazz) throws Throwable {
		XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				String packageName = getObjectField(mhparams.thisObject, "packageName").toString();
				ActivityInfo activity = (ActivityInfo) getObjectField(mhparams.thisObject, "info");
				if (shouldExclude(packageName, activity.name))
					return;

				boolean isHome = false;
				if (Build.VERSION.SDK_INT >= 19) {
					isHome = Boolean.valueOf(callMethod(mhparams.thisObject, "isHomeActivity"));
				} else {
					isHome = getBooleanField(mhparams.thisObject, "isHomeActivity");
				}

				if (!isHome) {
					// fullscreen = false means transparent
					setBooleanField(mhparams.thisObject, "fullscreen", false);
				}
			}
		});
	}

	private boolean shouldExclude(String packageName, String className) {
		if (packageName.equals("com.android.systemui")) {
			return true;
		} else {
			mSettings.reload();
			return !mSettings.getBoolean(packageName, className, Settings.ENABLE, true);
		}
	}
	
	private static void log(String log) {
		if (DEBUG) {
			XposedBridge.log(TAG + ": " + log);
		}
	}

}
