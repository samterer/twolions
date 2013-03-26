package br.com.maboo.neext.core;

import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import br.com.maboo.neext.R;
import br.com.maboo.neext.sql.SqlScript;
import br.com.maboo.neext.transaction.Transaction;
import br.com.maboo.neext.transaction.TransactionTask;
import br.com.maboo.neext.util.AndroidUtils;

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

	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		try {

			// abre base
			sqlScript = new SqlScript(this);

		} catch (SQLException e) {
			e.printStackTrace();

			// Não existe conexão
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

		}

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

		boolean dbOk = AndroidUtils.isConnectionDB(this); // verificação de teste apenas

		if (dbOk) {

			// abre base
			sqlScript = new SqlScript(this);

			// Inicia a transção
			task = new TransactionTask(this, transaction, R.string.m_wait);
			task.execute();

		} else {

			// Não existe conexão
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");
		}
	}

}