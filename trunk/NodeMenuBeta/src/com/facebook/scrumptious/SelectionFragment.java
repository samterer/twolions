package com.facebook.scrumptious;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.maboo.node.nodemenubeta.MainActivity;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class SelectionFragment extends Fragment {

	private static final String TAG = "SelectionFragment";

	private ProfilePictureView profilePictureView;
	private TextView userNameView;

	private static final int REAUTH_ACTIVITY_CODE = 100;

	// Splash screen timer
	private int SPLASH_TIME_OUT = 1500;

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.selection, container, false);

		// Find the user's profile picture custom view
		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);

		// Find the user's name view
		userNameView = (TextView) view.findViewById(R.id.selection_user_name);

		// Check for an open session
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			// Get the user's data
			makeMeRequest(session);
		}

		return view;
	}

	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								// Set the id for the ProfilePictureView
								// view that in turn displays the profile
								// picture.
								profilePictureView.setProfileId(user.getId());
								// Set the Textview's text to the user's name.
								userNameView.setText(user.getName());

								// começa um tempo e chama a tela padrao do
								// app(maps...)
								Handler handler = new Handler();

								// run a thread after 2 seconds to start the
								// home screen
								handler.postDelayed(new Runnable() {

									public void run() {

										// start the home screen if the back
										// button wasn't pressed
										// already
										Intent intent = new Intent(
												getActivity(),
												MainActivity.class);

										startActivity(intent);
									}
								}, SPLASH_TIME_OUT * 3); // time in milliseconds
															// (1 second = 1000
															// milliseconds)
															// until the run()
															// method will
															// be
															// called
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}
					}
				});
		request.executeAsync();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			// Get the user's data.
			makeMeRequest(session);
		}
	}

	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

}
