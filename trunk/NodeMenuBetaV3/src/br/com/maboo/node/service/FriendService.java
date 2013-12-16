package br.com.maboo.node.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.friend.FriendElement;
import com.facebook.model.GraphUser;

public class FriendService {

	private static final boolean LOG_ON = false;
	private static final String TAG = "FriendService";

	private static List<FriendElement> friends;

	public static List<FriendElement> getFriends(Session session)
			throws IOException {

		friends = new ArrayList<FriendElement>();
		
		List<FriendElement> itens = recoverList(session);
		return itens;
	}

	private static List<FriendElement> recoverList(Session session) {

		Request request = Request.newMyFriendsRequest(
				session,
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {

						for (final GraphUser g : users) {

							// url da imagem do usuario
							String url = "https://graph.facebook.com/"
									+ g.getId().toString() + "/picture?width=" + 40
									+ "&height=" + 40;

							if (LOG_ON) {
								Log.d(TAG, "Friend " + g.getName().toString() + " > "
										+ url);
							}

							friends.add(new FriendElement(null, g.getId()
									.toString(), g.getName().toString()
									.toString(), "noob", url) {
							});

						}

					}
				});
		request.executeAsync();

		if (LOG_ON) {
			Log.d(TAG, friends.size() + " encontrados.");
		}

		return friends;
	}
}