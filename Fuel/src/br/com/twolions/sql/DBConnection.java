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

	protected static SQLiteDatabase db = null;

	protected static SQLiteHelper dbHelper = null;

	public DBConnection(final Context ctx, final String base_name) {
		super();

		// Abre o banco de dados já existente
		try {
			// if (db == null) {

			if (db != null) {
				if (db.isOpen()) {
					if (dbHelper != null) {
					
						Log.i(CATEGORIA, "Abrindo o db para edição.");
						
						db = dbHelper.getReadableDatabase();
					
					} else {
					
						Log.i(CATEGORIA, "o dbHelper é nulo.");

					}
				} else {
				
					Log.i(CATEGORIA, "O db esta fechado.");
					
					db = ctx.openOrCreateDatabase(base_name, Context.MODE_PRIVATE,null);
					
					Log.i(CATEGORIA, "Pronto! Db aberto.");
				}
			} else {
			
				Log.i(CATEGORIA, "Abrindo conexão com o db.");
				
				db = ctx.openOrCreateDatabase(base_name, Context.MODE_PRIVATE,
						null);
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public long inserir(final ContentValues valores, final String table_name) {
		final long id = db.insert(table_name, "", valores);
		return id;
	}
	public int atualizar(final ContentValues valores, final String where,
			final String[] whereArgs, final String table_name) {
		final int count = db.update(table_name, valores, where, whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}
	public int deletar(final String where, final String[] whereArgs,
			final String table_name) {
		final int count = db.delete(table_name, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}
	public Cursor query(final SQLiteQueryBuilder queryBuilder,
			final String[] projection, final String selection,
			final String[] selectionArgs, final String groupBy,
			final String having, final String orderBy) {
		final Cursor c = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, orderBy);
		return c;
	}

}
