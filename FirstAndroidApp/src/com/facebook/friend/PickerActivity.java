package com.facebook.friend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.facebook.FacebookException;
import com.facebook.application.ScrumptiousApplication;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;
import com.firstandroidapp.R;

public class PickerActivity extends FragmentActivity {

	public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
	public static final Uri PLACE_PICKER = Uri.parse("picker://place");

	private FriendPickerFragment friendPickerFragment;
	private PlacePickerFragment placePickerFragment;

	private LocationListener locationListener;

	private static final Location SAN_FRANCISCO_LOCATION = new Location("") {
		{
			setLatitude(37.7750);
			setLongitude(-122.4183);
		}
	};

	private static final int SEARCH_RADIUS_METERS = 1000;
	private static final int SEARCH_RESULT_LIMIT = 50;
	private static final String SEARCH_TEXT = "restaurant";
	private static final int LOCATION_CHANGE_THRESHOLD = 50; // meters

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pickers);

		Bundle args = getIntent().getExtras();
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragmentToShow = null;
		Uri intentUri = getIntent().getData();

		if (FRIEND_PICKER.equals(intentUri)) {
			if (savedInstanceState == null) {
				friendPickerFragment = new FriendPickerFragment(args);
			} else {
				friendPickerFragment = (FriendPickerFragment) manager
						.findFragmentById(R.id.picker_fragment);
			}
			// Set the listener to handle errors
			friendPickerFragment
					.setOnErrorListener(new PickerFragment.OnErrorListener() {
						@Override
						public void onError(PickerFragment<?> fragment,
								FacebookException error) {
							PickerActivity.this.onError(error);
						}
					});
			// Set the listener to handle button clicks
			friendPickerFragment
					.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
						@Override
						public void onDoneButtonClicked(
								PickerFragment<?> fragment) {
							finishActivity();
						}
					});
			fragmentToShow = friendPickerFragment;

		} else if (PLACE_PICKER.equals(intentUri)) {
			if (savedInstanceState == null) {
				placePickerFragment = new PlacePickerFragment(args);
			} else {
				placePickerFragment = (PlacePickerFragment) manager
						.findFragmentById(R.id.picker_fragment);
			}
			placePickerFragment
					.setOnSelectionChangedListener(new PickerFragment.OnSelectionChangedListener() {
						@Override
						public void onSelectionChanged(
								PickerFragment<?> fragment) {
							finishActivity(); // call finish since you can only
												// pick one place
						}
					});
			placePickerFragment
					.setOnErrorListener(new PickerFragment.OnErrorListener() {
						@Override
						public void onError(PickerFragment<?> fragment,
								FacebookException error) {
							PickerActivity.this.onError(error);
						}
					});
			placePickerFragment
					.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
						@Override
						public void onDoneButtonClicked(
								PickerFragment<?> fragment) {
							finishActivity();
						}
					});
			fragmentToShow = placePickerFragment;
		} else {
			// Nothing to do, finish
			setResult(RESULT_CANCELED);
			finish();
			return;
		}

		manager.beginTransaction()
				.replace(R.id.picker_fragment, fragmentToShow).commit();
	}

	private void onError(Exception error) {
		onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean finishActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.error_dialog_title)
				.setMessage(error)
				.setPositiveButton(R.string.error_dialog_button_text,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								if (finishActivity) {
									finishActivity();
								}
							}
						});
		builder.show();
	}

	private void finishActivity() {
		ScrumptiousApplication app = (ScrumptiousApplication) getApplication();
		if (FRIEND_PICKER.equals(getIntent().getData())) {
			if (friendPickerFragment != null) {
				app.setSelectedUsers(friendPickerFragment.getSelection());
			}
		} else if (PLACE_PICKER.equals(getIntent().getData())) {
			if (placePickerFragment != null) {
				app.setSelectedPlace(placePickerFragment.getSelection());
			}
		}
		setResult(RESULT_OK, null);
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (FRIEND_PICKER.equals(getIntent().getData())) {
			try {
				friendPickerFragment.loadData(false);
			} catch (Exception ex) {
				onError(ex);
			}
		} else if (PLACE_PICKER.equals(getIntent().getData())) {
			try {
				Location location = null;
				// Instantiate the default criteria for a location provider
				Criteria criteria = new Criteria();
				// Get a location manager from the system services
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				// Get the location provider that best matches the criteria
				String bestProvider = locationManager.getBestProvider(criteria,
						false);
				if (bestProvider != null) {
					// Get the user's last known location
					location = locationManager
							.getLastKnownLocation(bestProvider);
					if (locationManager.isProviderEnabled(bestProvider)
							&& locationListener == null) {
						// Set up a location listener if one is not already set
						// up
						// and the selected provider is enabled
						locationListener = new LocationListener() {
							@Override
							public void onLocationChanged(Location location) {
								// On location updates, compare the current
								// location to the desired location set in the
								// place picker
								float distance = location
										.distanceTo(placePickerFragment
												.getLocation());
								if (distance >= LOCATION_CHANGE_THRESHOLD) {
									placePickerFragment.setLocation(location);
									placePickerFragment.loadData(true);
								}
							}

							@Override
							public void onStatusChanged(String s, int i,
									Bundle bundle) {
							}

							@Override
							public void onProviderEnabled(String s) {
							}

							@Override
							public void onProviderDisabled(String s) {
							}
						};
						locationManager.requestLocationUpdates(bestProvider, 1,
								LOCATION_CHANGE_THRESHOLD, locationListener,
								Looper.getMainLooper());
					}
				}
				if (location == null) {
					// Set the default location if there is no
					// initial location
					String model = Build.MODEL;
					if (model.equals("sdk") || model.equals("google_sdk")
							|| model.contains("x86")) {
						// This may be the emulator, use the default location
						location = SAN_FRANCISCO_LOCATION;
					}
				}
				if (location != null) {
					// Configure the place picker: search center, radius,
					// query, and maximum results.
					placePickerFragment.setLocation(location);
					placePickerFragment.setRadiusInMeters(SEARCH_RADIUS_METERS);
					placePickerFragment.setSearchText(SEARCH_TEXT);
					placePickerFragment.setResultsLimit(SEARCH_RESULT_LIMIT);
					// Start the API call
					placePickerFragment.loadData(true);
				} else {
					// If no location found, show an error
					onError(getResources()
							.getString(R.string.no_location_error), true);
				}
			} catch (Exception ex) {
				onError(ex);
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (locationListener != null) {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Remove updates for the location listener
			locationManager.removeUpdates(locationListener);
			locationListener = null;
		}
	}

}
