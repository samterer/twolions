package br.com.maboo.node.transaction;

import android.os.AsyncTask;
import br.livroandroid.transacao.Transacao;
import br.livroandroid.transacao.TransacaoTask;
import br.livroandroid.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;

public class TransactionFragment extends SherlockFragment {
	private TransacaoTask task;

	protected void alert(int mensagem) {
		AndroidUtils.alertDialog(getActivity().getApplicationContext(),
				mensagem);
	}

	// Inicia a thread
	public void startTransacao(Transacao transacao) {
		// Inicia a transção
		task = new TransacaoTask(getActivity(), transacao, R.string.wait);
		task.execute();

	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (task != null) {
			boolean executando = task.getStatus().equals(
					AsyncTask.Status.RUNNING);
			if (executando) {
				task.cancel(true);
				task.fecharProgress();
			}
		}
	}

}
