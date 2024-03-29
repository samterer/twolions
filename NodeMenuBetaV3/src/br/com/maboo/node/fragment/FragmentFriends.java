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
		OnItemClickListener, SearchView.OnQueryTextListener,
		SearchView.OnCloseListener {

	private String TAG = "FragmentFriends";

	private ArrayList<FriendElement> requestFriend;
	private ListFriendAdapter adapter;

	private ListView listview_log;

	private View view;

	// private int TAM_LIST = 400;

	private String URL_FACE = "http://www.facebook.com/";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fragmenttab2.xml
		view = inflater.inflate(R.layout.fragmentfriends, container, false);

		// instance of list_friend
		listview_log = (ListView) view.findViewById(R.id.list_friend);
		listview_log.setOnItemClickListener(this);

		try {
			carregaLista();
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
	private void carregaLista() {
		// Log.i(TAG, "randomList...");

		requestFriend = new ArrayList<FriendElement>();

		// tamanho total da lista
		int TAM_LIST = ListFriendElement.friends.size();

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

			// Pass results to ListMapAddrAdapter Class
			adapter = new ListFriendAdapter(getActivity(), requestFriend);

			// Binds the Adapter to the ListView
			listview_log.setAdapter(adapter);

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
		SearchView searchField = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchField.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));

		// listener
		searchField.setOnQueryTextListener(this);

		// menu
		this.menu = menu;

		return;
	}

	/*******************************************************************************
	 * search methods
	 *******************************************************************************/
	@Override
	public boolean onQueryTextChange(String newText) {

		Log.i(TAG, "onQueryTextChange");

		try {

			if (TextUtils.isEmpty(newText)) {
				// adapter.getFilter().filter("");
				Log.i(TAG, "onQueryTextChange Empty String");

				// limpa filtro
				listview_log.clearTextFilter();
				adapter.notifyDataSetChanged();

				adapter.clearAdapter();

				// recarrega a lista
				carregaLista();

				hideKeyBoard();

				// Log.i(TAG, "onQueryTextChange " + newText.toString());

			} else {
				Log.i(TAG, "onQueryTextChange " + newText.toString());

				adapter.getFilter().filter(newText.toString());
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	// pesquisa um endere�o
	public boolean onQueryTextSubmit(String query) {
		// AndroidUtils.toast(getActivity().getApplicationContext(), query);

		Log.i(TAG, ">> search name: " + query);

		// esconde o teclado
		hideKeyBoard();

		// devolve uma lista apenas com o item pesquisado (itens relacionados)
		adapter.getFilter().filter(query.toString());
		adapter.notifyDataSetChanged();

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

	/*
	 * close list
	 */
	public boolean onClose() {
		Log.i(TAG, "onClose");
		return false;
	}
}
