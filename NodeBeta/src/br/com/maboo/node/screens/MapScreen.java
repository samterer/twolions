package br.com.maboo.node.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.maboo.node.FragmentCircle;
import br.com.maboo.node.R;

import com.google.android.gms.maps.MapView;

public class MapScreen extends FragmentCircle {

	MapView m;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// inflat and return the layout
		View v = inflater.inflate(R.layout.map_layout, container, false);

		m = (MapView) v.findViewById(R.id.map);

		m.onCreate(savedInstanceState);
		
		m. getMap().setMyLocationEnabled(true);

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		m.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		m.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		m.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		m.onLowMemory();
	}

}