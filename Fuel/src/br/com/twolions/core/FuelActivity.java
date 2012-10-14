package br.com.twolions.core;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import br.com.twolions.R;
import br.com.twolions.sql.SqlScript;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.transaction.TransactionTask;
import br.com.twolions.util.AndroidUtils;

public class FuelActivity extends ActivityCircle {

	private TransactionTask task;

	public static SqlScript sqlScript;

	protected void alert(int menssage) {
		AndroidUtils.alertDialog(this, menssage);
	}

	// Inicia a thread
	public void startTransaction(Transaction transaction) {
		boolean dbOk = AndroidUtils.isConnectionDB(this);
		if (dbOk) {

			// abre base
			sqlScript = new SqlScript(this);

			// Inicia a trans��o
			task = new TransactionTask(this, transaction, R.string.aguarde);
			task.execute();
		} else {
			// N�o existe conex�o
			AndroidUtils.alertDialog(this,
					R.string.erro_conexao_db_indisponivel);
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		if (task != null) {
			boolean executando = task.getStatus().equals(
					AsyncTask.Status.RUNNING);
			if (executando) {
				executando = false;
				task.cancel(true);
				task.closedProgress();
			}
		}

	}

	public void onPause() {
		super.onPause();

		// fecha conexao
		if (sqlScript != null) {
			
			Log.i("base", ""+R.string.fecha_db);
		
			sqlScript.fechar();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("base", ""+R.string.criando_db);
		
		// abre base
		sqlScript = new SqlScript(this);

	}
	
	
	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);
		
		sqlScript = new SqlScript(this);
		
	}

}