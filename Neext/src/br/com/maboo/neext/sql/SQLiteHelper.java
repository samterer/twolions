package br.com.maboo.neext.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Implementacao de SQLiteOpenHelper
 * 
 * Classe utilit�ria para abrir, criar, e atualizar o banco de dados
 * 
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String CATEGORIA = "base";

	private final String[] scriptSQLCreate;
	private final String[] scriptSQLDelete;

	/**
	 * Cria uma inst�ncia de SQLiteHelper
	 * 
	 * @param context
	 * @param nomeBanco
	 *            nome do banco de dados
	 * @param versaoBanco
	 *            vers�o do banco de dados (se for diferente � para atualizar)
	 * @param scriptSQLCreate
	 *            SQL com o create table..
	 * @param scriptSQLDelete
	 *            SQL com o drop table...
	 */
	SQLiteHelper(final Context context, final String nomeBanco,
			final int versaoBanco, final String[] scriptSQLCreate,
			final String[] scriptSQLDelete) {
		super(context, nomeBanco, null, versaoBanco);

		this.scriptSQLCreate = scriptSQLCreate;

		this.scriptSQLDelete = scriptSQLDelete;

	}

	@Override
	// Criar novo banco...
	public void onCreate(final SQLiteDatabase db) {
	//	Log.i(CATEGORIA, "Criando banco com sql em [" + db.getPath() + "]");
		final int qtdeScripts = scriptSQLCreate.length;

		// Executa cada sql passado como par�metro
		for (int i = 0; i < qtdeScripts; i++) {
			final String sql = scriptSQLCreate[i];
	//		Log.i(CATEGORIA, sql);
			// Cria o banco de dados executando o script de cria��o
			db.execSQL(sql);
		}
	}

	@Override
	// Mudou a vers�o...
	public void onUpgrade(final SQLiteDatabase db, final int versaoAntiga,
			final int novaVersao) {
	//	Log.w(CATEGORIA, "Atualizando da vers�o " + versaoAntiga + " para " + novaVersao + ". Todos os registros ser�o deletados.");
		// Log.i(CATEGORIA, scriptSQLDelete);
		// Deleta as tabelas...
		final int qtdeScripts = scriptSQLDelete.length;

		// Executa cada sql passado como par�metro
		for (int i = 0; i < qtdeScripts; i++) {
			final String sql = scriptSQLDelete[i];
			Log.i(CATEGORIA, sql);
			// Cria o banco de dados executando o script de cria��o
			db.execSQL(sql);
		}

		// db.execSQL(scriptSQLDelete);
		// Cria novamente...
		onCreate(db);
	}
}