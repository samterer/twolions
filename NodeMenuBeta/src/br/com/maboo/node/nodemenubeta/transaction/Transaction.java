package br.com.maboo.node.nodemenubeta.transaction;

public interface Transaction {
	// Executar a transa��o em uma thread separada
	public void execute() throws Exception;
}
