package br.com.maboo.here.activity;



import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;
import br.com.maboo.here.R;
import br.com.maboo.here.core.ActivityCircle;

public class AboutActivity extends ActivityCircle {
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
