package br.com.maboo.neext.core;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import br.com.maboo.neext.R;
import br.com.maboo.neext.sql.SqlScript;
import br.com.maboo.neext.transaction.Transaction;
import br.com.maboo.neext.transaction.TransactionTask;
import br.com.maboo.neext.util.AndroidUtils;
import br.com.maboo.neext.util.Constants;

public class NeextActivity extends ActivityCircle {

	private TransactionTask task;

	public static SqlScript sqlScript;

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	protected void onDestroy() {
		super.onDestroy();

		if (task != null) {

			boolean executando = task.getStatus().equals(
					AsyncTask.Status.RUNNING);

			if (executando) {

				executando = false;

				task.cancel(true);
				task.closedProgress();
				
				update();

			}
		}

	}
	
	public void update() {
		
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

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

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
			task = new TransactionTask(this, transaction, R.string.m_wait);
			task.execute();

		} else {

			// Não existe conexão
			AndroidUtils.alertDialog(this,
					"Bad, bad app! No donut for you. Please, re-start.");
		}
	}

}