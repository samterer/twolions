package br.com.maboo.node.nodemenubeta.transaction;

import android.os.AsyncTask;
import android.util.Log;
import br.com.maboo.node.nodemenubeta.util.Util;

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

		boolean net = Util.isNetworkAvailable(getActivity());
		boolean gps = true;

		if (net && gps) {

			// Inicia a transção
			task = new TransactionTask(this.getActivity(), transaction,
					R.string.wait);
			task.execute();

		} else {
			
			Log.i("appLog","error!");

			if (net) {
				Util.alertDialog(getActivity(),
						"Verifique se o Wi-fi ou o plano de dados esta ativo.");
			}

			if (gps) {
				Util.alertDialog(getActivity(),
						"Verifique se o GPS esta ativo.");
			}
			
			onDestroy();

		}
	}

}
