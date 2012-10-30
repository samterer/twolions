package br.com.twolions.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import br.com.twolions.R;
import br.com.twolions.core.ActivityCircle;
import br.com.twolions.interfaces.InterfaceBar;

public class SettingsScreen extends ActivityCircle implements InterfaceBar {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		init();

		organizeBt();

	}

	private void init() {

		setContentView(R.layout.form_set);

	}

	public void organizeBt() {

		// bt left
		final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_back);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_save);

	}

	public void btBarLeft(View v) {

		setResult(RESULT_CANCELED);

		// Fecha a tela
		finish();

	}

	public void btBarRight(View v) {
		// TODO Auto-generated method stub

	}

}
