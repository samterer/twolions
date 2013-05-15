package br.com.maboo.here.screen;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;
import br.com.maboo.here.R;
import br.com.maboo.here.activity.LongMarketActivity;
import br.com.maboo.here.activity.ShowMapActivity;

public class ShowMapScreen extends ShowMapActivity{

	public ShowMapScreen() {
		super();

		// preapre buttons to animate
		prepareButtons();

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
	
	public ViewFlipper flipper;
	
	public void prepareButtons() {
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
	
}
