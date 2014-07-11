package br.com.maboo.imageedit.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.util.AnimUtil;

public class ActivitySplashScreen extends Activity {

	private int pos = 0;
	private final long timeInit = 250;
	private final long secTimeInit = 250;

	private Handler mHandler;
	private ImageView imgLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_splash_screen);

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

				showStage();

			} else {
				mHandler.postDelayed(this, secTimeInit);

				// put logo hosp
				Drawable d = getResources().getDrawable(R.drawable.logo_hosp);
				imgLogo.setImageDrawable(d);

				// next step
				pos++;
			}
		}
	};

	private void showStage() {
		RelativeLayout layoutLoad = (RelativeLayout) findViewById(R.id.layout_load);
		layoutLoad.setVisibility(View.VISIBLE);

		ImageView curtainLeft, curtainRight, logoBig;
		logoBig = (ImageView) findViewById(R.id.logo_big);
		curtainLeft = (ImageView) findViewById(R.id.curtain_left);
		curtainRight = (ImageView) findViewById(R.id.curtain_right);
		
		AnimUtil.getInstance(this).animeCurtainIn(curtainLeft, curtainRight, logoBig);
	
		//goToImageSwapScreen();
	}

	private void goToImageSwapScreen() {
		// go to screen of swap images
		Intent intent = new Intent(ActivitySplashScreen.this,
				ActivityImageSwap.class);
		startActivity(intent);

		ActivitySplashScreen.this.finish();
	}

}
