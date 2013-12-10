package br.com.maboo.node.nodemenubeta.transaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidbegin.menuviewpagertutorial.R;

/**
 * Classe para controlar a thread
 * 
 * 
 */
public class TransactionTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = "TransactionTask";

	private final Context context;

	private final Transaction transaction;

	private ProgressDialog progress;

	private Throwable exceptionError;

	private int aguardeMsg;

	public TransactionTask(Context context, Transaction transaction,
			int aguardeMsg) {

		this.context = context;

		this.transaction = transaction;

		this.aguardeMsg = aguardeMsg;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Inicia a janela de aguarde...
		openProgress();
	}

	public void openProgress() {

		try {
			progress = ProgressDialog.show(context, "",
					context.getString(aguardeMsg));

			progress.getWindow().setBackgroundDrawableResource(
					R.color.transparente);

		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}

	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {

			transaction.execute();

		} catch (Throwable e) {

			Log.e(TAG, e.getMessage(), e);

			// Salva o erro e retorna false
			this.exceptionError = e;

			return false;

		} finally {

			try {
				closedProgress();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}

		}

		// OK
		//TODO
		onPostExecute(true);
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean ok) {
		if (ok) {

			// Transação executou com sucesso
			transaction.update();

		} else {

			// Erro
			Log.e(TAG, "Error: " + exceptionError.getMessage());
		}
	}


	public void closedProgress() {
		try {
			if (progress != null) {
				progress.dismiss();
			}
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}
