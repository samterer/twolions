package br.com.twolions.sql;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBConnection {

	private static final String CATEGORIA = "base";

	private SQLiteDatabase db;

	public DBConnection(Context ctx, String nome_banco) {
		// Abre o banco de dados já existente
		try {
			if (db == null) {
				db = ctx.openOrCreateDatabase(nome_banco, Context.MODE_PRIVATE,
						null);
			} else {
				Log.i(CATEGORIA, "Utilizando a conexão já aberta.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public SQLiteDatabase getInstance() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	// Fecha o banco
	// public void closed() {
	// // fecha o banco de dados
	// if (db != null) {
	// db.close();
	// }
	// }

}
