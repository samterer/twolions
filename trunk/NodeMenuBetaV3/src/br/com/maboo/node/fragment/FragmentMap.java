package br.com.maboo.node.fragment;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
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
		OnItemClickListener {

	private String TAG = "FragmentMap";

	private View view;
	private MapView mapView;
	private GoogleMap map;

	/**
	 * 
	 * */
	private static final String IMAGEVIEW_TAG = "Android Logo";
	private android.widget.RelativeLayout.LayoutParams layoutParams;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// inflat and return the layout
		view = inflater.inflate(R.layout.fragmentmap, container, false);

		// get map
		mapView = (MapView) view.findViewById(R.id.map);

		// instancia o mapView
		mapView.onCreate(savedInstanceState);

		// habilita o menu no maps
		setHasOptionsMenu(true);

		// autocomplete
		autoComplete();

		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		String str = (String) adapterView.getItemAtPosition(position);
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();

		// move a camera para o ponto no mapa
		// new MoveCameraToAddressTask(getActivity(), map).execute(query);
	}

	/**
	 * pesquisa endereço automaticamente (tempo real)
	 */
	private AutoCompleteTextView atvPlaces;

	private void autoComplete() {
		atvPlaces = (AutoCompleteTextView) view.findViewById(R.id.autocomplete);
		atvPlaces.setThreshold(1);

		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				PlacesTask placesTask = new PlacesTask();

				// adapter
				placesTask.setAdapter(getActivity(), atvPlaces);

				// execute
				placesTask.execute(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
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

	private void hideKeyBoard() {
		// esconde o teclado
		AndroidUtils.closeVirtualKeyboard(
				getActivity().getApplicationContext(), atvPlaces);
	}

}
