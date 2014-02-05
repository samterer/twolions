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
	private int ZOOM = 18;

	/*******************************************************************************
	 * move a camera automaticamente para a posição recebida 'zoom' padrao =
	 * attr ZOOM se o usuario passar o valor 0, seta o zoom padrao
	 *******************************************************************************/
	public MoveCamera(GoogleMap map, LatLng latLng, int zoomRequest) {

		this.map = map;

		// casos especificos de zoom
		// como no caso da pesquisa por um endereço
		if (zoomRequest > 0 && zoomRequest < ZOOM) {
			ZOOM = zoomRequest;
		}

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
