package br.com.maboo.neext.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.ActivityCircle;

public class AndroidUtils extends ActivityCircle {
	protected static final String TAG = Constants.LOG_APP;

	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {

			return false;

		} else {

			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static boolean isConnectionDB(Context context) {
		boolean result = false;

		// open conection with db
		result = true;

		return result;
	}

	public static void alertDialog(final Context context, final int mensagem,
			final String title) {

		try {

			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(title).setMessage(mensagem).create();

			dialog.setIcon(R.drawable.iconerror);

			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					// fechar o app
					return;
				}
			});

			dialog.show();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	public void closedApp() {

	}

	public static void alertDialog(final Context context, final String mensagem) {

		try {

			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(context.getString(R.string.app_name))
					.setMessage(mensagem).create();

			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					return;
				}
			});

			dialog.show();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	// Retorna se é Android 3.x "honeycomb" ou superior (API Level 11)
	public static boolean isAndroid_3() {

		int apiLevel = Build.VERSION.SDK_INT;

		if (apiLevel >= 11) {
			return true;
		}

		return false;

	}

	// Retorna se a tela é large ou xlarge
	public static boolean isTablet(Context context) {

		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

	}

	// Retona se é um tablet com Android 3.x
	public static boolean isAndroid_3_Tablet(Context context) {

		return isAndroid_3() && isTablet(context);

	}

	// Fecha o teclado virtual se aberto (view com foque)
	public static boolean closeVirtualKeyboard(Context context, View view) {

		// Fecha o teclado virtual
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm != null) {
			boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			return b;
		}

		return false;
	}
	
	public static String getMonth(int month){
		int indice = 0;
		
		// ajusta o retorno do mes
		indice = month - 1;
		
	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    return monthNames[indice];
	}
}
