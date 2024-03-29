package com.facebook.scrumptious;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.maboo.node.MainActivity;
import br.com.maboo.node.R;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.friend.FriendElement;
import com.facebook.friend.ListFriendElement;
import com.facebook.model.GraphUser;
import com.facebook.scrumptious.auxiliar.FaceUserVO;
import com.facebook.widget.ProfilePictureView;

public class FragmentSelection extends Fragment {

	private static final String TAG = "SelectionFragment";

	private ProfilePictureView profilePictureView;
	private TextView userNameView;

	private static final int REAUTH_ACTIVITY_CODE = 100;

	// Splash screen timer
	private int SPLASH_TIME_OUT = 15;

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

		// esconde a actionBar
		getActivity().getActionBar().hide();

		View view = inflater.inflate(R.layout.fragmentlogon, container, false);

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

	/*******************************************************************************
	 * 1. faz a requisi��o pelos dados do usuario
	 * 2. recupera lista de amigos
	 * 3. inicia o app
	 *******************************************************************************/
	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser g, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (g != null) {

								// cria o profile do usuario no Node
								createProfile(g.getId().toString(), g.getName()
										.toString());
								
								// recupera lista de amigos
								createFriendList();
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						} else { // tudo Ok!
							startMap();
						}
					}
				});
		request.executeAsync();
	}

	/*******************************************************************************
	 * cria profile do usuario
	 *******************************************************************************/
	private void createProfile(String id, String name) {
		// Set the id for the ProfilePictureView
		// view that in turn displays the profile
		// picture.
		profilePictureView.setProfileId(id);

		// Set the Textview's text to the user's name.
		userNameView.setText("Bem Vindo " + name);

		// salva nome\id do usuario
		FaceUserVO.user_name = name;
		FaceUserVO.id_user = id;
		FaceUserVO.profilePicture = profilePictureView.getImage();

	}

	/*******************************************************************************
	 * carrega a lista de friends
	 *******************************************************************************/
	
	private String URL_GRAPH = "https://graph.facebook.com/";
	private String FRIEND_SIZE_IMG = "/picture?width=90&height=90";
	
	private List<GraphUser> temp;
	
	public void createFriendList() {
		try {

		//	itensRandom = new ArrayList<MapAddrElement>();
		//	friends = new ArrayList<MapAddrElement>();

			ListFriendElement.friends = new ArrayList<FriendElement>();
			
			recoverList();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*******************************************************************************
	 * Recupera lista de amigos do face
	 *******************************************************************************/
	private void recoverList() {

		temp = new ArrayList<GraphUser>();
		Request request = Request.newMyFriendsRequest(
				Session.getActiveSession(),
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {

						temp = users;

						populateTempList();

					}
				});
		request.executeAsync();

	}

	/*******************************************************************************
	 * Popula lista de amigos do node (padrao MapAddrElement)
	 *******************************************************************************/
	private void populateTempList() {
		for (final GraphUser g : temp) {

			// url da imagem do usuario
			String url = URL_GRAPH + g.getId().toString() + FRIEND_SIZE_IMG;

			// if (false) {Log.d(TAG, "Friend " + g.getName().toString() + " > "
			// + url);}

			ListFriendElement.friends.add(new FriendElement(null, g.getId().toString(), g
					.getName().toString().toString(), "noob", url) {
			});

		}

	}
	
	/*******************************************************************************
	 * inicia realmente o app, pela tela do Map
	 *******************************************************************************/
	private void startMap() {
		// come�a um tempo e chama a tela padrao do
		// app(maps...)
		Handler handler = new Handler();

		// run a thread after 2 seconds to start the
		// home screen
		handler.postDelayed(new Runnable() {

			public void run() {

				// onDetach();

				// onDestroy();

				// start the home screen if the back
				// button wasn't pressed
				// already
				Intent intent = new Intent(getActivity(), MainActivity.class);

				startActivity(intent);

				getActivity().finish();
			}
		}, SPLASH_TIME_OUT); // time in milliseconds
								// (1 second = 1000
								// milliseconds)
								// until the run()
								// method will
								// be
								// called
	}

	/*******************************************************************************
	 * default services
	 *******************************************************************************/
	
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
		if (uiHelper != null) {
			uiHelper.onResume();
		}
	}

	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		if (uiHelper != null) {
			uiHelper.onSaveInstanceState(bundle);
		}
	}

	public void onPause() {
		super.onPause();
		if (uiHelper != null) {
			uiHelper.onPause();
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (uiHelper != null) {
			uiHelper.onDestroy();
		}
	}

}
