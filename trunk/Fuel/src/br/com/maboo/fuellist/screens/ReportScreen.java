package br.com.maboo.fuellist.screens;

import android.content.res.Configuration;
import android.os.Bundle;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.core.ActivityReport;
import br.com.maboo.fuellist.util.Constants;

public class ReportScreen extends ActivityReport {

	private String TAG = Constants.LOG_APP;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mountScreen();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	private void mountScreen() {
		setContentView(R.layout.view_report);
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			finish();
		}

	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/

}
