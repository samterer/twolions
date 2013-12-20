package br.com.maboo.node.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
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

			// Zoom in, animating the camera.
			//map.animateCamera(CameraUpdateFactory.zoomIn());

			// Zoom out to zoom level 10, animating with a duration of 2 seconds.
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		}
	}

	public void moveAnimeCamera() {
		if (map != null) {
			// Construct a CameraPosition focusing on Mountain View and animate
			// the camera to that position.
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(latLng) // Sets the center of the map
					.zoom(14) // Sets the zoom
					.bearing(90) // Sets the orientation of the camera to east
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}
	}

}
