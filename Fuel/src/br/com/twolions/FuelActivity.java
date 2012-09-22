package br.com.twolions;

import android.os.AsyncTask;
import br.com.core.ActivityCircle;
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
			// Inicia a transção
			task = new TransactionTask(this, transaction, R.string.aguarde);
			task.execute();
		} else {
			// Não existe conexão
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