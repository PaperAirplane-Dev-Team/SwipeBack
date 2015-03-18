package info.papdt.swipeback.mod;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedHelpers.*;

import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

import java.lang.reflect.Method;

import info.papdt.swipeback.helper.WindowInsetsColorDrawable;
import info.papdt.swipeback.helper.Settings;

public class ModSDK21
{
	private static Settings mSettings;
	
	public static void zygoteSDK21() {
		mSettings = Settings.getInstance(null);
		findAndHookMethod("com.android.internal.policy.impl.PhoneWindow", null, "setStatusBarColor", int.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				int color = Integer.valueOf(mhparams.args[0].toString());
				if (color == 0)
					return;
				SwipeBackActivityHelper helper = (SwipeBackActivityHelper) getAdditionalInstanceField(mhparams.thisObject, "helper");
				if (helper != null) {
					ViewGroup root = (ViewGroup) helper.getSwipeBackLayout().getChildAt(0);
					View content = root.getChildAt(0);
					
					if (content.getBackground() instanceof WindowInsetsColorDrawable) {
						WindowInsetsColorDrawable d = (WindowInsetsColorDrawable) content.getBackground();
						d.setTopDrawable(new ColorDrawable(color));
						((Method) mhparams.method).invoke(mhparams.thisObject, Color.TRANSPARENT);
					}
				}
			}
		});
	}
	
	public static void afterOnCreateSDK21(SwipeBackActivityHelper helper, Activity activity, String packageName, String className) throws Throwable {
		mSettings.reload();
		if (mSettings.getBoolean(packageName, className, Settings.LOLLIPOP_HACK, false)) {
			setAdditionalInstanceField(activity.getWindow(), "helper", helper);
		} else {
			setAdditionalInstanceField(activity.getWindow(), "helper", null);
		}
	}
	
	public static void afterOnPostCreateSDK21(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
		Class<?> internalStyleable = findClass("com.android.internal.R.styleable", null);
		int[] internalTheme = (int[]) getStaticObjectField(internalStyleable, "Theme");
		int internalColorPrimary = getStaticIntField(internalStyleable, "Theme_colorPrimaryDark");
		
		SwipeBackActivityHelper helper = (SwipeBackActivityHelper) getAdditionalInstanceField(mhparams.thisObject, "helper");
		if (helper != null) {
			final Activity activity = (Activity) mhparams.thisObject;

			String packageName = activity.getApplicationInfo().packageName;
			String className = activity.getClass().getName();
			
			mSettings.reload();
			if (!mSettings.getBoolean(packageName, className, Settings.LOLLIPOP_HACK, false))
				return;
			
			ViewGroup root = (ViewGroup) helper.getSwipeBackLayout().getChildAt(0);
			View content = root.getChildAt(0);
			final WindowInsetsColorDrawable bkg = new WindowInsetsColorDrawable(content.getBackground());
			content.setBackground(bkg);

			TypedArray a = activity.getTheme().obtainStyledAttributes(internalTheme);
			int primary = a.getColor(internalColorPrimary, Color.TRANSPARENT);
			a.recycle();

			if (primary != Color.TRANSPARENT) {
				bkg.setTopDrawable(new ColorDrawable(primary));
			}

			root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
				@Override
				public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
					bkg.setTopInset(insets.getSystemWindowInsetTop());
					activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
					return insets;
				}
			});
		}
	}
}
