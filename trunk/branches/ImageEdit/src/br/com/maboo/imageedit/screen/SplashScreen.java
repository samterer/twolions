package br.com.maboo.imageedit.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.core.MainActivity;

public class SplashScreen extends Activity {

	private int pos = 0;
	private final long timeInit = 500;
	private final long secTimeInit = 250;

	private Handler mHandler;
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_splash);
		text = (TextView) findViewById(R.id.textSplash);
		text.setText("Hi!");
		
		mHandler = new Handler();
		mHandler.postDelayed(timedTask, timeInit);

	}

	private Runnable timedTask = new Runnable() {

		@Override
		public void run() {
			// verifica se é o fim da splashScreen
			if (pos > 0) {
				// fim da splash screen
				mHandler.removeCallbacks(timedTask);

				// go to screen of camera
				Intent intent = new Intent(SplashScreen.this,
						MainActivity.class);
				startActivity(intent);
				
				// fecha esta activity
				SplashScreen.this.finish();
				
				 // transition from splash to main menu
	            overridePendingTransition(R.anim.fadein,
	                    R.anim.fadeout);

			} else {
				// roda a thread
				mHandler.postDelayed(this, secTimeInit);
				text.setText("Hospitalhaços");
				pos++;
			}
		}
	};

}
