package br.com.twolions.core;

import android.os.AsyncTask;
import br.com.core.ActivityCircle;
import br.com.twolions.R;
import br.com.twolions.R.string;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.transaction.TransactionTask;
import br.com.utils.AndroidUtils;

public class FuelActivity extends ActivityCircle {
	private TransactionTask task;

	protected void alert(int menssage) {
		AndroidUtils.alertDialog(this, menssage);
	}

	// Inicia a thread
	public void startTransaction(Transaction transaction) {
		boolean dbOk = AndroidUtils.isConnectionDB(this);
		if (dbOk) {
			// Inicia a trans��o
			task = new TransactionTask(this, transaction, R.string.aguarde);
			task.execute();
		} else {
			// N�o existe conex�o
			AndroidUtils.alertDialog(this,
					R.string.erro_conexao_db_indisponivel);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (task != null) {
			boolean executando = task.getStatus().equals(
					AsyncTask.Status.RUNNING);
			if (executando) {
				task.cancel(true);
				task.closedProgress();
			}
		}
	}
}