package br.com.twolions;

public interface Transaction {
	// Executar a transa��o em uma thread separada
	public void execute() throws Exception;

	// Atualizar a view sincronizado com a thread principal
	public void updateList();
}
