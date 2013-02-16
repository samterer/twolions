package br.com.maboo.neext.util;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import br.com.maboo.neext.core.ActivityCircle;

public class NotificationViewer extends ActivityCircle {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Cancela a notificação
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Para cancelar precisa utilizar o mesmo id que foi utilizado para
		// criar
		nm.cancel(ConstantsNotify.NOTIFICATION_ID);
		
		finish();

		String SettingsPage = "br.com.maboo.neext/.screens.ListScreen";

		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setComponent(ComponentName.unflattenFromString(SettingsPage));
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
}
