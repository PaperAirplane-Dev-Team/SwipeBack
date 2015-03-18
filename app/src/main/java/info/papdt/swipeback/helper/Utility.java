package info.papdt.swipeback.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

public class Utility
{
	public static boolean isLauncher(Context context, String packageName) {
		ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).resolveActivityInfo(context.getPackageManager(), 0);
		if (homeInfo != null) {
			return homeInfo.packageName.equals(packageName);
		} else {
			return false;
		}
	}
	
	public static <T> T $(Object obj) {
		return (T) obj;
	}
}
