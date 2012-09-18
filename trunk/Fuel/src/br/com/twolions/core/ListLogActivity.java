package br.com.twolions.core;

import br.com.core.ActivityCircle;
import br.com.transacao.Transacao;
import br.com.transacao.TransacaoTask;
import br.com.twolions.R;
import br.com.utils.AndroidUtils;

public class ListLogActivity extends ActivityCircle {

	protected void alert(final int mensagem) {
		AndroidUtils.alertDialog(this, mensagem);
	}

	// inicia a thread
	public void startTransaction(final Transacao transacao) {

		// inicia a transacao
		final TransacaoTask task = new TransacaoTask(this, transacao,
				R.string.aguarde);
		task.execute();
	}
}
