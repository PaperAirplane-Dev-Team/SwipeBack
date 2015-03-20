package info.papdt.swipeback.mod;

import android.os.Build;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import static de.robv.android.xposed.XposedHelpers.*;

// Fixes for KitKat 4.4.{1,2}
public class ModKK441
{
	public static void hookKK441() throws Throwable {
		if (!(Build.VERSION.RELEASE.equals("4.4.1") || Build.VERSION.RELEASE.equals("4.4.2")))
			return;
		
		Class<?> ar = findClass("com.android.server.am.ActivityRecord", null);
		findAndHookMethod("com.android.server.am.ActivityStack", null, "isActivityOverHome", ar, new XC_MethodReplacement() {
			@Override
			protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam mhparams) throws Throwable {
				// Android 4.4.1 introduced a bug that a stack is not calculated correctly
				// All the activities are considered as over Home.
				// This replacement is from 4.4 and will fix this.
				Object recordTask = getObjectField(mhparams.args[0], "task");
				Object activities = getObjectField(recordTask, "mActivities");
				int rIndex = (Integer) callMethod(activities, "indexOf", mhparams.args[0]);
				for (--rIndex; rIndex >= 0; --rIndex) {
					// Look down in tasks
					final Object blocker = callMethod(activities, "get", rIndex);
					boolean finishing = getBooleanField(blocker, "finishing");
					if (!finishing) {
						break;
					}
				}

				// Arrived bottom, but nothing found
				if (rIndex < 0) {
					return true;
				}

				return false;
			}
		});
	}
}
