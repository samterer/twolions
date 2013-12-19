package br.com.maboo.node.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import br.com.maboo.node.map.ControllerMap;
import br.com.maboo.node.map.GeoPointManager;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.scrumptious.auxiliar.FaceUserVO;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class FragmentMap extends SherlockFragment implements OnMapClickListener {

	private String TAG = "FragmentMap";

	private View v;
	private MapView mapView;

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

	public void init() {

		if (mapView != null) {

			try { // inicializa o maps (conceitos de camera e afins...)
				MapsInitializer.initialize(getActivity());
			} catch (GooglePlayServicesNotAvailableException e) {
				e.printStackTrace();
			}

			// instacia o maps no formato completo 'GoogleMap'
			GoogleMap map = mapView.getMap();

			// controler do maps
			new ControllerMap(map);

			// inicializa a classe interna que controla os "controles" do maps
			GeoPointManager geo = new GeoPointManager(getActivity());
			geo.initPointManager(map);

			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				public void onInfoWindowClick(Marker marker) {

					Toast.makeText(getActivity().getApplicationContext(),
							"Bem vindo ao "+marker.getTitle()+" "+FaceUserVO.user_name, Toast.LENGTH_SHORT).show();

				}

			});

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

	@Override
	public void onMapClick(LatLng point) {
		Toast.makeText(getActivity().getApplicationContext(), "teste...",
				Toast.LENGTH_SHORT).show();

	}

}
