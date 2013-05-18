package br.com.maboo.here.screen;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import br.com.maboo.here.R;

public class MapScreen extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_map);
	}
}