package br.com.maboo.node.map;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * class que controla os controles
 * 
 * @author jeff
 * 
 */
public class ControllerMap {

	GoogleMap map;

	public ControllerMap(GoogleMap map) {
		this.map = map;
	}

	/*******************************************************************************
	 * cria o bt que leva o mapa até o usuario quando clicado (ou mantem o bt
	 * escondido)
	 *******************************************************************************/
	public void setUserPosition(boolean value) {

		// habilita o gps para encontrar a posiçãoa tual do usuario
		map.setMyLocationEnabled(value);

		// esconde o bt de my location
		map.getUiSettings().setMyLocationButtonEnabled(false);
	}

	/*******************************************************************************
	 * move a camera para a posição do usuario
	 *******************************************************************************/
	public void setCameraUserPosition() {

		Location location = map.getMyLocation();

		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());

		new MoveCamera(map, latLng, 0);
	}

}
