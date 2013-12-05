package br.com.maboo.node.nodemenubeta.transaction;

import android.os.AsyncTask;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;


public class TransactionCircle extends SherlockFragment {

	private TransactionTask task;

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	public void onDestroy() {
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

	// Inicia a thread
	public void startTransaction(Transaction transaction) {

		boolean dbOk = true; // verifica internet e gps

		if (dbOk) {

			// Inicia a transção
			task = new TransactionTask(this.getActivity(), transaction,
					R.string.wait);
			task.execute();

		} else {

			Toast.makeText(this.getActivity(),
					"Sorry, please... soooorry. And now, re-start the app.",
					Toast.LENGTH_SHORT).show();

		}
	}

}
