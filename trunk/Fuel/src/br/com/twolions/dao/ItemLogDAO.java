package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import br.com.twolions.modelobj.ItemLog;
import br.com.twolions.sql.DBConnection;
import br.com.twolions.util.Constants;

public class ItemLogDAO extends DBConnection {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String base_name = Constants.DB_NAME;
	// Nome da tabela
	public static final String table_name = Constants.TB_ITEM_LOG;

	// private ManagerDAO dao;

	public ItemLogDAO(final Context ctx) {
		super(ctx, base_name);

		Log.i(CATEGORIA, "ItemLogDAO...");
		

	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(final ItemLog itemLog) {
		long id = itemLog.getId();

		if (id != 0) {
			atualizar(itemLog);
		} else {
			// Insere novo
			id = inserir(itemLog);
		}

		return id;
	}

	public long inserir(final ItemLog itemLog) {
		final ContentValues values = new ContentValues();
		values.put(ItemLog.ID_CAR, itemLog.getId_car());
		values.put(ItemLog.DATE, String.valueOf(itemLog.getDate()));
		values.put(ItemLog.TYPE, itemLog.getType());
		values.put(ItemLog.SUBJECT, itemLog.getSubject());
		values.put(ItemLog.VALUE_P, itemLog.getValue_p());
		values.put(ItemLog.VALUE_U, itemLog.getValue_u());
		values.put(ItemLog.ODOMETER, itemLog.getOdometer());
		values.put(ItemLog.TEXT, itemLog.getText());
		
		//Log.i(CATEGORIA, "Inserindo o itemLog: " + buscarItemLog(itemLog.getId()).toString());

		final long id = inserir(values, table_name);
		return id;
	}

	public int atualizar(final ItemLog itemLog) {
		final ContentValues values = new ContentValues();
		values.put(ItemLog.ID_CAR, itemLog.getId_car());
		values.put(ItemLog.DATE, String.valueOf(itemLog.getDate()));
		values.put(ItemLog.TYPE, itemLog.getType());
		values.put(ItemLog.SUBJECT, itemLog.getSubject());
		values.put(ItemLog.VALUE_P, itemLog.getValue_p());
		values.put(ItemLog.VALUE_U, itemLog.getValue_u());
		values.put(ItemLog.ODOMETER, itemLog.getOdometer());
		values.put(ItemLog.TEXT, itemLog.getText());

		final String _id = String.valueOf(itemLog.getId());

		Log.i(CATEGORIA, "Atualizando o itemLog: " + buscarItemLog(itemLog.getId()).toString());
		
		final String where = ItemLog._ID + "=?";
		final String[] whereArgs = new String[]{_id};

		final int count = atualizar(values, where, whereArgs, table_name);

		return count;
	}

	public int deletar(final long id) {
		final String where = ItemLog._ID + "=?";

		final String _id = String.valueOf(id);
		final String[] whereArgs = new String[]{_id};
		
		Log.i(CATEGORIA, "Deletando o itemLog: " + buscarItemLog(id).toString());

		final int count = deletar(where, whereArgs, table_name);

		return count;
	}

	public ItemLog buscarItemLog(final long id) {
		// select * from ItemLog where _id=?
		final Cursor c = db.query(true, table_name, ItemLog.colunas,
				ItemLog._ID + "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			final ItemLog itemLog = new ItemLog();

			// Lê os dados
			itemLog.setId(c.getLong(0));
			itemLog.setId_car(c.getLong(1));
			itemLog.setDate(c.getString(2));
			itemLog.setType(c.getInt(3));
			itemLog.setSubject(c.getString(4));
			itemLog.setValue_p(c.getDouble(5));
			itemLog.setValue_u(c.getDouble(6));
			itemLog.setOdometer(c.getLong(7));
			itemLog.setText(c.getString(8));

			Log.i(CATEGORIA, "ItemLog: " + itemLog.toString());

			return itemLog;
		}

		return null;
	}

	// Retorna uma lista com todos os ItemLogs pelo id do carro
	public List<ItemLog> listarItemLogs(final long id_Car) {
		final Cursor c = db.query(table_name, ItemLog.colunas, ItemLog.ID_CAR
				+ "='" + id_Car + "'", null, null, null, null);

		final List<ItemLog> itemLogs = new ArrayList<ItemLog>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			final int idxId = c.getColumnIndex(ItemLog._ID);
			final int idxIdCar = c.getColumnIndex(ItemLog.ID_CAR);
			final int idxIdDate = c.getColumnIndex(ItemLog.DATE);
			final int idxIdType = c.getColumnIndex(ItemLog.TYPE);
			final int idxIdSubject = c.getColumnIndex(ItemLog.SUBJECT);
			final int idxIdValueP = c.getColumnIndex(ItemLog.VALUE_P);
			final int idxIdValueU = c.getColumnIndex(ItemLog.VALUE_U);
			final int idxIdOdometer = c.getColumnIndex(ItemLog.ODOMETER);
			final int idxIdText = c.getColumnIndex(ItemLog.TEXT);

			// Loop até o final
			do {
				final ItemLog itemLog = new ItemLog();
				itemLogs.add(itemLog);

				// recupera os atributos de carro
				itemLog.setId(c.getLong(idxId));
				itemLog.setId_car(c.getLong(idxIdCar));
				itemLog.setDate(c.getString(idxIdDate));
				itemLog.setType(c.getInt(idxIdType));
				itemLog.setSubject(c.getString(idxIdSubject));
				itemLog.setValue_p(c.getDouble(idxIdValueP));
				itemLog.setValue_u(c.getDouble(idxIdValueU));
				itemLog.setOdometer(c.getLong(idxIdOdometer));
				itemLog.setText(c.getString(idxIdText));

				Log.i(CATEGORIA, "ItemLog: " + itemLog.toString());

			} while (c.moveToNext());
		}

		return itemLogs;
	}

	// Retorna uma lista com todos os ItemLogs pelo id do carro
	public List<ItemLog> listarItemLogs() {
		final Cursor c = getCursor();

		final List<ItemLog> itemLogs = new ArrayList<ItemLog>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			final int idxId = c.getColumnIndex(ItemLog._ID);
			final int idxIdCar = c.getColumnIndex(ItemLog.ID_CAR);
			final int idxIdDate = c.getColumnIndex(ItemLog.DATE);
			final int idxIdType = c.getColumnIndex(ItemLog.TYPE);
			final int idxIdSubject = c.getColumnIndex(ItemLog.SUBJECT);
			final int idxIdValueP = c.getColumnIndex(ItemLog.VALUE_P);
			final int idxIdValueU = c.getColumnIndex(ItemLog.VALUE_U);
			final int idxIdOdometer = c.getColumnIndex(ItemLog.ODOMETER);
			final int idxIdText = c.getColumnIndex(ItemLog.TEXT);

			// Loop até o final
			do {
				final ItemLog itemLog = new ItemLog();
				itemLogs.add(itemLog);

				// recupera os atributos de carro
				itemLog.setId(c.getLong(idxId));
				itemLog.setId_car(c.getLong(idxIdCar));
				itemLog.setDate(c.getString(idxIdDate));
				itemLog.setType(c.getInt(idxIdType));
				itemLog.setSubject(c.getString(idxIdSubject));
				itemLog.setValue_p(c.getDouble(idxIdValueP));
				itemLog.setValue_u(c.getDouble(idxIdValueU));
				itemLog.setOdometer(c.getLong(idxIdOdometer));
				itemLog.setText(c.getString(idxIdText));

				Log.i(CATEGORIA, "ItemLog: " + itemLog.toString());

			} while (c.moveToNext());
		}

		return itemLogs;
	}
	// Retorna um cursor com todos os carros
	public Cursor getCursor() {
		try {
			return db.query(table_name, ItemLog.colunas, null, null, null,
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
	public List<ItemLog> listarItemLogsPorTipo(final String type_item) {
		final Cursor c = db.query(table_name, ItemLog.colunas, ItemLog.TYPE
				+ "='" + type_item + "'", null, null, null, null);

		final List<ItemLog> itemLogs = new ArrayList<ItemLog>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			final int idxId = c.getColumnIndex(ItemLog._ID);
			final int idxIdCar = c.getColumnIndex(ItemLog.ID_CAR);
			final int idxIdDate = c.getColumnIndex(ItemLog.DATE);
			final int idxIdType = c.getColumnIndex(ItemLog.TYPE);
			final int idxIdSubject = c.getColumnIndex(ItemLog.SUBJECT);
			final int idxIdValueP = c.getColumnIndex(ItemLog.VALUE_P);
			final int idxIdValueU = c.getColumnIndex(ItemLog.VALUE_U);
			final int idxIdOdometer = c.getColumnIndex(ItemLog.ODOMETER);
			final int idxIdText = c.getColumnIndex(ItemLog.TEXT);

			// Loop até o final
			do {
				final ItemLog itemLog = new ItemLog();
				itemLogs.add(itemLog);

				// recupera os atributos de carro
				itemLog.setId(c.getLong(idxId));
				itemLog.setId_car(c.getLong(idxIdCar));
				itemLog.setDate(c.getString(idxIdDate));
				itemLog.setType(c.getInt(idxIdType));
				itemLog.setSubject(c.getString(idxIdSubject));
				itemLog.setValue_p(c.getDouble(idxIdValueP));
				itemLog.setValue_u(c.getDouble(idxIdValueU));
				itemLog.setOdometer(c.getLong(idxIdOdometer));
				itemLog.setText(c.getString(idxIdText));

				Log.i(CATEGORIA, "ItemLog: " + itemLog.toString());

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
