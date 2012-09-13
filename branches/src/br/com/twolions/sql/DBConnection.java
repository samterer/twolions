package br.com.twolions.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class DBConnection {

	private static final String CATEGORIA = "base";

	protected SQLiteDatabase db;

	public DBConnection(Context ctx, String nome_banco) {
		super();

		// Abre o banco de dados já existente
		try {
			if (getInstance() == null) {
				Log.i(CATEGORIA, "Abrindo conexão com o db.");
				this.db = ctx.openOrCreateDatabase(nome_banco,
						Context.MODE_PRIVATE, null);
			} else {
				Log.i(CATEGORIA, "Utilizando a conexão já aberta.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public long inserir(ContentValues valores, String table_name) {
		long id = db.insert(table_name, "", valores);
		return id;
	}
	public int atualizar(ContentValues valores, String where,
			String[] whereArgs, String table_name) {
		int count = db.update(table_name, valores, where, whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}
	public int deletar(String where, String[] whereArgs, String table_name) {
		int count = db.delete(table_name, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}
	public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor c = queryBuilder.query(db, projection, selection, selectionArgs,
				groupBy, having, orderBy);
		return c;
	}

	public SQLiteDatabase getInstance() {
		return this.db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

}
