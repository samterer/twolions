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
public class MoveCamera {

	private GoogleMap map;
	private int ZOOM = 19;

	public MoveCamera(GoogleMap map) {
		this.map = map;
	}

	/*******************************************************************************
	 * move a camera automaticamente para a posição recebida
	 *******************************************************************************/
	public MoveCamera(GoogleMap map, LatLng latLng) {

		this.map = map;

		moveCamera(latLng);
	}

	/*******************************************************************************
	 * Manda a camera para uma posição especifica no map
	 *******************************************************************************/
	public void moveCamera(LatLng latLng) {
		if (map != null) {
			// Move the camera instantly to Sydney with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
		}
	}

}
