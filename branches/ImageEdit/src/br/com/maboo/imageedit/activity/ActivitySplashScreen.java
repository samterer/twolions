package br.com.maboo.imageedit.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import br.com.maboo.imageedit.R;

public class ActivitySplashScreen extends Activity {

	private int pos = 0;
	private final long timeInit = 1000;
	private final long secTimeInit = 500;

	private Handler mHandler;
	private ImageView imgLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_splash);
		
		imgLogo = (ImageView) findViewById(R.id.logo);
		
		// init timer
		mHandler = new Handler();
		mHandler.postDelayed(timedTask, timeInit);

	}

	private Runnable timedTask = new Runnable() {

		@Override
		public void run() {
			// verifica se é o fim da splashScreen
			if (pos > 0) {
				
				// end of this splash (handler)
				mHandler.removeCallbacks(timedTask);

				// go to screen of camera
				Intent intent = new Intent(ActivitySplashScreen.this, ImageSwap.class);
				startActivity(intent);
				
				ActivitySplashScreen.this.finish(); // go to next part of splash
				
	            overridePendingTransition(R.anim.fadein, R.anim.fadeout); // fecha curtinas

			} else {
				// roda a thread
				mHandler.postDelayed(this, secTimeInit);
				
				// put logo hosp
				Drawable d = getResources().getDrawable(R.drawable.logo_hosp);
				imgLogo.setImageDrawable(d);
				
				// next step
				pos++;
			}
		}
	};

}
