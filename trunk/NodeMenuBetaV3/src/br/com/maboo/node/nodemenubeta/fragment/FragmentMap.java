package br.com.maboo.node.nodemenubeta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

public class FragmentMap extends SherlockFragment implements
		OnMapClickListener, OnMapLongClickListener {

	private View v;
	private MapView mapView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// inflat and return the layout
		v = inflater.inflate(R.layout.map_layout, container, false);
		mapView = (MapView) v.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);

		initMap();

		return v;
	}

	public void initMap() {

		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mapView == null) {

		} else
		// Check if we were successful in obtaining the map.
		if (mapView != null) {
			// bt my location
			mapView.getMap().setMyLocationEnabled(true);
			
			mapView.getMap().isMyLocationEnabled();

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

	public void onMapClick(LatLng point) {
		// mTapTextView.setText("tapped, point=" + point);
	}

	public void onMapLongClick(LatLng point) {
		// mTapTextView.setText("long pressed, point=" + point);
	}

}
