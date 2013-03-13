package br.com.maboo.neext.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class DBConnection {

	private static final String CATEGORIA = "base";

	private static SQLiteDatabase db = null;

	private static SQLiteHelper dbHelper = null;

	public DBConnection(final Context ctx, final String base_name) {
		super();

		// Abre o banco de dados já existente
		try {
			if (getDb() == null) {
				synchronized (DBConnection.class) {

					if (getDb() == null) {

						Log.i(CATEGORIA, "Abrindo conexão com o db.");

						setDb(ctx.openOrCreateDatabase(base_name,
								Context.MODE_PRIVATE, null));
					}
				}
			}

			// if (db != null) {
			// if (db.isOpen()) {
			// if (dbHelper != null) {
			//
			// Log.i(CATEGORIA, "Abrindo o db para edição.");
			//
			// db = dbHelper.getReadableDatabase();
			//
			// } else {
			//
			// Log.i(CATEGORIA, "o dbHelper é nulo.");
			//
			// }
			// } else {
			//
			// Log.i(CATEGORIA, "O db esta fechado.");
			//
			// db = ctx.openOrCreateDatabase(base_name,
			// Context.MODE_PRIVATE,null);
			//
			// Log.i(CATEGORIA, "Pronto! Db aberto.");
			// }
			// } else {
			//
			// Log.i(CATEGORIA, "Abrindo conexão com o db.");
			//
			// db = ctx.openOrCreateDatabase(base_name, Context.MODE_PRIVATE,
			// null);
			//
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long inserir(final ContentValues valores, final String table_name) {
		final long id = getDb().insert(table_name, "", valores);
		return id;
	}

	public int atualizar(final ContentValues valores, final String where,
			final String[] whereArgs, final String table_name) {
		final int count = getDb().update(table_name, valores, where, whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}

	public int deletar(final String where, final String[] whereArgs,
			final String table_name) {
		final int count = getDb().delete(table_name, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}

	public Cursor query(final SQLiteQueryBuilder queryBuilder,
			final String[] projection, final String selection,
			final String[] selectionArgs, final String groupBy,
			final String having, final String orderBy) {
		final Cursor c = queryBuilder.query(getDb(), projection, selection,
				selectionArgs, groupBy, having, orderBy);
		return c;
	}

	public static SQLiteHelper getDbHelper() {
		return dbHelper;
	}

	public static void setDbHelper(SQLiteHelper dbHelper) {
		DBConnection.dbHelper = dbHelper;
	}

	public static SQLiteDatabase getDb() {
		return db;
	}

	public static void setDb(SQLiteDatabase db) {
		DBConnection.db = db;
	}

}
