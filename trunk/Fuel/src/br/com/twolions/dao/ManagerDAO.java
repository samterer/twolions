package br.com.twolions.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import br.com.twolions.daoobjects.Carro;

public class ManagerDAO {

	private static final String CATEGORIA = "base";

	protected SQLiteDatabase db = null;

	private String table_name = "";

	/*
	 * Instancia da classe Nome do banco Nome da tabela
	 */
	public ManagerDAO(Context ctx, String banco, String table,
			SQLiteDatabase dbSql) {

		db = dbSql;

		table_name = table;

		// if (table != null && table != "") {
		// this.NOME_TABELA = table;
		// Log.e(CATEGORIA, "Table name: " + table);
		// } else {
		// Log.e(CATEGORIA, "The name table is empty.");
		// }

		// db = new DBConnection(ctx, banco);
		// Log.e(CATEGORIA, "Open a new connection with db. ["+
		// db.getInstance().getPath() + "]");

	}
	// Insere um novo carro
	public long inserir(ContentValues valores) {
		Log.i(CATEGORIA, "table_name [" + table_name + "]");
		long id = db.insert(table_name, "", valores);
		return id;
	}

	// Atualiza o carro com os valores abaixo
	// A cláusula where é utilizada para identificar o carro a ser atualizado
	public int atualizar(ContentValues valores, String where, String[] whereArgs) {
		int count = db.update(table_name, valores, where, whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}

	// Deleta o carro com os argumentos fornecidos
	public int deletar(String where, String[] whereArgs) {
		int count = db.delete(table_name, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}

	// Retorna um cursor com todos os elementos
	public Cursor getCursor() {
		try {
			// select from carros
			return db.query(table_name, Carro.colunas, null, null, null, null,
					null, null);
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar os objs: " + e.toString());
			e.printStackTrace();
			return null;
		}
	}

	// Busca um carro utilizando as configurações definidas no
	// SQLiteQueryBuilder
	// Utilizado pelo Content Provider
	public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor c = queryBuilder.query(db, projection, selection, selectionArgs,
				groupBy, having, orderBy);
		return c;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			if (db != null) {
				db.close();
				db = null;
			}
		}
	}

}
