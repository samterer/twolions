package br.com.maboo.node.map;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import br.com.maboo.node.util.DownloadUrl;

// Fetches all places from GooglePlaces AutoComplete Web Service
public class PlacesTask extends AsyncTask<String, Object, String> {

	private ListView atvPlaces;
	private Context context;

	public void setAdapter(Context context, ListView atvPlaces) {
		this.context = context;
		this.atvPlaces = atvPlaces;
	}

	@Override
	protected String doInBackground(String... place) {
		// For storing data from web service
		String data = "";

		// Obtain browser key from https://code.google.com/apis/console
		// essa nao é a chave original, mas funciona
		String key = "key=AIzaSyAhaD4HwgofkA2_9Z7fLbGB1V8Shi-S7do";// +
																	// context.getString(R.string.map_id);

		String input = "";

		try {
			input = "input=" + URLEncoder.encode(place[0], "utf8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// place type to be searched
		String types = "types=geocode";

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = input + "&" + types + "&" + sensor + "&" + key;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
				+ output + "?" + parameters;

		Log.i("PlacesTask", url);

		try {
			// Fetching the data from we service
			data = DownloadUrl.downloadUrl(url);
		} catch (Exception e) {
			Log.d("Background Task", e.toString());
		}
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		// Creating ParserTask
		ParserTask parserTask = new ParserTask();

		// Starting Parsing the JSON string returned by Web Service
		parserTask.execute(result);
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { android.R.id.text1 };

			// Creating a SimpleAdapter for the listView
			SimpleAdapter adapter = new SimpleAdapter(context, result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			atvPlaces.setAdapter(adapter);
		}
	}
}