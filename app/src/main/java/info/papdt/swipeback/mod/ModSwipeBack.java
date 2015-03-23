package info.papdt.swipeback.mod;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
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
				Activity activity = $(mhparams.thisObject);
				
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
				
				int sensitivity = mSettings.getInt(packageName, className, Settings.SENSITIVITY, 100);
				helper.getSwipeBackLayout().setSensitivity(activity, (float) sensitivity / 100.0f);
				
				setAdditionalInstanceField(activity, "helper", helper);
				
				if (Build.VERSION.SDK_INT >= 21)
					ModSDK21.afterOnCreateSDK21(helper, activity, packageName, className);
			}
		});
		
		findAndHookMethod(Activity.class, "onPostCreate", Bundle.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				SwipeBackActivityHelper helper = $(getAdditionalInstanceField(mhparams.thisObject, "helper"));
				if (helper != null) {
					Activity activity = $(mhparams.thisObject);

					int isFloating = getStaticIntField(findClass("com.android.internal.R.styleable", null), "Window_windowIsFloating");
					if (activity.getWindow().getWindowStyle().getBoolean(isFloating, false)) {
						setAdditionalInstanceField(mhparams.thisObject, "helper", null);
						return;
					}
					
					helper.onPostCreate();
					
					if (Build.VERSION.SDK_INT == 21)
						ModSDK21.afterOnPostCreateSDK21(mhparams);
				}
			}
		});

		findAndHookMethod(Activity.class, "findViewById", int.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				if (mhparams.getResult() == null) {
					SwipeBackActivityHelper helper = $(getAdditionalInstanceField(mhparams.thisObject, "helper"));
					if (helper != null) {
						mhparams.setResult(helper.findViewById((int) mhparams.args[0]));
					}
				}
			}
		});
		
		findAndHookMethod(Activity.class, "finish", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				MySwipeBackHelper helper = $(getAdditionalInstanceField(mhparams.thisObject, "helper"));
				if (helper != null && helper.getSwipeBackLayout().getScrollPercent() < 1) {
					Activity activity = $(mhparams.thisObject);
					String packageName = activity.getApplicationInfo().packageName;
					String className = activity.getClass().getName();
					
					mSettings.reload();
					if (!mSettings.getBoolean(packageName, className, Settings.SCROLL_TO_RETURN, false))
						return;
					
					helper.onFinish();
					Object isFinishing = getAdditionalInstanceField(mhparams.thisObject, "isFinishing");
					
					// Replace the default 'finish' by scrollToFinish
					if (isFinishing == null || !Boolean.valueOf(isFinishing)) {
						setAdditionalInstanceField(mhparams.thisObject, "isFinishing", true);
						mhparams.setResult(null);
						helper.getSwipeBackLayout().scrollToFinishActivity();
					}
				}
			}
		});
		
		ModKK441.hookKK441();
		
		if (Build.VERSION.SDK_INT < 21)
			return;
			
		ModSDK21.zygoteSDK21();
	}
	
	private void hookActivityRecord(Class<?> clazz) throws Throwable {
		XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				String packageName = $(getObjectField(mhparams.thisObject, "packageName"));
				ActivityInfo activity = $(getObjectField(mhparams.thisObject, "info"));
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
