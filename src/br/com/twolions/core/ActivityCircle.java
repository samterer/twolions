package br.com.twolions.core;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ActivityCircle extends Activity {

	// Ciclo de vida da atividade
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (icicle == null) {
			Log.i("appLog", "Inicio do app.");
		} else {
			Log.i("appLog", getClassName() + ".onCreate() chamado: " + icicle);
		}
		// monta tela
		confTela();
	}

	private void confTela() {
		// retira o titulo da tela
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
