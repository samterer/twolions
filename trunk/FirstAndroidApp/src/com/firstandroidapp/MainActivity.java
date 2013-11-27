package com.firstandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainActivity extends Activity {

	String facebook_id;
	String facebook_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@SuppressWarnings("deprecation")
			@Override
			public void call(final Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {

					// make request to the /me API
					Request request = Request.newMeRequest(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								public void onCompleted(GraphUser user,
										Response response) {
									if (session == Session.getActiveSession()) {

										if (user != null) {
											facebook_id = user.getId();// user
																		// id
											facebook_name = user.getName();

											Log.i("appLog", facebook_id);
											Log.i("appLog", facebook_name);

											Intent myIntent = new Intent(
													MainActivity.this,
													ShowFacebook.class);
											myIntent.putExtra("name",
													facebook_name);
											myIntent.putExtra("id", facebook_id);
											startActivity(myIntent);
			

										} else {
											Log.i("appLog", "the user is null");
										}
									} else if (response.getError() != null) {
										Toast.makeText(MainActivity.this,
												"Houve um erro com a sessão",
												Toast.LENGTH_SHORT).show();
									}
								}
							});
					request.executeAsync();
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
