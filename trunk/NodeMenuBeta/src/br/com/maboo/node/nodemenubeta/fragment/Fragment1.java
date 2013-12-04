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

public class Fragment1 extends SherlockFragment implements OnMapClickListener,
		OnMapLongClickListener {

	private MapView m;

	// private TextView mTapTextView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// inflat and return the layout
		View v = inflater.inflate(R.layout.map_layout, container, false);
		m = (MapView) v.findViewById(R.id.map);
		m.onCreate(savedInstanceState);

		// mTapTextView = (TextView) findViewById(R.id.tap_text);

		initMap();
		setListener();

		return v;
	}

	public void initMap() {

		// bt my location
		m.getMap().setMyLocationEnabled(true);
	}

	private void setListener() // If the setUpMapIfNeeded(); is needed then...
	{
		m.getMap().setMyLocationEnabled(true);
	}

	public void onResume() {
		super.onResume();
		m.onResume();
	}

	public void onPause() {
		super.onPause();
		m.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
		m.onDestroy();
	}

	public void onLowMemory() {
		super.onLowMemory();
		m.onLowMemory();
	}

	public void onMapClick(LatLng point) {
		// mTapTextView.setText("tapped, point=" + point);
	}

	public void onMapLongClick(LatLng point) {
		// mTapTextView.setText("long pressed, point=" + point);
	}

}
