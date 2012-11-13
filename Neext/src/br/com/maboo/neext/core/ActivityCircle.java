package br.com.maboo.neext.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

public class ActivityCircle extends Activity {

	// Ciclo de vida da atividade
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (icicle == null) {
			Log.i("appLog", "Open activity.");
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

	// animacao de transição
	protected static ViewFlipper flipper;

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

	// progress
	protected static ProgressDialog dialog;

}
