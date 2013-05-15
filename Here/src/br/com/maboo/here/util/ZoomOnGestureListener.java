package br.com.maboo.here.util;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ZoomOnGestureListener extends SimpleOnGestureListener implements
		OnTouchListener {

	private MapView mapView = null;

	/**
	 * Constructor.
	 * 
	 * @param mapView
	 *            reference to the map view.
	 */
	public ZoomOnGestureListener(MapView mapView) {

		setMapView(mapView);

		configZoom();

	}

	/*******************************************************************
	 * CONFIG ZOOM
	 *******************************************************************/

	// default config zoom
	private void configZoom() {

		// zoom
		this.mapView.getController().setZoom(19);

		// control of zoom in scree
		this.mapView.setBuiltInZoomControls(true);

	}

	/*******************************************************************
	 * TOUCH \ TAP
	 *******************************************************************/

	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_POINTER_DOWN:

			Log.i("appLog", "ACTION_POINTER_DOWN");

			break;
		case MotionEvent.ACTION_DOWN:

			Log.i("appLog", "ACTION_DOWN");

			break;
		case MotionEvent.ACTION_MOVE:

			Log.i("appLog", "ACTION_MOVE");

			break;
		}

		return false;
	}

	// verirify double tap
	public boolean onDoubleTap(MotionEvent e) {

		// get position of tap
		getCurrentPositionTap(e);

		return true;
	}

	/*******************************************************************
	 * PREPARE ZOOM
	 *******************************************************************/

	// get position of tap in screen and convert this with point for center map
	public void getCurrentPositionTap(MotionEvent event) {

		GeoPoint point_touch = null;
		point_touch = mapView.getProjection().fromPixels((int) event.getX(),
				(int) event.getY());

		// Log.i("appLog", "Lattitude=" + point_touch.getLatitudeE6() / 1E6 +
		// " Longitude=" + point_touch.getLongitudeE6() / 1E6);

		// center map in point
		this.mapView.getController().animateTo(point_touch);

		// zoomin in point of tap
		ZoomInPositionTap();

	}

	// relize zoom in and verifiry proximity of zoom for hide or show markets
	public void ZoomInPositionTap() {

		// Zoom in
		mapView.getController().zoomIn();

		verifyZoom();

	}

	/*******************************************************************
	 * VISIBILITY
	 *******************************************************************/

	// verify with show of hide markets
	private void verifyZoom() {

		new VerifyShowMarkets(getMapView());

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
