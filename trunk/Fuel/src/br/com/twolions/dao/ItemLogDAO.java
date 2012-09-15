package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.sql.DBConnection;

public class ItemLogDAO extends DBConnection {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String base_name = "db_itsmycar";
	// Nome da tabela
	public static final String table_name = "ItemLog";

	// private ManagerDAO dao;

	public ItemLogDAO(Context ctx) {
		super(ctx, base_name);
		// dao = new ManagerDAO(ctx, base_name, table_name, db);
	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(ItemLog itemLog) {
		long id = itemLog.getId();

		if (id != 0) {
			atualizar(itemLog);
		} else {
			// Insere novo
			id = inserir(itemLog);
		}

		return id;
	}

	public long inserir(ItemLog itemLog) {
		ContentValues values = new ContentValues();
		values.put(ItemLog.ID_CAR, itemLog.getId_car());
		values.put(ItemLog.DATE, String.valueOf(itemLog.getDate()));
		values.put(ItemLog.TYPE, itemLog.getType());
		values.put(ItemLog.SUBJECT, itemLog.getSubject());
		values.put(ItemLog.VALUE_P, itemLog.getValue_p());
		values.put(ItemLog.VALUE_U, itemLog.getValue_u());
		values.put(ItemLog.ODOMETER, itemLog.getOdometer());
		values.put(ItemLog.TEXT, itemLog.getText());

		long id = inserir(values, table_name);
		return id;
	}

	public int atualizar(ItemLog itemLog) {
		ContentValues values = new ContentValues();
		values.put(ItemLog.ID_CAR, itemLog.getId_car());
		values.put(ItemLog.DATE, String.valueOf(itemLog.getDate()));
		values.put(ItemLog.TYPE, itemLog.getType());
		values.put(ItemLog.SUBJECT, itemLog.getSubject());
		values.put(ItemLog.VALUE_P, itemLog.getValue_p());
		values.put(ItemLog.VALUE_U, itemLog.getValue_u());
		values.put(ItemLog.ODOMETER, itemLog.getOdometer());
		values.put(ItemLog.TEXT, itemLog.getText());

		String _id = String.valueOf(itemLog.getId_car());

		String where = ItemLog._ID + "=?";
		String[] whereArgs = new String[]{_id};

		int count = atualizar(values, where, whereArgs, table_name);

		return count;
	}

	public int deletar(long id) {
		String where = ItemLog._ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[]{_id};

		int count = deletar(where, whereArgs, table_name);

		return count;
	}

	public ItemLog buscarItemLog(long id) {
		// select * from ItemLog where _id=?
		Cursor c = db.query(true, table_name, ItemLog.colunas, ItemLog._ID
				+ "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			ItemLog itemLog = new ItemLog();

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

			return itemLog;
		}

		return null;
	}

	// Retorna uma lista com todos os ItemLogs pelo id do carro
	public List<ItemLog> listarItemLogs(long id_Car) {
		Cursor c = db.query(table_name, ItemLog.colunas, ItemLog.ID_CAR + "='"
				+ id_Car + "'", null, null, null, null);

		List<ItemLog> itemLogs = new ArrayList<ItemLog>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(ItemLog._ID);
			int idxIdCar = c.getColumnIndex(ItemLog.ID_CAR);
			int idxIdDate = c.getColumnIndex(ItemLog.DATE);
			int idxIdType = c.getColumnIndex(ItemLog.TYPE);
			int idxIdSubject = c.getColumnIndex(ItemLog.SUBJECT);
			int idxIdValueP = c.getColumnIndex(ItemLog.VALUE_P);
			int idxIdValueU = c.getColumnIndex(ItemLog.VALUE_U);
			int idxIdOdometer = c.getColumnIndex(ItemLog.ODOMETER);
			int idxIdText = c.getColumnIndex(ItemLog.TEXT);

			// Loop até o final
			do {
				ItemLog itemLog = new ItemLog();
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

	// retorna uma lista de itens pelo type
	// type:
	// - fuel - 0
	// - expense - 1
	// - note - 2
	// - repair - 3
	public List<ItemLog> listarItemLogs(String type_item) {
		Cursor c = db.query(table_name, ItemLog.colunas, ItemLog.TYPE + "='"
				+ type_item + "'", null, null, null, null);

		List<ItemLog> itemLogs = new ArrayList<ItemLog>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(ItemLog._ID);
			int idxIdCar = c.getColumnIndex(ItemLog.ID_CAR);
			int idxIdDate = c.getColumnIndex(ItemLog.DATE);
			int idxIdType = c.getColumnIndex(ItemLog.TYPE);
			int idxIdSubject = c.getColumnIndex(ItemLog.SUBJECT);
			int idxIdValueP = c.getColumnIndex(ItemLog.VALUE_P);
			int idxIdValueU = c.getColumnIndex(ItemLog.VALUE_U);
			int idxIdOdometer = c.getColumnIndex(ItemLog.ODOMETER);
			int idxIdText = c.getColumnIndex(ItemLog.TEXT);

			// Loop até o final
			do {
				ItemLog itemLog = new ItemLog();
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
