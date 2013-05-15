package br.com.maboo.here.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ZoomOverlay extends Overlay {
	private GestureDetector gestureDetector = null;

	/**
	 * Public constructor. Creates and initializes a new instance.
	 * 
	 * @param context
	 *            of this application.
	 * @param geoPoint
	 */
	public ZoomOverlay(Context context, MapView mapView) {
		gestureDetector = new GestureDetector(context,
				new ZoomOnGestureListener(mapView));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
		return gestureDetector.onTouchEvent(motionEvent);
	}
}
