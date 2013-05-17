package br.com.maboo.here.util;



import android.content.Intent;
import android.os.Bundle;
import br.com.maboo.here.R;
import br.com.maboo.here.core.ActivityCircle;
import br.com.maboo.here.screen.MapScreen;

public class InitHereActivity extends ActivityCircle implements
		OnCallHomeScreenListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5816997025901886681L;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		boolean networkOk = AndroidUtils.isNetWorkAvailable(this);

		if (networkOk) {
			try {

				// map
				finish();

				startActivity(new Intent(this, MapScreen.class));

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			AndroidUtils alert = new AndroidUtils(this);

			alert.alertDialog(this, R.string.erro_conexao_indisponivel);

		}

	}

	public void onExecute() {

		// mata a activity
		finish();

	}
}
