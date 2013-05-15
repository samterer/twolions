package br.com.maboo.here.util;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import br.com.maboo.here.R;

@SuppressWarnings("serial")
public class AndroidUtils implements OnCallHomeScreenListener {

	private Object object;

	protected static final String TAG = "appLog";

	public AndroidUtils(Object object) {

		this.object = object;

	}

	public static boolean isNetWorkAvailable(Context context) {

		boolean isGPS = false;

		boolean isNet = false;

		// internet
		// ConnectivityManager connectivity = (ConnectivityManager)
		// context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// gps
		// ConnectivityManager gpstivity = (ConnectivityManager)
		// context.getSystemService(Context.LOCATION_SERVICE);

		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		List<String> providers = locationManager.getProviders(true);

		for (String providerName : providers) {

			if (providerName.equals(LocationManager.GPS_PROVIDER)) {

				isGPS = true;
				// return true;

			}

			// if (providerName.equals(LocationManager.NETWORK_PROVIDER)) {
			//
			// ConnectivityManager cm = (ConnectivityManager) context
			// .getSystemService(Context.CONNECTIVITY_SERVICE);
			//
			// boolean isConnected = cm.getActiveNetworkInfo() != null
			// && cm.getActiveNetworkInfo().isConnectedOrConnecting();
			//
			// if (isConnected) {
			//
			// // return true;
			// retorno = true;
			//
			// } else {
			//
			// retorno = false;
			//
			// }
			//
			// }

		}

		// internet
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] info = connectivity.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					isNet = true;
				}
			}
		}

		// return false;
		if (isGPS && isNet) {
			return true;
		} else {
			return false;
		}

		/*
		 * if (connectivity == null || gpstivity == null) {
		 * 
		 * return false;
		 * 
		 * } else {
		 * 
		 * NetworkInfo[] info = connectivity.getAllNetworkInfo(); if (info !=
		 * null) { for (int i = 0; i < info.length; i++) { if
		 * (info[i].getState() == NetworkInfo.State.CONNECTED) { return true; }
		 * } }
		 * 
		 * }
		 * 
		 * return false;
		 */
	}

	public void alertDialog(final Context context, final int mensagem) {
		try {
			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(context.getString(R.string.app_name))
					.setMessage(mensagem).create();

			dialog.setIcon(R.drawable.iconerror);

			dialog.setTitle("Falha na conexão");

			dialog.setButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					if (context instanceof OnCallHomeScreenListener) {

						// Log.i("appLog", "call onExecute");

						onExecute();
					}

					return;
				}
			});
			dialog.show();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	public void onExecute() {
		if (object != null && object instanceof OnCallHomeScreenListener) {
			((OnCallHomeScreenListener) object).onExecute();
		}
	}
}
