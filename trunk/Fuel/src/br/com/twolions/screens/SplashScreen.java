package br.com.twolions.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import br.com.twolions.R;
import br.com.twolions.core.ActivityCircle;

public class SplashScreen extends ActivityCircle {

	Handler handler;

	Intent tInicial;

	boolean isFinishSplash = false;

	CountDownTimer timer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("appLog", "onCreate");

		tInicial = new Intent(this, ListCarScreen.class);

		handler = new Handler();

		setContentView(R.layout.splashscreen);

		timer = new CountDownTimer(3000, 1000) // 10seceonds
												// Timer
		{
			@Override
			public void onTick(long millisUntilFinished) {
				Log.i("appLog", "seconds remaining: " + millisUntilFinished
						/ 1000);
			}

			@Override
			public void onFinish() {
				Log.i("appLog", "done!");

				handler.post(new Runnable() {
					public void run() {
						if (!isFinishSplash) {
							startMaboo();

							finish();
						}
					}
				});
			};
		}.start();
	}

	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		// clicando na tela
		case MotionEvent.ACTION_DOWN:
			//
			return true;
		case MotionEvent.ACTION_UP:

			// handler.removeMessages(what);
			isFinishSplash = true;

			timer = null;

			startMaboo();

			finish();
		}

		return true;

	}

	private void startMaboo() {
		Log.i("appLog", "init maboo()");
		startActivity(tInicial);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		handler.removeMessages(0);
	}

}