package br.com.twolions.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import br.com.twolions.R;
import br.com.twolions.core.ActivityViewItem;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.util.Constants;

public class ViewItemScreen extends ActivityViewItem implements InterfaceBar {

	private String TAG = Constants.LOG_APP;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mountScreen();

		organizeBt();
	}

	private void mountScreen() {
		setContentView(R.layout.view_item);

	}

	public void btBarLeft(View v) {

		// go next screen
		finish();

	}
	public void btBarRight(View v) {

		//

	}

	public void organizeBt() {
		// bt left
		ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_cancel);

		// bt rigt
		// ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		// bt_right.setImageResource(R.drawable.bt_add);

		// ImageView title = (ImageView) findViewById(R.id.title);
		// title.setImageResource(R.drawable.t_select_vehicle);

	}
	public void onBackPressed() { // call my backbutton pressed method when
									// boolean==true

		Log.i(TAG, "Clicked back");

	}

}
