package br.com.maboo.node.map;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/*******************************************************************************
 * pesquisa um lugar passando o endereço por extenso 1- se o lugar for
 * encontrado a camera é direcionada para o ponto 2- se nao for encontrado,
 * é devolvida uma msg pro usuario
 *******************************************************************************/
// An AsyncTask class for accessing the GeoCoding Web Service
public class MoveToSearchAddressTask extends AsyncTask<String, Void, List<Address>> {

	private Activity act;
	private GoogleMap map;

	public MoveToSearchAddressTask(Activity act, GoogleMap map) {
		this.act = act;
		this.map = map;
	}

	@Override
	protected List<Address> doInBackground(String... locationName) {
		// Creating an instance of Geocoder class
		Geocoder geocoder = new Geocoder(act.getBaseContext());
		List<Address> addresses = null;

		try {
			// Getting a maximum of 3 Address that matches the input text
			addresses = geocoder.getFromLocationName(locationName[0], 3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addresses;
	}

	@Override
	protected void onPostExecute(List<Address> addresses) {

		if (addresses == null || addresses.size() == 0) {
			Toast.makeText(act.getBaseContext(), "No Location found",
					Toast.LENGTH_SHORT).show();
		}

		// Clears all the existing markers on the map
		map.clear();

		// Adding Markers on Google Map for each matching address
		for (int i = 0; i < addresses.size(); i++) {

			Address address = (Address) addresses.get(i);

			// Creating an instance of GeoPoint, to display in Google Map
			// latLng = new LatLng(address.getLatitude(),
			// address.getLongitude());

			new MoveCamera(map, new LatLng(address.getLatitude(),
					address.getLongitude()), 14);

			String addressText = String.format("%s, %s", address
					.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0)
					: "", address.getCountryName());

			// markerOptions = new MarkerOptions();
			// markerOptions.position(latLng);
			// markerOptions.title(addressText);

			// googleMap.addMarker(markerOptions);

			// Locate the first location
			// if(i==0)
			// googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		}
	}
}
