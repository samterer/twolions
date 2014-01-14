package br.com.maboo.node;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import br.com.maboo.node.util.Util;
import br.livroandroid.utils.AndroidUtils;

import com.facebook.scrumptious.FacebookLoginActivity;

public class LoadingActivity extends Activity {

	// Splash screen timer
	private int SPLASH_TIME_OUT = 1000;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// esconde a actionBar
		getActionBar().hide();

		// verifica a internet e o gps
		// se um dos dois não estiver ok o app será fechado
		if (!Util.isVerify(getApplicationContext())) {

			AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle(this.getString(R.string.app_name))
					.setMessage(
							"Ative o gps, e verifique a conexão com a internet.")
					.create();

			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					// onDestroy();

					finish();

				}
			});

			dialog.show();
		} else {

			try {
				PackageInfo info = getPackageManager().getPackageInfo(
						"br.com.maboo.node", PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA1");
					md.update(signature.toByteArray());
					Log.e("MY KEY HASH:",
							Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			// start the home screen if the back
			// button wasn't pressed
			// already
			Intent intent = new Intent(this, FacebookLoginActivity.class);

			startActivity(intent);

			// fecha essa activity, e a pilha de activitys começa
			// na proxima
			finish();
		}

	}

}
