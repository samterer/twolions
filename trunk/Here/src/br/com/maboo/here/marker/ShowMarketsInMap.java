package br.com.maboo.here.marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.com.maboo.here.R;
import br.com.maboo.here.dao.VectorMarketsForTest;
import br.com.maboo.here.overlay.ImagesOverlay;
import br.com.maboo.here.overlay.MarketOverlayItem;

import com.google.android.maps.MapView;

public class ShowMarketsInMap {

	private Vector<Market> vMarket;

	// private List<OverlayItem> overlays;
	private List<MarketOverlayItem> overlays;

	private Drawable dMarcMarket = null;

	private Context context;

	private MapView mapView;

	public ShowMarketsInMap(Context context, MapView mapView) {

		this.context = context;

		this.mapView = mapView;

		beginMarcMarketInMap();

	}

	private void beginMarcMarketInMap() {

		initOverlaysMarket();

		createFakeBase();

		configOverlayItem();

		showOverlaysInMap();

	}

	private void initOverlaysMarket() {

		overlays = new ArrayList<MarketOverlayItem>();
	}

	private void createFakeBase() {
		// inicializable base
		VectorMarketsForTest baseFake;
		baseFake = new VectorMarketsForTest();

		// list from fake base
		vMarket = baseFake.getvListaMarket();
	}

	private void configOverlayItem() {

		// gerate the list overlays

		Vector<Market> aux = vMarket;
		for (int i = 0; i < vMarket.size(); i++) {
			Market m = (Market) aux.elementAt(i);

			// overlays.add(new MarketOverlayItem(new GeoPoint(m.getLatitude(),
			// m.getLongitude()), m.getNome(), "ALERTA"));

			overlays.add(new MarketOverlayItem(m));
		}

	}

	private void showOverlaysInMap() {

		// define marc of market in map
		dMarcMarket = this.context.getResources().getDrawable(R.drawable.marc);

		// show overlay
		ImagesOverlay marketsOverlay = new ImagesOverlay(this.context,
				overlays, dMarcMarket);

		// add overlay to map
		this.mapView.getOverlays().add(marketsOverlay);
	}
}
