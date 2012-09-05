package br.com.twolions.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.sql.DBConnection;

public class ManagerDAO {

	private static final String CATEGORIA = "base";

	// Nome da tabela
	public static String nome_tabela = "";

	protected DBConnection db;

	/*
	 * Instancia da classe Nome do banco Nome da tabela
	 */
	public ManagerDAO(Context ctx, String banco, String table) {

		this.nome_tabela = table;

		if (db.getInstance() == null) {
			db = new DBConnection(ctx, banco);
		}

	}
	public ManagerDAO(Context ctx, String banco) {

		if (db.getInstance() == null) {
			db = new DBConnection(ctx, banco);
		}

	}

	// Insere um novo carro
	public long inserir(ContentValues valores) {
		long id = db.getInstance().insert(nome_tabela, "", valores);
		return id;
	}

	// Atualiza o carro com os valores abaixo
	// A cláusula where é utilizada para identificar o carro a ser atualizado
	public int atualizar(ContentValues valores, String where, String[] whereArgs) {
		int count = db.getInstance().update(nome_tabela, valores, where,
				whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}

	// Deleta o carro com os argumentos fornecidos
	public int deletar(String where, String[] whereArgs) {
		int count = db.getInstance().delete(nome_tabela, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}

	// Retorna um cursor com todos os carros
	public Cursor getCursor() {
		try {
			// select * from carros
			return db.getInstance().query(nome_tabela, Carro.colunas, null,
					null, null, null, null, null);
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar os objs: " + e.toString());
			return null;
		}
	}

	// Busca um carro utilizando as configurações definidas no
	// SQLiteQueryBuilder
	// Utilizado pelo Content Provider de carro
	public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor c = queryBuilder.query(db.getInstance(), projection, selection,
				selectionArgs, groupBy, having, orderBy);
		return c;
	}

}
