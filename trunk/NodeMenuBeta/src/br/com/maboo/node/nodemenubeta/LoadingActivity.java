package br.com.maboo.node.nodemenubeta;

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
import br.com.maboo.node.nodemenubeta.util.Util;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.scrumptious.FacebookLoginActivity;

public class LoadingActivity extends Activity {

	// Splash screen timer
	private int SPLASH_TIME_OUT = 1000;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// verifica a internet e o gps
		// se um dos dois não estiver ok o app será fechado
		if (!Util.isVerify(this)) {

			AlertDialog dialog = new AlertDialog.Builder(this)
					.setTitle(this.getString(R.string.app_name))
					.setMessage("Ative o gps, e verifique a conexão com a internet.")
					.create();

			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					finish();

				}
			});

			dialog.show();
		} else {
			
			try {
				PackageInfo info = getPackageManager().getPackageInfo("com.eatapp", PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
				    MessageDigest md = MessageDigest.getInstance("SHA");
				    md.update(signature.toByteArray());
				    Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
				} catch (NameNotFoundException e) {

				} catch (NoSuchAlgorithmException e) {

				}
			
			// start the home screen if the back
			// button wasn't pressed
			// already
			Intent intent = new Intent(this, FacebookLoginActivity.class);

			startActivity(intent);
		}

	}

}
