package br.com.maboo.node.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.androidbegin.menuviewpagertutorial.R;

public class Util {
	protected static final String TAG = "Util";

	// verifica se possui conexao com a internet
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

	// verifica se o gps esta ativo
	public static boolean isGpsAvailable(Context context) {
		LocationManager mlocManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		;
		boolean enabled = mlocManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (enabled) {
			return true;
		} else {
			return false;
		}
	}
	

	// verifica se existe rede
	public static boolean isVerify(Context context) {

		boolean net = isNetworkAvailable(context); // verifica se existe rede
		boolean gps = isGpsAvailable(context); // verifica se o gps esta ativo

		if (!net) {
			//msg = "Sem conexão com a internet baby";
			Log.i(TAG,"Sem conexão com a internet.");
		}

		if (!gps) {
			//msg = "O gps não esta ativo.";
			Log.i(TAG,"O gps não esta ativo.");
		}

		if (!net || !gps) {
			//toast(context, "Verifique o acesso a internet e o GPS");
			Log.i(TAG,"Verifique o acesso a internet e o GPS.");
			
			return false;
		}

		return true;

	}

	@SuppressWarnings("deprecation")
	public static void alertDialog(final Context context, final int mensagem,
			final String title) {

		try {

			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(title).setMessage(mensagem).create();

			dialog.setIcon(R.drawable.android_red);

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

	@SuppressWarnings("deprecation")
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

	public static void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	// Retorna se a tela é large ou xlarge
	public static boolean isTablet(Context context) {

		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

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

	public static String getMonth(int month) {
		int indice = 0;

		// ajusta o retorno do mes
		indice = month - 1;

		String[] monthNames = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		return monthNames[indice];
	}
}
