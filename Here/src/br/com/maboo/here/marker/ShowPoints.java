package br.com.maboo.here.marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import br.com.maboo.here.R;
import br.com.maboo.here.dao.VectorMarketsForTest;
import br.com.maboo.here.overlay.ImagesOverlay;
import br.com.maboo.here.overlay.PointOverlayItem;

import com.google.android.maps.MapView;

public class ShowPoints {

	private Vector<Point> vPoint;

	// private List<OverlayItem> overlays;
	private List<PointOverlayItem> overlays;

	private Drawable dMarcMarket = null;

	private Context context;

	private MapView mapView;

	public ShowPoints(Context context, MapView mapView) {

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

		overlays = new ArrayList<PointOverlayItem>();
	}

	private void createFakeBase() {
		// inicializable base
		VectorMarketsForTest baseFake;
		baseFake = new VectorMarketsForTest();

		// list from fake base
		vPoint = baseFake.getvListaMarket();
	}

	private void configOverlayItem() {

		// gerate the list overlays

		Vector<Point> aux = vPoint;
		for (int i = 0; i < vPoint.size(); i++) {
			Point m = (Point) aux.elementAt(i);

			overlays.add(new PointOverlayItem(m));
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
