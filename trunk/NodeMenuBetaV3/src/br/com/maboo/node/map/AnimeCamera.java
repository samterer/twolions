package br.com.maboo.node.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Responsavel pela animacoes da camera 'transicoes de um ponto para o outro'
 * 
 * @author jeff
 * 
 */
public class AnimeCamera {

	private GoogleMap map;
	private LatLng latLng;

	public AnimeCamera(GoogleMap map, LatLng latLng) {

		this.map = map;
		this.latLng = latLng;

	}

	public void moveCamera() {
		if (map != null) {
			// Move the camera instantly to Sydney with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
		}
	}


}
