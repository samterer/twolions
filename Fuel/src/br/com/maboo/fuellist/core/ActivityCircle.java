package br.com.maboo.fuellist.core;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ActivityCircle extends Activity {

	// Ciclo de vida da atividade
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (icicle == null) {
			// Log.i("appLog", "Open activity.");
		} else {
			Log.i("appLog", getClassName() + ".onCreate() chamado: " + icicle);
		}
	}

	protected void onStart() {
		super.onStart();
		Log.i("appLog", getClassName() + ".onStart() chamado.");
	}

	protected void onRestart() {
		super.onRestart();
		Log.i("appLog", getClassName() + ".onRestart() chamado.");
	}

	protected void onResume() {
		super.onResume();
		Log.i("appLog", getClassName() + ".onResume() chamado.");
	}

	protected void onPause() {
		super.onPause();
		Log.i("appLog", getClassName() + ".onPause() chamado.");
	}

	protected void onStop() {
		super.onStop();
		Log.i("appLog", getClassName() + ".onStop() chamado.");
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.i("appLog", getClassName() + ".onDestroy() chamado.");
	}

	// retorna o nome da classe sem o pacote
	public String getClassName() {
		String s = getClass().getName();
		return s.substring(s.lastIndexOf("."));
	}

}
