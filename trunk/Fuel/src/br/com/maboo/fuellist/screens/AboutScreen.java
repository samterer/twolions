package br.com.maboo.fuellist.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.core.ActivityCircle;

public class AboutScreen extends ActivityCircle {

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
	
	public void linkTwitter(View v) {
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/maboobr"));
        startActivity(intent);
		
	}
	
	public void link(View v) {
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maboobr.wix.com/maboobr"));
        startActivity(intent);
		
	}

}
