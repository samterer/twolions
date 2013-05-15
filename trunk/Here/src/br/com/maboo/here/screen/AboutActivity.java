package br.com.maboo.here.screen;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ViewFlipper;
import br.com.maboo.here.R;

public class AboutActivity extends Activity {
	ViewFlipper flipper;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mountScreen();

		flipper = (ViewFlipper) findViewById(R.id.flipper);

		Button button2 = (Button) findViewById(R.id.Button02);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				flipper.setInAnimation(inFromLeftAnimation());
				flipper.setOutAnimation(outToRightAnimation());
				flipper.showPrevious();
			}
		});

	}

	private void mountScreen() {

		setContentView(R.layout.layout_about);

		// setContentView(R.layout.animate);

	}

	public void openWebPage() {

		// startActivity(new Intent(Intent.ACTION_VIEW,
		// Uri.parse("http://www.twolion.blogspot.com")));

		flipper.setInAnimation(inFromLeftAnimation());
		flipper.setOutAnimation(outToRightAnimation());
		flipper.showPrevious();

	}

	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			flipper.setInAnimation(inFromLeftAnimation());
			flipper.setOutAnimation(outToRightAnimation());
			flipper.showPrevious();

			return true;
		}

		return super.onKeyDown(keyCode, event); // handles other keys
	}

}
