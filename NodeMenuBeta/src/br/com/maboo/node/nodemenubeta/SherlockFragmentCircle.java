package br.com.maboo.node.nodemenubeta;

import br.livroandroid.transacao.Transacao;
import br.livroandroid.transacao.TransacaoTask;
import br.livroandroid.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;

public class SherlockFragmentCircle extends SherlockFragment{

	public void alert(int mensagem) {
		AndroidUtils.alertDialog(getActivity(), mensagem);
	}
	
	public void startTransaction(Transacao transacao) {
		TransacaoTask task = new TransacaoTask(getActivity(), transacao, R.string.wait);
		task.execute();
	}
}
