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
	private String NOME_TABELA = "";

	protected DBConnection dbCon;

	/*
	 * Instancia da classe Nome do banco Nome da tabela
	 */
	public ManagerDAO(Context ctx, String banco, String table) {

		if (table != null) {
			this.NOME_TABELA = table;
		}

		// if (dbCon == null) {
		Log.e(CATEGORIA, "Open a new connection with dbCon. ");
		dbCon = new DBConnection(ctx, banco);
		// }

		Log.e(CATEGORIA, "NOME_TABELA: " + NOME_TABELA);

	}

	// Insere um novo carro
	public long inserir(ContentValues valores) {
		long id = dbCon.getInstance().insert(NOME_TABELA, "", valores);
		return id;
	}

	// Atualiza o carro com os valores abaixo
	// A cláusula where é utilizada para identificar o carro a ser atualizado
	public int atualizar(ContentValues valores, String where, String[] whereArgs) {
		int count = dbCon.getInstance().update(NOME_TABELA, valores, where,
				whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}

	// Deleta o carro com os argumentos fornecidos
	public int deletar(String where, String[] whereArgs) {
		int count = dbCon.getInstance().delete(NOME_TABELA, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}

	// Retorna um cursor com todos os elementos
	public Cursor getCursor() {
		try {
			// select * from carros
			return dbCon.getInstance().query(NOME_TABELA, Carro.colunas, null,
					null, null, null, null, null);
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
		Cursor c = queryBuilder.query(dbCon.getInstance(), projection,
				selection, selectionArgs, groupBy, having, orderBy);
		return c;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (dbCon != null) {
			if (dbCon.getInstance() != null) {
				dbCon.getInstance().close();
				dbCon = null;
			}
		}

	}
}
