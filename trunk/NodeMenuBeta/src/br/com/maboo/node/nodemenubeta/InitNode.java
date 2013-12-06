package br.com.maboo.node.nodemenubeta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import br.com.maboo.node.nodemenubeta.util.Util;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.scrumptious.LogonActivity;

public class InitNode extends Activity {

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
			// start the home screen if the back
			// button wasn't pressed
			// already
			Intent intent = new Intent(this, LogonActivity.class);

			startActivity(intent);
		}

	}

}
