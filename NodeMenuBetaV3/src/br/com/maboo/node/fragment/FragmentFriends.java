package br.com.maboo.node.fragment;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import br.com.maboo.node.R;
import br.com.maboo.node.adapter.ListFriendAdapter;
import br.livroandroid.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.facebook.friend.FriendElement;
import com.facebook.friend.ListFriendElement;

public class FragmentFriends extends SherlockFragment implements
		OnItemClickListener, SearchView.OnQueryTextListener {

	private String TAG = "FragmentFriends";

	private ArrayList<FriendElement> requestFriend;

	private ListView listview_log;

	private View view;

	private int TAM_LIST = 400;

	private String URL_FACE = "http://www.facebook.com/";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fragmenttab2.xml
		view = inflater.inflate(R.layout.fragmentfriends, container, false);

		// instance of list_friend
		listview_log = (ListView) view.findViewById(R.id.list_friend);
		listview_log.setOnItemClickListener(this);

		try {
			chargeList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// habilita o menu no maps
		setHasOptionsMenu(true);

		return view;
	}

	/*******************************************************************************
	 * abre a pagina do facebook (profile do usuario clicado)
	 * 
	 * @param id
	 *******************************************************************************/
	private void sendToProfilePage(String id) {
		String url = URL_FACE + id;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	/*******************************************************************************
	 * random list (gera uma lista randomica de 10 amigos do usuario logado)
	 *******************************************************************************/
	private void chargeList() {
		// Log.i(TAG, "randomList...");

		requestFriend = new ArrayList<FriendElement>();

		// tamanho total da lista
		TAM_LIST = ListFriendElement.friends.size();

		for (int i = 0; i < TAM_LIST; i++) {

			FriendElement b = ListFriendElement.friends.get(i);

			requestFriend.add(b);
		}

		atualizarView();
	}

	/*******************************************************************************
	 * atualiza a lista com os dados dentro de requestFriend
	 *******************************************************************************/
	public void atualizarView() {
		// Log.i(TAG, "atualizarView...");

		// atualiza os friends na thread principal
		if (requestFriend != null && requestFriend.size() > 0) {
			listview_log.setAdapter(new ListFriendAdapter(getActivity(),
					requestFriend));

			// troca o bg já que agora a lista esta populada
			ImageView img = (ImageView) view.findViewById(R.id.image1);
			img.setVisibility(View.INVISIBLE);
		}
	}

	/*******************************************************************************
	 * click em algum usuario da list
	 *******************************************************************************/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		FriendElement f = (FriendElement) parent.getAdapter().getItem(posicao);

		sendToProfilePage(f.getId().toString());
	}

	/*******************************************************************************
	 * menu (action bar)
	 *******************************************************************************/
	// utilizado unicamente apos o submit de uma pesquisa
	private Menu menu;
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_friends, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getActivity()
				.getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));

		// listener
		searchView.setOnQueryTextListener(this);
		
		this.menu = menu;

		return;
	}

	/*******************************************************************************
	 * search methods
	 *******************************************************************************/
	private String first;

	@Override
	public boolean onQueryTextChange(String newText) {

		Log.i(TAG, "onQueryTextChange");

		if (TextUtils.isEmpty(newText)) {
			((ListFriendAdapter) listview_log.getAdapter()).getFilter().filter(
					"");
			Log.i(TAG, "onQueryTextChange Empty String");
			listview_log.clearTextFilter();
		} else {
			Log.i(TAG, "onQueryTextChange " + newText.toString());
			((ListFriendAdapter) listview_log.getAdapter()).getFilter().filter(
					newText.toString());
		}
		return true;
	}

	@Override
	// pesquisa um endereço
	public boolean onQueryTextSubmit(String query) {
		// AndroidUtils.toast(getActivity().getApplicationContext(), query);

		Log.i(TAG, ">> search name: " + query);

		// esconde o teclado
		hideKeyBoard();

		// for (FriendElement f : requestFriend) {
		// Log.i(TAG, "f: "+f.getNome());
		// }

		return false;
	}

	/*
	 * esconde o teclado
	 */
	private void hideKeyBoard() {
		// esconde o teclado
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		AndroidUtils.closeVirtualKeyboard(
				getActivity().getApplicationContext(), searchView);
	}

	public boolean onClose() {
		Log.i(TAG, "onClose");
		return false;
	}
}
