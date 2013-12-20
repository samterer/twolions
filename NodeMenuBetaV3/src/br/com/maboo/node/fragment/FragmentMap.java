package br.com.maboo.node.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.maboo.node.map.AnimeCamera;
import br.com.maboo.node.map.ControllerMap;
import br.com.maboo.node.map.GeoPointManager;
import br.com.maboo.node.map.ManagerClickOnMarker;
import br.livroandroid.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidbegin.menuviewpagertutorial.ChatActivity;
import com.androidbegin.menuviewpagertutorial.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class FragmentMap extends SherlockFragment {

	private String TAG = "FragmentMap";

	private View v;
	private MapView mapView;
	private GoogleMap map;

	private TextView mTapText;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// inflat and return the layout
		v = inflater.inflate(R.layout.map_layout, container, false);

		// get map
		mapView = (MapView) v.findViewById(R.id.map);
		// get header map
		mTapText = (TextView) v.findViewById(R.id.text);
		// instancia o mapView
		mapView.onCreate(savedInstanceState);

		init();

		return v;
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

			// controler do maps
			new ControllerMap(map);

			// inicializa a classe interna que controla os "controles" do maps
			GeoPointManager geo = new GeoPointManager(getActivity());
			geo.initPointManager(map);

			/*********************
			 * click em um markers
			 *********************/
			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				public void onInfoWindowClick(Marker marker) {

					ManagerClickOnMarker mco = new ManagerClickOnMarker(
							getActivity(), ChatActivity.class);
					mco.goTo("local", marker.getTitle());

				}

			});

			map.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					AndroidUtils.toast(getActivity().getApplicationContext(),
							"zoom, here: " + marker.getTitle());

					new AnimeCamera(map, new LatLng(
							marker.getPosition().latitude,
							marker.getPosition().longitude)).moveCamera();
					return false;
				}
			});

			/*********************
			 * double click em um markers
			 *********************/

			// habilita o menu no maps
			setHasOptionsMenu(true);

		}

	}

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

	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	/*******************************************************************************
	 * menu
	 *******************************************************************************/

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_map, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.item1:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;

		case R.id.item2:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		}
		return true;
	}
}
