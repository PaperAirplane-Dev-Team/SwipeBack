package info.papdt.swipeback.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.support.v4.app.NotificationCompat;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.base.GlobalActivity;

public class ClassNameReceiver extends BroadcastReceiver
{
	public static final String ACTION = "info.papdt.swipeback.intent.CLASS_NAME";
	public static final String EXTRA_CLASSNAME = "classname";
	public static final String EXTRA_PACKAGENAME = "packagename";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String packageName = intent.getStringExtra(EXTRA_PACKAGENAME);
		String className = intent.getStringExtra(EXTRA_CLASSNAME);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		if (packageName != null && className != null) {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			
			// Notification content
			String title = context.getResources().getString(R.string.notify_title);
			String summary = context.getResources().getString(R.string.notify_summary);
			String content = String.format(context.getResources().getString(R.string.notify_content), packageName, className);
			builder.setContentTitle(title);
			builder.setContentText(content);
			builder.setSmallIcon(android.R.color.transparent);
			builder.setPriority(NotificationCompat.PRIORITY_LOW);
			NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
			style.setBigContentTitle(context.getResources().getString(R.string.notify_title));
			style.bigText(content);
			style.setSummaryText(summary);
			builder.setStyle(style);
			
			// Per-app action
			Intent i = new Intent(context, GlobalActivity.class);
			i.putExtra("pass", packageName);
			i.putExtra("fragment", "peract");
			PendingIntent pending = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
			builder.addAction(android.R.color.transparent, context.getResources().getString(R.string.notify_app), pending);
			
			// Per-activity button
			i = new Intent(context, GlobalActivity.class);
			i.putExtra("pass", packageName + "," + className);
			i.putExtra("fragment", "peract");
			pending = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
			builder.addAction(android.R.color.transparent, context.getResources().getString(R.string.notify_activity), pending);
			builder.setContentIntent(pending);
			nm.notify(R.drawable.ic_launcher, builder.build());
		} else {
			nm.cancel(R.drawable.ic_launcher);
		}
	}
}
