package br.livroandroid.transacao;

public interface Transacao {
	// Executar a transação em uma thread separada
	public void executar() throws Exception;

	// Atualizar a view sincronizado com a thread principal
	public void atualizarView();
}
