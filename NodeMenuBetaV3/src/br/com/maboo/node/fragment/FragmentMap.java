package br.com.maboo.node.fragment;

import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.maboo.node.R;
import br.com.maboo.node.map.ControllerMap;
import br.com.maboo.node.map.MarkerManager;
import br.com.maboo.node.map.MoveCamera;
import br.com.maboo.node.map.PlacesTask;
import br.livroandroid.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class FragmentMap extends SherlockFragment implements
		OnItemClickListener, SearchView.OnQueryTextListener,
		SearchView.OnCloseListener {

	private String TAG = "FragmentMap";

	private View view;
	private MapView mapView;
	private GoogleMap map;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// inflat and return the layout
		view = inflater.inflate(R.layout.fragmentmap, container, false);

		// get map
		mapView = (MapView) view.findViewById(R.id.map);

		// instancia o mapView
		mapView.onCreate(savedInstanceState);

		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	/**
	 * verifica cliques em itens da lista de autoSearch
	 */
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {

		// When clicked, show a toast with the TextView text
		Toast.makeText(getActivity().getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();

		// endereco por extenso
		moveCameraParaEndereco((String) ((TextView) view).getText());
	}

	/**
	 * move a camera para o endereço digitado
	 * 
	 * @param endereco
	 */
	private void moveCameraParaEndereco(String endereco) {

		// convert o endereco em lat e lon e move a camera para o ponto
		Geocoder coder = new Geocoder(getActivity().getApplicationContext());
		List<Address> address;
		Address loc;
		try {
			address = coder.getFromLocationName(endereco, 5);
			if (address == null) {
				return;
			}
			loc = address.get(0);

			// direciona o usuario para o local
			new MoveCamera(map, new LatLng(loc.getLatitude(),
					loc.getLongitude()), 0);

			// esconde o teclado
			hideKeyBoard();

			// limpa lista de enderecos
			limpaLista();

			// esconde o searchView
			onClose();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {

		if (mapView != null) {

			try { // inicializa o maps (conceitos de camera e afins...)
				MapsInitializer.initialize(getActivity());
			} catch (GooglePlayServicesNotAvailableException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			// instacia o maps no formato completo 'GoogleMap'
			map = mapView.getMap();

			// visualização inicial do mapa (estilo satelite)
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

			// controler do maps (icones sobre o mapa)
			new ControllerMap(map).setUserPosition(true);

			// inicializa a classe interna responsavel por criar os nodes na
			// tela, assim como itens dinamicos (icone do usuario)
			// mando a view para provaveis animacoes
			new MarkerManager().initPointManager(map, getActivity(), view);

			// habilita a barra
			Handler handler = new Handler();
			final Runnable r = new Runnable() {
				public void run() {
					// exibe o maps
					mapView.setVisibility(View.VISIBLE);
					// habilita o menu no maps
					setHasOptionsMenu(true);
				}
			};
			handler.postDelayed(r, 350);

		}

	}

	/*******************************************************************************
	 * default services
	 *******************************************************************************/

	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/*******************************************************************************
	 * gerencia a bateria
	 *******************************************************************************/

	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	/*******************************************************************************
	 * menu (action bar)
	 *******************************************************************************/
	// utilizado unicamente apos o submit de uma pesquisa
	private Menu menu;
	private SearchView searchView;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_map, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getActivity()
				.getSystemService(Context.SEARCH_SERVICE);

		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));

		// listener
		searchView.setOnQueryTextListener(this);

		this.menu = menu;

		return;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		// Location user
		case R.id.action_location_found:

			Location location = map.getMyLocation();

			new MoveCamera(map, new LatLng(location.getLatitude(),
					location.getLongitude()), 0);

			break;
		// Normal style map
		case R.id.item1:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			break;
		// Satellite style map
		case R.id.item2:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

			break;
		}
		return true;
	}

	/*******************************************************************************
	 * search methods
	 *******************************************************************************/
	/**
	 * prepara a lista onde o endereços serão carregados
	 */
	private ListView list_addr;

	/**
	 * inicializa a lista de endereços
	 */
	private void initList() {
		list_addr = (ListView) view.findViewById(R.id.list_addr);
		list_addr.setOnItemClickListener(this);

		list_addr.setVisibility(View.VISIBLE);
	}

	/**
	 * Limpa a lista de enderecos e a fecha, escondendo o teclado
	 */
	private void limpaLista() {
		// limpa o filtro de pesquisa
		list_addr.clearTextFilter();

		// esconde a view da list
		list_addr.setVisibility(View.GONE);

		// esconde o teclado
		hideKeyBoard();
	}

	/*
	 * recebe o texto que esta sendo inserido em tempo real
	 */
	@Override
	public boolean onQueryTextChange(String newText) {

		Log.i(TAG, "onQueryTextChange");

		try {

			if (TextUtils.isEmpty(newText)) {
				// adapter.getFilter().filter("");
				Log.i(TAG, "onQueryTextChange Empty String");

				// limpa a lista de enderecos
				limpaLista();

				// Log.i(TAG, "onQueryTextChange " + newText.toString());

			} else {
				Log.i(TAG, "onQueryTextChange " + newText.toString());

				// abre a lista de endereço
				initList();

				PlacesTask placesTask = new PlacesTask();

				// adapter
				placesTask.setAdapter(getActivity().getApplicationContext(),
						list_addr);

				// execute
				placesTask.execute(newText);

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * pesquisa pelo endereço digitado
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		// AndroidUtils.toast(getActivity().getApplicationContext(), query);

		Log.i(TAG, ">> search name: " + query);

		// endereco por extenso
		moveCameraParaEndereco(query);

		// esconde o teclado
		hideKeyBoard();

		return false;
	}

	/*
	 * esconde o teclado
	 */
	private void hideKeyBoard() {
		// fecha teclado
		AndroidUtils.closeVirtualKeyboard(
				getActivity().getApplicationContext(), searchView);
	}

	/*
	 * close list
	 */
	public boolean onClose() {
		Log.i(TAG, "onClose");

		if (searchView != null) {
			searchView.clearFocus();
		}

		return false;
	}
}
