package br.com.twolions.base;

import android.content.Context;

public class RepositorioCarroScript extends RepositorioCarro {

	// Script para fazer drop na tabela
	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS carro";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_DATABASE_CREATE = new String[]{
			"create table carro ( _id integer primary key autoincrement, nome text not null,placa text not null);",
			"insert into carro(nome,placa) values('Fusca','ABC-1234');",
			"insert into carro(nome,placa) values('Brasilia','DEF-5678');",
			"insert into carro(nome,placa) values('Chevete','GHI-9999');"};

	// Nome do banco
	private static final String NOME_BANCO = "bd_fuel";

	// Controle de versão
	private static final int VERSAO_BANCO = 1;

	// Nome da tabela
	public static final String TABELA_CARRO = "carro";

	// Classe utilitária para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;

	// Cria o banco de dados com um script SQL
	public RepositorioCarroScript(Context ctx) {
		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, RepositorioCarroScript.NOME_BANCO,
				RepositorioCarroScript.VERSAO_BANCO,
				RepositorioCarroScript.SCRIPT_DATABASE_CREATE,
				RepositorioCarroScript.SCRIPT_DATABASE_DELETE);

		// abre o banco no modo escrita para poder alterar também
		db = dbHelper.getWritableDatabase();
	}

	// Fecha o banco
	public void fechar() {
		super.fechar();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
