package br.com.maboo.node.map;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 *  classe que controla os pontos no mapa
 * @author jeff
 *
 */
public class GeoPoint {

	// posicao inicial do mapa
	public GeoPoint(final GoogleMap map) {

		map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

			@Override
			public void onMyLocationChange(Location location) {

				map.addMarker(new MarkerOptions().position(
						new LatLng(location.getLatitude(), location
								.getLongitude())).title("It's Me!"));

				LatLng latLng = new LatLng(location.getLatitude(), location
						.getLongitude());

				// lança posicao inicial
				new AnimeCamera(map, latLng);

			}
		});

	}
}

