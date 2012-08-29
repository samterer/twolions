package br.com.twolions.screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.twolions.R;

public class ListCarActivity extends Activity {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mountScreen();

	}

	private void mountScreen() {

		setContentView(R.layout.list_car);

		// get font
		// Typeface tf =
		// Typeface.createFromAsset(getAssets(),"DroidSansFallback.ttf");
		// insert font in title of bar
		// TextView tv = (TextView) findViewById(R.id.title_bar);

		// tv.setTypeface(tf);

	}

	// TESTS

	public void helpAboutReport(View v) {

		ImageView t_report = (ImageView) findViewById(R.id.t_report);
		t_report.setVisibility(View.GONE);

		ImageView t_select_vehicle = (ImageView) findViewById(R.id.t_select_vehicle);
		t_select_vehicle.setVisibility(View.VISIBLE);

		Toast.makeText(this, "help about report(single car)",
				Toast.LENGTH_SHORT).show();

	}

	public void helpAboutSelectVehicle(View v) {

		ImageView t_report = (ImageView) findViewById(R.id.t_report);
		t_report.setVisibility(View.VISIBLE);

		ImageView t_select_vehicle = (ImageView) findViewById(R.id.t_select_vehicle);
		t_select_vehicle.setVisibility(View.GONE);

		Toast.makeText(this, "help about selection of vehicles",
				Toast.LENGTH_SHORT).show();

	}

	public void help(View v) {
		ImageView bt_help = (ImageView) findViewById(R.id.bt_help);
		bt_help.setVisibility(View.GONE);

		ImageView bt_select_vehicle = (ImageView) findViewById(R.id.bt_select_vehicle);
		bt_select_vehicle.setVisibility(View.VISIBLE);

		Toast.makeText(this, "go to help screen", Toast.LENGTH_SHORT).show();
	}

	public void backToSelectBehicle(View v) {

		ImageView bt_help = (ImageView) findViewById(R.id.bt_help);
		bt_help.setVisibility(View.VISIBLE);

		ImageView bt_select_vehicle = (ImageView) findViewById(R.id.bt_select_vehicle);
		bt_select_vehicle.setVisibility(View.GONE);

		Toast.makeText(this, "back to vehicle list", Toast.LENGTH_SHORT).show();

	}

	public void addNewRegister(View v) {

		Toast.makeText(this, "add new register", Toast.LENGTH_SHORT).show();

	}
}
