package br.com.maboo.node.map;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.maboo.node.R;
import br.com.maboo.node.sessao.TelaSessao;

class GetAddressTask extends AsyncTask<Location, Void, Address> {

	private String TAG = "GetAddressTask";

	private Context mContext;
	private View mView;

	// loading progress
	ProgressBar pDialog;
	// text na progress bar
	TextView textProgressBar;

	// info bar do map
	LinearLayout lLayout;
	TextView endPt1;
	TextView endPt2;
	ImageView iconBar;

	// animação
	Animation barUp;
	Animation barDown;

	public GetAddressTask(Context context, View view) {
		super();
		this.mContext = context;
		this.mView = view;

		init();

		// muda tela na sessao
		TelaSessao.TELA = TelaSessao.MAPA_PESQUISA;
	}

	/**
	 * inicializa componentes da barra de informação
	 */
	private void init() {
		// sobe barra de informações do endereço
		lLayout = (LinearLayout) mView.findViewById(R.id.bar_map_info);

		pDialog = (ProgressBar) mView.findViewById(R.id.progressBar);
		textProgressBar = (TextView) mView.findViewById(R.id.textProgressBar);

		endPt1 = (TextView) mView.findViewById(R.id.endPt1);
		endPt2 = (TextView) mView.findViewById(R.id.endPt2);
		iconBar = (ImageView) mView.findViewById(R.id.iconeBar);

		// anime up
		barUp = AnimationUtils.loadAnimation(mContext, R.anim.bar_up);
		barDown = AnimationUtils.loadAnimation(mContext, R.anim.bar_down);

	}

	@Override
	protected void onPreExecute() {
		// Toast.makeText(mContext, "searching...", Toast.LENGTH_LONG).show();

		// verifica se a bar já esta na tela
		if (lLayout.getVisibility() == View.INVISIBLE
				|| lLayout.getVisibility() == View.GONE) { // inicia animação

			lLayout.startAnimation(barUp);
			lLayout.setVisibility(View.VISIBLE);

		} else {

			// esconde os textos
			endPt1.setVisibility(View.INVISIBLE);
			endPt2.setVisibility(View.INVISIBLE);

			// icone da barra
			iconBar.setVisibility(View.INVISIBLE);

			// exibe o progress
			pDialog.setVisibility(View.VISIBLE);
			textProgressBar.setVisibility(View.VISIBLE);
		}

		super.onPreExecute();
	}

	/**
	 * A method that's called once doInBackground() completes. Turn off the
	 * indeterminate activity indicator and set the text of the UI element that
	 * shows the address. If the lookup failed, display the error message.
	 */
	protected void onPostExecute(final Address address) {

		// dismiss the dialog after getting all products
		pDialog.setVisibility(View.GONE);
		textProgressBar.setVisibility(View.GONE);

		// carrega o endereço nos campos
		try {
			drawAddress(address);
		} catch (NullPointerException e) {
		}

		// exibe os textos
		Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {
				// carrega a classe AddressStatic com o endereço da vez
				AddressStatic.address = address;
				// titulo do endereços
				endPt1.setVisibility(View.VISIBLE);
				endPt2.setVisibility(View.VISIBLE);
				// icone da barra
				iconBar.setVisibility(View.VISIBLE);
			}
		};
		handler.postDelayed(r, 350);
	}

	/**
	 * preenche os campos de texto com os devidos endereços
	 * 
	 * @param address
	 */
	private void drawAddress(Address address) {
		// preenche o endereço
		// linha superior do endereço
		String pt1 = "";

		if (address.getThoroughfare() != null) {
			if (address.getLocale().getDisplayName().length() > 0) {
				pt1 = address.getThoroughfare();
			}
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < pt1.length(); i++) {
			if (i < 40) {
				sb.append(pt1.charAt(i));
			} else {
				sb.append("...");
				break;
			}
		}
		pt1 = sb.toString();
		// set address line 1
		endPt1.setText(pt1);

		// linha inferior do endereço
		String pt2 = address.getSubAdminArea() + " - "
				+ address.getCountryName();
		// set address line 2
		endPt2.setText(pt2);
	}

	/**
	 * Get a Geocoder instance, get the latitude and longitude look up the
	 * address, and return it
	 * 
	 * @params params One or more Location objects
	 * @return A string containing the address of the current location, or an
	 *         empty string if no address can be found, or an error message
	 */
	protected Address doInBackground(Location... params) {

		Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

		// Get the current location from the input parameter list
		Location loc = params[0];

		// Create a list to contain the result address
		List<Address> addresses = null;
		try {

			/*
			 * Return 1 address.
			 */
			addresses = geocoder.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);

		} catch (IOException e1) {

			Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
			e1.printStackTrace();

			// return ("IO Exception trying to get address");
			return null;

		} catch (IllegalArgumentException e2) {

			// Error message to post in the log
			String errorString = "Illegal arguments "
					+ Double.toString(loc.getLatitude()) + " , "
					+ Double.toString(loc.getLongitude())
					+ " passed to address service";
			Log.e("LocationSampleActivity", errorString);
			e2.printStackTrace();

			// return errorString;
			return null;
		}
		// If the reverse geocode returned an address
		if (addresses != null && addresses.size() > 0) {

			// Get the first address
			Address address = addresses.get(0);

			// Return the text
			return address;

		} else {

			// return "No address found";
			return null;

		}
	}

}