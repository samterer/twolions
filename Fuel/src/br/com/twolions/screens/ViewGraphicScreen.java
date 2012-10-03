package br.com.twolions.screens;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import br.com.twolions.R;
import br.com.twolions.core.ActivityViewItem;
import br.com.twolions.util.Constants;

public class ViewGraphicScreen extends ActivityViewItem {

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

			// Toast.makeText(this, "voltando a lista...",
			// Toast.LENGTH_SHORT).show();

			finish();
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
