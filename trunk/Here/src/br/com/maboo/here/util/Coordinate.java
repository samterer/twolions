package br.com.maboo.here.util;

import android.location.Address;
import android.location.Location;

/**
 * Ajuda a construir um GeoPoint
 * 
 * @author ricardo
 * 
 */
public class Coordinate extends GeoPoint {
	// valores em graus * 1E6 (microdegrees)
	public Coordinate(int latitudeE6, int longitudeE6) {
		super(latitudeE6, longitudeE6);
	}

	// Converte para "graus * 1E6"
	public Coordinate(double latitude, double longitude) {
		this((int) (latitude * 1E6), (int) (longitude * 1E6));
	}

	// Cria baseado no objeto 'Location' diretamente recebido do GPS
	public Coordinate(Location location) {
		this(location.getLatitude(), location.getLongitude());
	}

	// Cria baseado em um endereço
	public Coordinate(Address endereco) {
		this(endereco.getLatitude(), endereco.getLongitude());
	}

	@Override
	public int getLatitudeE6() {
		return super.getLatitudeE6();
	}

	@Override
	public int getLongitudeE6() {
		return super.getLongitudeE6();
	}

	public double getLatitude() {
		return super.getLatitudeE6() / 1E6;
	}

	public double getLongitude() {
		return super.getLongitudeE6() / 1E6;
	}
}
