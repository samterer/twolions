package br.com.maboo.node.nodemenubeta;

import android.app.Application;
import android.util.Log;
import br.livroandroid.utils.DownloadImagemUtil;

public class FriendsApplication extends Application {
	private static final String TAG = "FriendsApplication";
	// Singleton
	private static FriendsApplication instance = null;
	// Variáveis
	private DownloadImagemUtil downloader;
	public static FriendsApplication getInstance() {
		if (instance == null)
			throw new IllegalStateException("Configure a aplicação no AndroidManifest.xml");
		return instance;
	}
	@Override
	public void onCreate() {
		Log.i(TAG, "ContextApplication.onCreate()");
		downloader = new DownloadImagemUtil(this);
		// Salva a instância para termos acesso como Singleton
		instance = this;
	}
	public DownloadImagemUtil getDownloadImagemUtil() {
		return downloader;
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "ContextApplication.onTerminate()");
		downloader = null;
	}
}
