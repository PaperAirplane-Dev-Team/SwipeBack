package info.papdt.swipeback.ui.model;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;

public class AppModel
{
	public String packageName;
	public String title;
	public WeakReference<Drawable> icon;
	
	public Runnable iconRefreshRunnable;
	
	public void refreshIcon() {
		if (iconRefreshRunnable != null)
			iconRefreshRunnable.run();
	}
}
