package br.com.maboo.here.overlay;

import br.com.maboo.here.marker.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PointOverlayItem extends OverlayItem {

	private Point market;

	public PointOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
		// TODO Auto-generated constructor stub
	}

	public PointOverlayItem(Point market) {
		super(new GeoPoint(market.getLatitude(), market.getLongitude()), market
				.getNome(), "ALERTA");

		this.market = market;
	}

	public Point getMarket() {
		return market;
	}

}
