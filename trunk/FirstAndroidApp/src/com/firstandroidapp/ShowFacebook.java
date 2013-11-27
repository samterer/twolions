package com.firstandroidapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

public class ShowFacebook extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_facebook);

		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);
		
		TextView mTextview = (TextView) findViewById(R.id.mTextview);
		
		String name = getIntent().getStringExtra("name");
		String id = getIntent().getStringExtra("id");
		
		// foto
		profilePictureView.setProfileId(id);
		
		// nome
		mTextview.setText("Bem Vindo!"+ name);
	
	}


}
