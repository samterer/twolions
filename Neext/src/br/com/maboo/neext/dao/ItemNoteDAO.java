package br.com.maboo.neext.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.sql.DBConnection;
import br.com.maboo.neext.util.Constants;

public class ItemNoteDAO extends DBConnection {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String base_name = Constants.DB_NAME;
	// Nome da tabela
	public static final String table_name = Constants.TB_ITEM_LOG;

	// private ManagerDAO dao;

	public ItemNoteDAO(final Context ctx) {
		super(ctx, base_name);

		Log.i(CATEGORIA, "ItemNoteDAO...");

	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(final ItemNote itemLog) {
		long id = itemLog.getId();

		if (id != 0) {
			atualizar(itemLog);
		} else {
			// Insere novo
			id = inserir(itemLog);
		}

		return id;
	}

	public long inserir(final ItemNote itemLog) {
		final ContentValues values = new ContentValues();
		values.put(ItemNote.TYPE, itemLog.getType());
		values.put(ItemNote.DATE, String.valueOf(itemLog.getDate()));
		values.put(ItemNote.SUBJECT, itemLog.getSubject());

		values.put(ItemNote.TEXT, itemLog.getText());

		// Log.i(CATEGORIA, "Inserindo o itemLog: " +
		// buscarItemNote(itemLog.getId()).toString());

		final long id = inserir(values, table_name);
		return id;
	}

	public int atualizar(final ItemNote itemLog) {
		final ContentValues values = new ContentValues();
		values.put(ItemNote.TYPE, itemLog.getType());
		values.put(ItemNote.DATE, String.valueOf(itemLog.getDate()));

		values.put(ItemNote.SUBJECT, itemLog.getSubject());

		values.put(ItemNote.TEXT, itemLog.getText());

		final String _id = String.valueOf(itemLog.getId());

		Log.i(CATEGORIA,
				"Atualizando o itemLog: "
						+ buscarItemNote(itemLog.getId()).toString());

		final String where = ItemNote._ID + "=?";
		final String[] whereArgs = new String[] { _id };

		final int count = atualizar(values, where, whereArgs, table_name);

		return count;
	}

	public int deletar(final long id) {
		final String where = ItemNote._ID + "=?";

		final String _id = String.valueOf(id);
		final String[] whereArgs = new String[] { _id };

		Log.i(CATEGORIA, "Deletando o itemLog: "
				+ buscarItemNote(id).toString());

		final int count = deletar(where, whereArgs, table_name);

		return count;
	}

	public ItemNote buscarItemNote(final long id) {
		// select * from ItemNote where _id=?
		final Cursor c = db.query(true, table_name, ItemNote.colunas,
				ItemNote._ID + "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			final ItemNote itemLog = new ItemNote();

			// L� os dados
			itemLog.setId(c.getLong(0));
			itemLog.setType(c.getString(1));
			itemLog.setDate(c.getString(2));
			itemLog.setSubject(c.getString(3));
			itemLog.setText(c.getString(4));

			Log.i(CATEGORIA, "ItemNote: " + itemLog.toString());

			return itemLog;
		}

		return null;
	}

	// Retorna uma lista com todos os ItemNotes pelo id do carro
	public List<ItemNote> listarItemNotes() {
		final Cursor c = getCursor();

		final List<ItemNote> itemLogs = new ArrayList<ItemNote>();

		if (c.moveToFirst()) {

			// Recupera os �ndices das colunas
			final int idxId = c.getColumnIndex(ItemNote._ID);

			final int idxIdType = c.getColumnIndex(ItemNote.TYPE);
			final int idxIdDate = c.getColumnIndex(ItemNote.DATE);
			final int idxIdSubject = c.getColumnIndex(ItemNote.SUBJECT);

			final int idxIdText = c.getColumnIndex(ItemNote.TEXT);

			// Loop at� o final
			do {
				final ItemNote itemLog = new ItemNote();
				itemLogs.add(itemLog);

				// recupera os atributos de carro
				itemLog.setId(c.getLong(idxId));
				itemLog.setType(c.getString(idxIdType));
				itemLog.setDate(c.getString(idxIdDate));
				itemLog.setSubject(c.getString(idxIdSubject));

				itemLog.setText(c.getString(idxIdText));

				Log.i(CATEGORIA, "ItemNote: " + itemLog.toString());

			} while (c.moveToNext());
		}

		return itemLogs;
	}

	// Retorna um cursor com todos os carros
	public Cursor getCursor() {
		try {
			return db.query(table_name, ItemNote.colunas, null, null, null,
					null, null, null);
		} catch (final SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar os logs: " + e.toString());
			return null;
		}
	}

	// retorna uma lista de itens pelo type
	// type:
	// - fuel - 0
	// - expense - 1
	// - note - 2
	// - repair - 3
	public List<ItemNote> listarItemNotesPorTipo(final String type_item) {
		final Cursor c = db.query(table_name, ItemNote.colunas, ItemNote.TYPE
				+ "='" + type_item + "'", null, null, null, null);

		final List<ItemNote> itemLogs = new ArrayList<ItemNote>();

		if (c.moveToFirst()) {

			// Recupera os �ndices das colunas
			final int idxId = c.getColumnIndex(ItemNote._ID);

			final int idxIdDate = c.getColumnIndex(ItemNote.DATE);
			final int idxIdType = c.getColumnIndex(ItemNote.TYPE);
			final int idxIdSubject = c.getColumnIndex(ItemNote.SUBJECT);

			final int idxIdText = c.getColumnIndex(ItemNote.TEXT);

			// Loop at� o final
			do {
				final ItemNote itemLog = new ItemNote();
				itemLogs.add(itemLog);

				// recupera os atributos de carro
				itemLog.setId(c.getLong(idxId));

				itemLog.setType(c.getString(idxIdType));
				itemLog.setDate(c.getString(idxIdDate));
				itemLog.setSubject(c.getString(idxIdSubject));

				itemLog.setText(c.getString(idxIdText));

				Log.i(CATEGORIA, "ItemNote: " + itemLog.toString());

			} while (c.moveToNext());
		}

		return itemLogs;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
