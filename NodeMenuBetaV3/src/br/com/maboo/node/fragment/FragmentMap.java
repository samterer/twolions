package br.com.maboo.node.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.maboo.node.R;
import br.com.maboo.node.chat.ChatActivity;
import br.com.maboo.node.map.AnimeCamera;
import br.com.maboo.node.map.ControllerMap;
import br.com.maboo.node.map.GeoPointManager;
import br.com.maboo.node.map.ManagerClickOnMarker;
import br.livroandroid.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class FragmentMap extends SherlockFragment {

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

		// onClick no ponto para criação de um chat
		// depois, envia o view do ponto para uma classe que vai gerenciar ele
		ImageView point = (ImageView) view.findViewById(R.id.point_maker);

		point.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "TESTANDOOO!", Toast.LENGTH_SHORT)
						.show();

			}
		});

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
	 * maker (ponto para criar locais)
	 *******************************************************************************/

	public void moveMaker(View v) {
		AndroidUtils.toast(getActivity().getApplicationContext(),
				"click on maker...");
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
