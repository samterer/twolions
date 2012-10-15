package br.com.twolions.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

		setContentView(R.layout.view_item);

		mountScreen();

		organizeBt();
	}

	/******************************************************************************
	 * SERVICES
	 * 
	 ******************************************************************************/

	private AlertDialog mountScreen() {

		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View npView = inflater.inflate(R.layout.view_item, null);
		return new AlertDialog.Builder(this)
				.setTitle("Text Size:")
				.setView(npView)
				.setPositiveButton("Ok!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).create();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	public void openAlert(View v) {
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage("Are you sure you want to delete this car?");

		// Método executado se escolher Sim
		alerta.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//
			}
		});

		// Método executado se escolher Não
		alerta.setNegativeButton("Not", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//
			}
		});
		// Exibe o alerta de confirmação
		alerta.show();
	}

	/******************************************************************************
	 * BTS
	 ******************************************************************************/

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
