package br.livroandroid.transacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.livroandroid.utils.AndroidUtils;

/**
 * Classe para controlar a thread
 * 
 * @author ricardo
 * 
 */
public class TransacaoTask extends AsyncTask<Void, Void, Boolean> {
	private static final String TAG = "livroandroid";
	private final Context context;
	private final Transacao transacao;
	private ProgressDialog progresso;
	private Throwable exceptionErro;
	private int aguardeMsg;
	public TransacaoTask(Context context, Transacao transacao, int aguardeMsg) {
		this.context = context;
		this.transacao = transacao;
		this.aguardeMsg = aguardeMsg;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Inicia a janela de aguarde...
		abrirProgress();
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			transacao.executar();
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
			// Salva o erro e retorna false
			this.exceptionErro = e;
			return false;
		} finally {
			try {
				fecharProgress();
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
			transacao.atualizarView();
		} else {
			// Erro
			AndroidUtils.alertDialog(context, "Erro: " + exceptionErro.getMessage());
		}
	}
	public void abrirProgress() {
		try {
			progresso = ProgressDialog.show(context, "", context.getString(aguardeMsg));
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	public void fecharProgress() {
		try {
			if (progresso != null) {
				progresso.dismiss();
			}
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}
