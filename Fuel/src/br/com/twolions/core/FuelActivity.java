package br.com.twolions.core;

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

			// Inicia a transção
			task = new TransactionTask(this, transaction, R.string.aguarde);
			task.execute();
		} else {
			// Não existe conexão
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
			Log.i("base", "fechando conexão com o db...");
			sqlScript.fechar();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// if (sqlScript == null) {
		Log.i("base", "criando nova conexão com o db...");
		// abre base
		sqlScript = new SqlScript(this);
		// }

	}

}