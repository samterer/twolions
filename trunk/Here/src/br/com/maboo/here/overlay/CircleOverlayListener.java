package br.com.maboo.here.overlay;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import br.com.maboo.here.util.Coordinate;

/**
 * Simples Overlay que recebe as coordenadas Latitude e Longitude
 * 
 * e desenha um círculo
 * 
 * @author ricardo
 * 
 */
public class CircleOverlayListener extends CircleOverlay implements
		LocationListener {

	public CircleOverlayListener(GeoPoint geoPoint, int cor) {
		super(geoPoint, cor);

	}

	public void onLocationChanged(Location location) {
		// Atualiza a coordenada do CirculoOverlay
		setGeoPoint(new Coordinate(location));

	}

	public void onProviderDisabled(String s) {
	}

	public void onProviderEnabled(String s) {
	}

	public void onStatusChanged(String s, int i, Bundle bundle) {
	}
}
