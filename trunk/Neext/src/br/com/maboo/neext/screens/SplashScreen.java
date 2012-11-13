package br.com.maboo.neext.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.ActivityCircle;

public class SplashScreen extends ActivityCircle {

	Handler handler;

	Intent tInicial;

	boolean isFinishSplash = false;

	CountDownTimer timer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tInicial = new Intent(this, ListScreen.class);

		handler = new Handler();

		setContentView(R.layout.splash_layout);

		timer = new CountDownTimer(5000, 1000) // 3seceonds
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

		return true;

	}

	private void startMaboo() {
		Log.i("appLog", "init maboo()");

		startActivity(tInicial);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		handler.removeMessages(0);
	}

}