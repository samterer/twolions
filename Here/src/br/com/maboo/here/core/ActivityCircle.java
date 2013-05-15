package br.com.maboo.here.core;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.google.android.maps.MapActivity;

public class ActivityCircle extends MapActivity {

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
	
	/*********************************************************************************
	 * ANIMATE
	 *********************************************************************************/

	public Animation inFromRightAnimation() {

		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	public Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	public Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}


	protected boolean isRouteDisplayed() {
		return false;
	}

}
