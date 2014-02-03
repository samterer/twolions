package br.com.maboo.node.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import br.com.maboo.node.R;
import br.com.maboo.node.map.ControllerMap;
import br.com.maboo.node.map.MarkerManager;
import br.com.maboo.node.map.MoveCamera;
import br.com.maboo.node.map.MoveToSearchAddressTask;
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
		SearchView.OnQueryTextListener {

	private String TAG = "FragmentMap";

	private View view;
	private MapView mapView;
	private GoogleMap map;

	/**/
	private static final String IMAGEVIEW_TAG = "Android Logo";
	private android.widget.RelativeLayout.LayoutParams layoutParams;

	/**/
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

	private void init() {

		if (mapView != null) {

			try { // inicializa o maps (conceitos de camera e afins...)
				MapsInitializer.initialize(getActivity());
			} catch (GooglePlayServicesNotAvailableException e) {
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
			new MarkerManager().initPointManager(map, getActivity());

			// habilita o menu no maps
			setHasOptionsMenu(true);

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_map, menu);

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
	@Override
	public boolean onQueryTextChange(String newText) {
		//
		return false;
	}

	@Override
	// pesquisa um endereço
	public boolean onQueryTextSubmit(String query) {
		// AndroidUtils.toast(getActivity().getApplicationContext(), query);

		// esconde o teclado
		hideKeyBoard();

		// move a camera para o ponto no mapa
		new MoveToSearchAddressTask(getActivity(), map).execute(query);

		return false;
	}

	private void hideKeyBoard() {
		// esconde o teclado
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		AndroidUtils.closeVirtualKeyboard(
				getActivity().getApplicationContext(), searchView);
	}
}
