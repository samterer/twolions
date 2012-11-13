package br.com.maboo.neext.transaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.com.maboo.neext.R;
import br.com.maboo.neext.util.AndroidUtils;
import br.com.maboo.neext.util.Constants;

/**
 * Classe para controlar a thread
 * 
 * @author ricardo
 * 
 */
public class TransactionTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = Constants.LOG_APP;

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
		return true;
	}

	@Override
	protected void onPostExecute(Boolean ok) {
		if (ok) {

			// Transação executou com sucesso
			transaction.update();

		} else {

			// Erro
			AndroidUtils.alertDialog(context,
					"Error: " + exceptionError.getMessage());
		}
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
