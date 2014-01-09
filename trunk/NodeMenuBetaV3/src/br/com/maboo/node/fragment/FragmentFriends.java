package br.com.maboo.node.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.maboo.node.R;
import br.com.maboo.node.adapter.ListFriendAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.friend.FriendElement;
import com.facebook.friend.ListFriendElement;
import com.facebook.model.GraphUser;

public class FragmentFriends extends SherlockFragment implements OnItemClickListener {

	private String TAG = "FragmentFriend";

	//private List<FriendElement> friends;
	//private List<GraphUser> temp;
	private ArrayList<FriendElement> itensRandom;

	private ListView listview_log;

	private View view;
	
	private int TAM_LIST = 400;

	//private String URL_GRAPH = "https://graph.facebook.com/";
	//private String FRIEND_SIZE_IMG = "/picture?width=90&height=90";
	private String URL_FACE = "http://www.facebook.com/";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fragmenttab2.xml
		view = inflater.inflate(R.layout.fragmentfriends, container, false);

		listview_log = (ListView) view.findViewById(R.id.list_friend);
		listview_log.setOnItemClickListener(this);

		try {
		//	executar();
		
			randomList();
			atualizarView();
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	/**
	 * abre a pagina do facebook (profile do usuario clicado)
	 * 
	 * @param id
	 */
	private void sendToProfilePage(String id) {
		String url = URL_FACE + id;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	/**
	 * carrega a lista de friends
	 *//*
	public void executar() {
		try {

			itensRandom = new ArrayList<FriendElement>();
		//	friends = new ArrayList<FriendElement>();

			ListFriendElement.friends = new ArrayList<FriendElement>();
			
			recoverList();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	/**
	 * Recupera lista de amigos do face
	 */
/*	private void recoverList() {

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

	}*/

	/**
	 * Popula lista de amigos do node (padrao FriendElement)
	 *//*
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

		randomList();
		atualizarView();

	}*/

	/**
	 * random list (gera uma lista randomica de 10 amigos do usuario logado)
	 */
	private void randomList() {
		// Log.i(TAG, "randomList...");
		
		itensRandom = new ArrayList<FriendElement>();
		
		// tamanho total da lista
		TAM_LIST = ListFriendElement.friends.size();

		// a lista de amigos usada tera apenas 10 pessoas conhecidas,s empre de
		// maneira randomica
		// Collections.shuffle(ListFriendElement.friends);

		for (int i = 0; i < TAM_LIST; i++) {

			FriendElement b = ListFriendElement.friends.get(i);

			itensRandom.add(b);
		}
	}

	public void atualizarView() {
		// Log.i(TAG, "atualizarView...");

		// atualiza os friends na thread principal
		if (itensRandom != null && itensRandom.size() > 0) {
			listview_log.setAdapter(new ListFriendAdapter(getActivity(),
					itensRandom));

			// troca o bg j� que agora a lista esta populada
			ImageView img = (ImageView) view.findViewById(R.id.image1);
			img.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		FriendElement f = (FriendElement) parent.getAdapter().getItem(posicao);

		sendToProfilePage(f.getId().toString());
	}
}
