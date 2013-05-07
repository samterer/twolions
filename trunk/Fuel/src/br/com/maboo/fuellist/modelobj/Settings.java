package br.com.maboo.fuellist.modelobj;

import android.content.SharedPreferences;
import android.util.Log;

public class Settings {

	// obter valores
	// moeda
	protected String moeda;

	// distancia
	protected String dist;

	// volume de medida (ex:litro...etc)
	protected String volume;

	public String getMoeda() {
		return moeda;
	}

	protected void setMoeda(String moeda) {
		this.moeda = moeda;
	}

	public String getDist() {
		return dist;
	}

	protected void setDist(String dist) {
		this.dist = dist;
	}

	public String getVolume() {
		return volume;
	}

	protected void setVolume(String volume) {
		this.volume = volume;
	}

	public Settings(SharedPreferences sharedPrefs) {
		Log.i("appLog", "## charge SharedPreferences ##");

		setMoeda(sharedPrefs.getString("pref_currency", "$"));

		setDist(sharedPrefs.getString("pref_distance", "mi"));

		setVolume(sharedPrefs.getString("pref_volume", "US gal"));

		// teste do settings
		// Log.i("appLog", "## moeda = " + getMoeda() + " ##");

		// Log.i("appLog", "## volume = " + getVolume() + " ##");

		// Log.i("appLog", "## distancia = " + getDist() + " ##");
	}
}
