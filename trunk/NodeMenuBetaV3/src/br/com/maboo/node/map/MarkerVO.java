package br.com.maboo.node.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Pontos do mapa
 * 
 * @author Usuario
 * 
 */
public class MarkerVO {

	private LatLng latLng;
	private int idGeoPoint;
	private String desc;

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public int getIdGeoPoint() {
		return idGeoPoint;
	}

	public void setIdGeoPoint(int idGeoPoint) {
		this.idGeoPoint = idGeoPoint;
	}

	public MarkerVO(LatLng latLng, int idGeoPoint, String desc) {
		this.idGeoPoint = idGeoPoint;
		this.latLng = latLng;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
