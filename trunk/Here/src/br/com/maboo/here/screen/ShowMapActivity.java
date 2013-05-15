package br.com.maboo.here.screen;


import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.ViewFlipper;
import br.com.maboo.here.R;
import br.com.maboo.here.core.MapActivityCircle;
import br.com.maboo.here.marker.ShowMarketsInMap;
import br.com.maboo.here.util.Coordinate;
import br.com.maboo.here.util.ZoomOverlay;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class ShowMapActivity extends MapActivityCircle {

	ViewFlipper flipper;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		init();

	}

	/*********************************************************************************
	 * OPEN MAP
	 *********************************************************************************/

	public void init() {

		// recupera o mapa
		recoverMap();

		// inicializa o controler e demais variaveis
		initController();

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

		// preapre buttons to animate
		prepareButtons();

	}

	/*********************************************************************************
	 * RECOVER AND CENTER MAP
	 *********************************************************************************/

	private void recoverMap() {

		// setContentView(R.layout.layout_map);

		setContentView(R.layout.animate);

		// recebe o mapa
		setMapView(((MapView) findViewById(R.id.map)));

	}

	private void initController() {

		// map controller
		setController(getMapView().getController());

	}

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
		new ShowMarketsInMap(this, getMapView());
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
	 * MENU
	 *********************************************************************************/

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.layout_optionmenu, menu);

		return true;
	}

	public boolean onMenuItemSelected(int featuredId, MenuItem item) {

		// Log.i(getTAG_LOG(), "onMenuItemClick.");

		int itemSelec = item.getItemId();

		switch (itemSelec) {
		case R.id.iconabout:

			// screen with information about me and my office
			// Log.i(getTAG_LOG(),"screen with information about me and my office.");

			about();

			return true;
		case R.id.iconrefresh:

			// refresh all information from web and gps connection
			// Log.i(getTAG_LOG(),"refresh all information from web and gps connection.");

			refresh();

			return true;
		case R.id.iconexit:

			// quit app
			// Log.i(getTAG_LOG(), "quit app.");

			quit();

			return true;
		}

		return false;
	}

	private void about() {

		// Toast.makeText(this, "open about", Toast.LENGTH_SHORT).show();

		// startActivity(new Intent(getMapView().getContext(),
		// AboutActivity.class));

		flipper.setInAnimation(inFromRightAnimation());
		flipper.setOutAnimation(outToLeftAnimation());
		flipper.showNext();

	}

	private void refresh() {

		// centerMap();
		startActivity(new Intent(this, LongMarketActivity.class));
	}

	private void quit() {

		finish();

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		// Log.i(getTAG_LOG(), "item.getItemId(): " + item.getItemId());

		switch (item.getItemId()) {
		case R.id.streetview:

			getMapView().setStreetView(true);

			getMapView().setSatellite(false);

			return true;

		case R.id.satelliteview:

			getMapView().setSatellite(true);

			getMapView().setStreetView(false);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*********************************************************************************
	 * ANIMATE
	 *********************************************************************************/
	private void prepareButtons() {
		flipper = (ViewFlipper) findViewById(R.id.flipper);

		TextView text = (TextView) findViewById(R.id.textPri);

		text.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				flipper.setInAnimation(inFromLeftAnimation());
				flipper.setOutAnimation(outToRightAnimation());
				flipper.showPrevious();
			}
		});
	}

	private Animation inFromRightAnimation() {

		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

}
