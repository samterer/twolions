package br.com.maboo.here.core;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import br.com.maboo.here.util.Coordinate;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MapActivityCircle extends ActivityCircle implements LocationListener {

	private MapController controller;

	private MapView mapView;

	private MyLocationOverlay currentLocationOverlay;

	private Location currentLocation;

	private GeoPoint currentGeoPoint;

	private String TAG_LOG = getClass().getSimpleName();

	// Ciclo de vida da atividade
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

	}

	/*********************************************************************************
	 * MAP
	 *********************************************************************************/

	public void onLocationChanged(Location location) {

		setCurrentLocation(location);

		setCurrentGeoPoint(new Coordinate(getCurrentLocation()));

		if (getCurrentGeoPoint() != null) {

			// center map in new position
			getController().animateTo(getCurrentGeoPoint());

		}

		if (getMapView() != null) {

			// repaint screen
			getMapView().invalidate();

		}

		String message = String.format(
				"New Location \n Longitude: %1$s \n Latitude: %2$s",
				location.getLongitude() * 1E6, location.getLatitude() * 1E6);

		Log.i(getClass().getSimpleName(), message);

	}

	protected LocationManager getLocationManager() {

		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	}

	public void onProviderDisabled(String provider) {

		Log.i(getClass().getSimpleName(),
				"Provider disabled by the user. GPS turned off");

		/*
		 * // gps is running? boolean isGPS =
		 * getLocationManager().isProviderEnabled(
		 * LocationManager.GPS_PROVIDER);
		 * 
		 * // internet in air? boolean isNet =
		 * getLocationManager().isProviderEnabled(
		 * LocationManager.NETWORK_PROVIDER);
		 * 
		 * if (!isGPS || !isNet) {
		 * 
		 * // open config gps // open config internet startActivityForResult(new
		 * Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
		 * 0);
		 * 
		 * }
		 */

	}

	public void onProviderEnabled(String provider) {

		Log.i(getClass().getSimpleName(),
				"Provider enabled by the user. GPS turned on");

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {

		Log.i(getClass().getSimpleName(), "Provider status changed.");

	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	/*********************************************************************************
	 * CIRCLE ACTIVITY
	 *********************************************************************************/

	protected void onStart() {
		super.onStart();

		Log.i(getClass().getSimpleName(), getClassName() + ".onStart() called.");
	}

	protected void onRestart() {
		super.onRestart();

		Log.i(getClass().getSimpleName(), getClassName()
				+ ".onRestart() called.");
	}

	protected void onResume() {
		super.onResume();

		// Registra o listener
		if (getCurrentLocationOverlay() != null) {

			getCurrentLocationOverlay().enableMyLocation();

		}

		Log.i(getClass().getSimpleName(), getClassName()
				+ ".onResume() called.");
	}

	protected void onPause() {
		super.onPause();

		// remove o listener
		if (getCurrentLocationOverlay() != null) {
			getCurrentLocationOverlay().disableMyLocation();
		}

		Log.i(getClass().getSimpleName(), getClassName() + ".onPause() called.");
	}

	protected void onStop() {
		super.onStop();
		Log.i(getClass().getSimpleName(), getClassName() + ".onStop() called.");
	}

	protected void onDestroy() {
		super.onDestroy();

		// remove o listener for not running in background
		getLocationManager().removeUpdates(this);

		Log.i(getClass().getSimpleName(), getClassName()
				+ ".onDestroy() called.");
	}

	/*********************************************************************************
	 * GET \ SET
	 *********************************************************************************/

	public String getTAG_LOG() {
		return TAG_LOG;
	}

	public void setTAG_LOG(String tAG_LOG) {
		TAG_LOG = tAG_LOG;
	}

	public MapController getController() {
		return controller;
	}

	public void setController(MapController controller) {
		this.controller = controller;
	}

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	public MyLocationOverlay getCurrentLocationOverlay() {
		return currentLocationOverlay;
	}

	public void setCurrentLocationOverlay(
			MyLocationOverlay currentLocationOverlay) {
		this.currentLocationOverlay = currentLocationOverlay;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	public GeoPoint getCurrentGeoPoint() {
		return currentGeoPoint;
	}

	public void setCurrentGeoPoint(GeoPoint currentGeoPoint) {
		this.currentGeoPoint = currentGeoPoint;
	}
	
}
