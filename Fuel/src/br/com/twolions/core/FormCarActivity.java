package br.com.twolions.core;

import android.os.Bundle;
import android.util.Log;
import br.com.twolions.sql.SqlScript;

public class FormCarActivity extends ActivityCircle {

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
			Log.i("base", "fechando conexão com o db...");
			sqlScript.fechar();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

			Log.i("base", "criando nova conexão com o db...");
			// abre base
			sqlScript = new SqlScript(this);
	}


}
