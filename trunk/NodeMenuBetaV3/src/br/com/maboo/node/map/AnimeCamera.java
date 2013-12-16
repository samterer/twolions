package br.com.maboo.node.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Responsavel pela animacoes da camera
 * 'transicoes de um ponto para o outro'
 * @author jeff
 *
 */
public class AnimeCamera {

	public AnimeCamera(GoogleMap map, LatLng latLng) {
		// Construct a CameraPosition focusing on Mountain View and animate
		// the camera to that position.
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latLng) // Sets the center of the map to Mountain
								// View
				.zoom(17) // Sets the zoom
				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		map.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

}
