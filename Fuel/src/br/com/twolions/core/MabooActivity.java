package br.com.twolions.core;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import br.com.twolions.R;
import br.com.twolions.sql.SqlScript;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.transaction.TransactionTask;
import br.com.twolions.util.AndroidUtils;
import br.com.twolions.util.Constants;

public class MabooActivity extends ActivityCircle {

	private TransactionTask task;

	public static SqlScript sqlScript;

	protected void alert(int menssage) {

		AndroidUtils.alertDialog(this, menssage,
				this.getString(R.string.app_name));

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
					R.string.erro_conexao_db_indisponivel, "" + R.string.t_e_c);
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

			Log.i(Constants.LOG_BASE, this.getString(R.string.a_f_db));

			sqlScript.fechar();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i(Constants.LOG_BASE, this.getString(R.string.a_c_db));

		// abre base
		sqlScript = new SqlScript(this);

	}

	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		sqlScript = new SqlScript(this);

	}

}