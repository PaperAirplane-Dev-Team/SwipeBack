package info.papdt.swipeback.mod;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedHelpers.*;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

import info.papdt.swipeback.helper.MySwipeBackHelper;
import static info.papdt.swipeback.BuildConfig.DEBUG;

public class ModSwipeBack implements IXposedHookLoadPackage
{
	private static final String TAG = ModSwipeBack.class.getSimpleName();

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		log("Loading package " + lpparam.packageName);
		
		if (lpparam.packageName.equals("android")) {
			Class<?> ar = findClass("com.android.server.am.ActivityRecord", lpparam.classLoader);

			XposedBridge.hookAllConstructors(ar, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
					String packageName = getObjectField(mhparams.thisObject, "packageName").toString();
					if (shouldExclude(packageName))
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
			return;
		} else if (shouldExclude(lpparam.packageName)) {
			return;
		}
		
		findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				Activity activity = (Activity) mhparams.thisObject;
				SwipeBackActivityHelper helper = new MySwipeBackHelper(activity);
				helper.onActivityCreate();
				helper.getSwipeBackLayout().setEnableGesture(true);
				helper.getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
				setAdditionalInstanceField(activity, "helper", helper);
			}
		});
		
		findAndHookMethod(Activity.class, "onPostCreate", Bundle.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				SwipeBackActivityHelper helper = (SwipeBackActivityHelper) getAdditionalInstanceField(mhparams.thisObject, "helper");
				if (helper != null) {
					helper.onPostCreate();
				}
			}
		});
	}
	
	private static boolean shouldExclude(String name) {
		if (name.equals("com.android.systemui")) {
			return true;
		} else {
			return false;
		}
	}
	
	private static void log(String log) {
		if (DEBUG) {
			XposedBridge.log(TAG + ": " + log);
		}
	}

}
