package br.com.twolions.screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import br.com.twolions.R;
import br.com.twolions.core.ActivityCircle;

public class TwoLionsSplashScreen extends ActivityCircle {

	Handler handler;

	Intent splashScreen;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		splashScreen = new Intent(this, SplashScreen.class);

		handler = new Handler();

		setContentView(R.layout.splashtwolions);

		CountDownTimer timer = new CountDownTimer(2000, 1000) // 10seceonds
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
						// finishActivity(0);

						finish();

						montaMenuInicial();
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

			finish();

			montaMenuInicial();
		}

		return true;

	}

	private void montaMenuInicial() {
		// Log.i("appLog", "montaMenuInicial()");
		startActivity(splashScreen);
	}

}
