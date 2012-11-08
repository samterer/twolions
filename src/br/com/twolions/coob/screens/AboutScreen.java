package br.com.twolions.coob.screens;

import java.util.Vector;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import br.com.twolions.coob.R;
import br.com.twolions.coob.core.ActivityCircle;
import br.com.twolions.coob.util.HexValidator;

public class AboutScreen extends ActivityCircle {

	Vector<EditText> vEditText;

	Vector<LinearLayout> vLinearLayout;

	HexValidator hex;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();

	}

	/****************************************************************
	 * SERVICES
	 ****************************************************************/

	private void init() {

		setContentView(R.layout.about_layout);

	}

	public void onBackPressed() { // call my backbutton pressed method when
		super.onBackPressed();

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}
	
	/****************************************************************
	 * TOUCH
	 ****************************************************************/
	
	public void hideAbout(View v) {
		finish();
		
		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

}
