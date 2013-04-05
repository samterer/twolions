package br.com.twolions.screens;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import br.com.twolions.R;
import br.com.twolions.interfaces.InterfaceBar;

public class SettingsScreen extends PreferenceActivity implements InterfaceBar {

	//obter valores
		//boolean vibrar = prefs.getBoolean(getString(key), true);

	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.edit_set);
            //setContentView(R.layout.form_set);
    }


	public void btBarRight(View v) {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();  
		editor.commit();  
		
		//sai da tela de configurações.
		finish();
	}

	public void onBackPressed() { // call my backbutton pressed method when
		super.onBackPressed();
	}
	
	public void organizeBt() {
		// TODO Auto-generated method stub
		
	}


	public void btBarLeft(View v) {
		// TODO Auto-generated method stub
		
	}

}
