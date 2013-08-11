package br.com.twolions.coob.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import br.com.twolions.coob.R;
import br.com.twolions.coob.core.ActivityCircle;

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
	
	/****************************************************************
	 * LINKS
	 ****************************************************************/
	private Intent intent = null;
	private String url = "";
	
	public void openTwitter(View v) {
		url = "https://twitter.com/maboobr";
		
		openUrl();
	}

	public void openFacebook(View v) {
		url = "https://www.facebook.com/maboobr";
	
		openUrl();
	}

	public void openPage(View v) {
		url = "http://maboobr.wix.com/maboobr";
	
		openUrl();
	}
	
	public void openMarket(View v) {
		url = "https://play.google.com/store/apps/developer?id=MABOOBR&hl=pt_BR";
	
		openUrl();
	}
	
	private void openUrl(){
		
		intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	
		startActivity(intent);
		
	}
}
