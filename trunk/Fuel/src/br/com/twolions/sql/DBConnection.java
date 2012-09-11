package br.com.twolions.sql;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBConnection {

	private static final String CATEGORIA = "base";

	private SQLiteDatabase db;

	public DBConnection(Context ctx, String nome_banco) {
		super();

		// Abre o banco de dados j� existente
		try {
			if (getInstance() == null) {
				Log.i(CATEGORIA, "Abrindo conex�o com o dbCon.");
				this.db = ctx.openOrCreateDatabase(nome_banco,
						Context.MODE_PRIVATE, null);
			} else {
				Log.i(CATEGORIA, "Utilizando a conex�o j� aberta.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public SQLiteDatabase getInstance() {
		return this.db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

}
