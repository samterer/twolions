package br.com.maboo.neext.core;

import android.os.Bundle;
import android.util.Log;
import br.com.maboo.neext.R;
import br.com.maboo.neext.sql.SqlScript;

public class FormItemActivity extends ActivityCircle {

	public static SqlScript sqlScript;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// abre base
		sqlScript = new SqlScript(this);

	}

	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		// fecha conexao
		if (sqlScript != null) {
			Log.i("base", this.getString(R.string.a_f_db));

			sqlScript.fechar(); // fecha a base
		}
	}

	protected void onResume() {
		super.onResume();

		if (sqlScript == null) {

			Log.i("base", this.getString(R.string.a_c_db));

			sqlScript = new SqlScript(this); // abre a base
		}
	}

}
