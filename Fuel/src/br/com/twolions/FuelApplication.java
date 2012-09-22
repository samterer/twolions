package br.com.twolions;

import android.app.Application;
import android.util.Log;

public class FuelApplication extends Application {
	private static final String TAG = "appLog";
	// Singleton
	private static FuelApplication instance = null;

	public static FuelApplication getInstance() {
		if (instance == null)
			throw new IllegalStateException(
					"Configure a aplicação no AndroidManifest.xml");
		return instance;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "ContextApplication.onCreate()");
		// Salva a instância para termos acesso como Singleton
		instance = this;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "ContextApplication.onTerminate()");
	}
}
