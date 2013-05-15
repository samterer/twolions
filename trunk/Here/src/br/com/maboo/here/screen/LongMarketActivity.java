package br.com.maboo.here.screen;


import android.os.Bundle;
import android.widget.TextView;
import br.com.maboo.here.R;
import br.com.maboo.here.core.ActivityCircle;

public class LongMarketActivity extends ActivityCircle {

	// private Market market;

	// public LongMarketActivity(Market market) {
	// this.market = market;
	// }

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mountScreen();

		inserDataInScreen();

	}

	private void mountScreen() {

		setContentView(R.layout.layout_long_screen_market);

	}

	private void inserDataInScreen() {

		TextView nameMarket = (TextView) findViewById(R.id.namemarket);

		nameMarket.setText("TESTE");

		// if (market != null) {
		//
		// nameMarket.setText(market.getNome());

		// } else {

		// Log.i("appLog", "market esta null");

		// }

	}

}