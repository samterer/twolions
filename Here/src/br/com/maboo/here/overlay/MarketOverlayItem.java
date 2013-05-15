package br.com.maboo.here.overlay;

import br.com.maboo.here.marker.Market;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MarketOverlayItem extends OverlayItem {

	private Market market;

	public MarketOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
		// TODO Auto-generated constructor stub
	}

	public MarketOverlayItem(Market market) {
		super(new GeoPoint(market.getLatitude(), market.getLongitude()), market
				.getNome(), "ALERTA");

		this.market = market;
	}

	public Market getMarket() {
		return market;
	}

}
