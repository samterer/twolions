package br.com.maboo.neext.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.ActivityCircle;

public class NotificationUtil extends ActivityCircle {

	Notification notification;
	NotificationManager notificationManager;
	Intent intentNotifyViewer;

	public NotificationUtil(Context context) {
		
		intentNotifyViewer = new Intent();
		intentNotifyViewer.setClass(this, NotificationViewer.class);
		startActivity(intentNotifyViewer);
		
	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	}

	public void createNotify(String text, String contentTitle,
			String contentText) {
		
		int icon = R.drawable.iconerror;
		long when = System.currentTimeMillis();
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intentNotifyViewer, 0);
		
		notification = new Notification(icon, text, when);

		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;

		notification.ledARGB = Color.RED;
		notification.ledOffMS = 300;
		notification.ledOnMS = 300;

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		// notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentIntent);

	}

	public void postNotify() {
		try {
			notificationManager.notify(ConstantsNotify.NOTIFICATION_ID,
					notification);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}
}
