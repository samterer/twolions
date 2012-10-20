package br.com.twolions.core;

import android.os.Bundle;
import android.util.Log;
import br.com.twolions.R;
import br.com.twolions.sql.SqlScript;

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
			sqlScript.fechar();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (sqlScript == null) {
			Log.i("base", this.getString(R.string.a_c_db));
			// abre base
			sqlScript = new SqlScript(this);
		}
	}

}
