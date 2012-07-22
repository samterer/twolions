package br.com.twolions.screen;

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

	Intent mInicial;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mInicial = new Intent(this, MenuScreen.class);

		handler = new Handler();

		setContentView(R.layout.splashscreen);

		CountDownTimer timer = new CountDownTimer(1000, 1000) // 10seceonds
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
			montaMenuInicial();
		}

		return true;

	}

	private void montaMenuInicial() {
		// Log.i("appLog", "montaMenuInicial()");
		startActivity(mInicial);
	}

}
