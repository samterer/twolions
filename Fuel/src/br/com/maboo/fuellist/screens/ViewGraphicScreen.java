package br.com.maboo.fuellist.screens;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.core.ActivityGraphic;
import br.com.maboo.fuellist.util.Constants;

public class ViewGraphicScreen extends ActivityGraphic {

	private String TAG = Constants.LOG_APP;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mountScreen();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	private void mountScreen() {
		setContentView(R.layout.view_graphic);
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE) {

			Toast.makeText(this, "voltando a lista...", Toast.LENGTH_SHORT)
					.show();

			// finish();
		}

	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/

	public void onBackPressed() { // call my backbutton pressed method when
									// boolean==true
		Log.i(TAG, "Clicked back");

	}

}
