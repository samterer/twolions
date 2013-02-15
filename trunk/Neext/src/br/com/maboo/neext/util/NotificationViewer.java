package br.com.maboo.neext.util;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.ActivityCircle;

public class NotificationViewer extends ActivityCircle {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.notification_viewer);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.cancel(ConstantsNotify.NOTIFICATION_ID);
	}

}