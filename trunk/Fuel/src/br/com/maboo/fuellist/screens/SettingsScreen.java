package br.com.maboo.fuellist.screens;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import br.com.maboo.fuellist.R;

public class SettingsScreen extends PreferenceActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.edit_settings);

	}

}
