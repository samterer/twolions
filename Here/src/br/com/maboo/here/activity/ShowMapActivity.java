package br.com.maboo.here.activity;



import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import br.com.maboo.here.R;
import br.com.maboo.here.core.MapActivityCircle;
import br.com.maboo.here.marker.ShowPoints;
import br.com.maboo.here.util.Coordinate;
import br.com.maboo.here.util.ZoomOverlay;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class ShowMapActivity extends MapActivityCircle {

	
	private Context context;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		this.context = getApplicationContext();
		
		// recupera o mapa
		setContentView(R.layout.animate);

		// recebe o mapa
		if(getMapView() == null) {
			setMapView(((MapView) findViewById(R.id.map)));
		}
		
		// map controller
		if(getController() == null) {
			setController(getMapView().getController());
		}
		
		// marca o ponto onde esta o dispositivo
		mapBrandWithCircle();

		// config zoom
		confZoom();

		// prepare markets in map
		prepareMarktInMap();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {

				// centraliza mapa no ultimo ponto conhecido
				centerMap();

			}
		}, 1500);
		
	}
	
	/*********************************************************************************
	 * ZOOM IN DOUBLE CLICK SCREEN
	 *********************************************************************************/

	// configura o zoom do mapa
	private void confZoom() {

		// control of zoom
		getMapView().getOverlays().add(
				new ZoomOverlay(getBaseContext(), getMapView()));

	}
	
	/*********************************************************************************
	 * RECOVER AND CENTER MAP
	 *********************************************************************************/

	private void centerMap() {

		// map in the last location know
		setCurrentLocation(getLocationManager().getLastKnownLocation(
				LocationManager.GPS_PROVIDER));

		// convert the last location to GeoPoint
		if (getCurrentLocation() != null) {

			Log.i(getTAG_LOG(), "centralizing map");

			setCurrentGeoPoint(new Coordinate(getCurrentLocation()));

			// center map in last postion know
			getController().setCenter(getCurrentGeoPoint());

		}

		getLocationManager().requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	private void mapBrandWithCircle() {

		setCurrentLocationOverlay(new MyLocationOverlay(this, getMapView()));

		getCurrentLocationOverlay().runOnFirstFix(new Runnable() {
			public void run() {
				Log.i(getTAG_LOG(), "MyOverlay runOnFirstFix: "
						+ getCurrentLocationOverlay().getMyLocation());
			}
		});

		// add overlay to map
		getMapView().getOverlays().add(getCurrentLocationOverlay());

	}

	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);

	}

	public void prepareMarktInMap() {
		// show markets in map
		new ShowPoints(this, getMapView());
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	

	
}
