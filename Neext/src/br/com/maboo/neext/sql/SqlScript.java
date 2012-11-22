package br.com.maboo.neext.sql;

import android.content.Context;
import br.com.maboo.neext.util.Constants;

public class SqlScript extends DBConnection {

	// Nome do banco
	private static final String BASE_NAME = Constants.DB_NAME;

	// Controle de versão
	private static final int VERSAO_BANCO = Constants.DB_VERSION;

	// Classe utilitária para abrir, criar, e atualizar o banco de dados
	// private final SQLiteHelper dbHelper;

	private static String[] sqlScript = { "create table", "insert into" };

	// ///////////////
	// TABLE ITEM LOG
	// ///////////////
	public static final String TB_ITEM_LOG = Constants.TB_ITEM_LOG;

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_ITEM_LOG = "DROP TABLE IF EXISTS "
			+ TB_ITEM_LOG;

	// Cria a tabela com o "_id" sequencial
	// type item
	// - fuel - 0
	// - expense - 1
	// - note - 2
	// - repair - 3
	private static final String[] SCRIPT_CREATE_TB_ITEM_LOG = new String[] {
			sqlScript[0]
					+ " "
					+ TB_ITEM_LOG
					+ " ( _id integer primary key autoincrement, type integer not null, date text not null, subject text, text text)",

			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(type,date,subject,text) values('a6a6ed','25/12/2012-20:30','teste1','testando testando');",
			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(type,date,subject,text) values('c7eb78','25/12/2012-21:30','teste2','testando testando');",
			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(type,date,subject,text) values('a5f5a5','25/11/2012-22:30','teste3','testando testando');",
			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(type,date,subject,text) values('FFA500','25/11/2012-23:30','teste4','testando testando');" };

	// Cria o banco de dados com um script SQL
	public SqlScript(final Context ctx) {
		super(ctx, BASE_NAME);

		// create
		final String[] script_tables_create = {
				// create fuel car and 1 register
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[0],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[1],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[2],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[3],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[4] };
		// delete
		final String[] script_tables_delete = {

		// delete table item
		SqlScript.SCRIPT_DELETE_TB_ITEM_LOG };

		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, SqlScript.BASE_NAME,
				SqlScript.VERSAO_BANCO, script_tables_create,
				script_tables_delete);

		// abre o banco no modo escrita para poder alterar também
		db = dbHelper.getWritableDatabase();

	}

	// Fecha o banco
	public void fechar() {

		if (db != null) {
			db.close();
		}

		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
