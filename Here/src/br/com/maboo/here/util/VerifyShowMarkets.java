package br.com.maboo.here.util;

import br.com.maboo.here.overlay.ImagesOverlay;

public class VerifyShowMarkets {

	private MapView mapView;

	public VerifyShowMarkets(MapView mapView) {

		// Log.i("appLog", "verify visibility");

		setMapView(mapView);

		verifyZoom(getMapView().getZoomLevel());

	}

	// verifiry distance of zoom for show or hide markets
	public void verifyZoom(int zoomLevel) {

		if (zoomLevel >= 20) {

			showOrHideMarkets(true);

		} else {

			showOrHideMarkets(false);

		}

	}

	// show or hide markets
	public void showOrHideMarkets(boolean showOverlays) {

		for (Overlay overlay : getMapView().getOverlays()) {
			if (overlay instanceof ImagesOverlay) {
				((ImagesOverlay) overlay).setVisible(showOverlays);
			}
		}

	}

	/*******************************************
	 * GET\SET
	 *******************************************/

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

}
