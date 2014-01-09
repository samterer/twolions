package br.com.maboo.node.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragment;

public class SecondClickOnMarker extends SherlockFragment {
	
	private String TAG = "ManagerClickOnMarker";
	
	private Activity activity;
	private Class<?> cls;
	private Intent intent;
	
	// instancia a class
	public SecondClickOnMarker(Activity activity, Class<?> cls) {
		this.activity = activity;
		this.cls = cls;
		
		// cria intent
		intent = new Intent(activity,
				cls);
	}

	/*******************************************************************************
	 * go to cls, without attrs
	 *******************************************************************************/
	public void goTo() {
		// cria intent
		start();
	}
	
	/*******************************************************************************
	 * go to cls, with attrs
	 * type string
	 *******************************************************************************/
	public void goTo(String key, String value) {
		Bundle bundle = new Bundle();
		bundle.putString(key, value);
		
		intent.putExtras(bundle);
		
		start();
	}
	
	/*******************************************************************************
	 * executa a intent 
	 *******************************************************************************/
	private void start(){
		try{
			activity.startActivity(intent);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.i(TAG,"sucesso.");
		}
		
	}
}
