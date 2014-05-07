package br.com.maboo.imageedit.core;

import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.R.string;
import br.com.maboo.imageedit.util.Utils;
import android.app.Activity;
import android.os.AsyncTask;

public class ActivityCircle extends Activity {

	private TransactionTask task;

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

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	protected void alert(String menssage) {
		Utils.alertDialog(this, menssage);
	}

	// Inicia a thread
	public void startTransaction(Transaction transaction) {
		// Inicia a transção
		task = new TransactionTask(this, transaction, R.string.m_wait);
		task.execute();
	}

}
