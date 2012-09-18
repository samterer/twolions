package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.twolions.daoobjects.Fuel;
import br.com.twolions.sql.DBConnection;

public class FuelDAO extends DBConnection {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String base_name = "db_itsmycar";
	// Nome da tabela
	public static final String table_name = "fuel";

	// private ManagerDAO dao;

	public FuelDAO(final Context ctx) {
		super(ctx, base_name);
		// dao = new ManagerDAO(ctx, base_name, table_name, db);
	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(final Fuel fuel) {
		long id = fuel.id;

		if (id != 0) {
			atualizar(fuel);
		} else {
			// Insere novo
			id = inserir(fuel);
		}

		return id;
	}

	public long inserir(final Fuel fuel) {
		final ContentValues values = new ContentValues();
		values.put(Fuel.ID_CAR, fuel.id_car);
		values.put(Fuel.DATE, String.valueOf(fuel.date));
		values.put(Fuel.VALUE_U, fuel.value_u);
		values.put(Fuel.VALUE_P, fuel.value_p);
		values.put(Fuel.ODOMETER, fuel.odometer);

		final long id = inserir(values, table_name);
		return id;
	}

	public int atualizar(final Fuel fuel) {
		final ContentValues values = new ContentValues();
		values.put(Fuel.ID_CAR, fuel.id_car);
		values.put(Fuel.DATE, String.valueOf(fuel.date));
		values.put(Fuel.VALUE_U, fuel.value_u);
		values.put(Fuel.VALUE_P, fuel.value_p);
		values.put(Fuel.ODOMETER, fuel.odometer);

		final String _id = String.valueOf(fuel.id);

		final String where = Fuel._ID + "=?";
		final String[] whereArgs = new String[]{_id};

		final int count = atualizar(values, where, whereArgs, table_name);

		return count;
	}

	public int deletar(final long id) {
		final String where = Fuel._ID + "=?";

		final String _id = String.valueOf(id);
		final String[] whereArgs = new String[]{_id};

		final int count = deletar(where, whereArgs, table_name);

		return count;
	}

	public Fuel buscarFuel(final long id) {
		// select * from Fuel where _id=?
		final Cursor c = db.query(true, table_name, Fuel.colunas, Fuel._ID
				+ "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			final Fuel fuel = new Fuel();

			// Lê os dados
			fuel.id = c.getLong(0);
			fuel.id_car = c.getLong(1);
			fuel.date = c.getString(2);
			fuel.value_u = c.getDouble(3);
			fuel.value_p = c.getDouble(4);
			fuel.odometer = c.getLong(5);

			return fuel;
		}

		return null;
	}

	// Retorna uma lista com todos os Fuels pelo id do carro
	public List<Fuel> listarFuels(final long id_Car) {
		final Cursor c = db.query(table_name, Fuel.colunas, Fuel.ID_CAR + "='"
				+ id_Car + "'", null, null, null, null);

		final List<Fuel> fuels = new ArrayList<Fuel>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			final int idxId = c.getColumnIndex(Fuel._ID);
			final int idxIdCar = c.getColumnIndex(Fuel.ID_CAR);
			final int idxIdDate = c.getColumnIndex(Fuel.DATE);
			final int idxIdValueU = c.getColumnIndex(Fuel.VALUE_U);
			final int idxIdValueP = c.getColumnIndex(Fuel.VALUE_P);
			final int idxIdOdometer = c.getColumnIndex(Fuel.ODOMETER);

			// Loop até o final
			do {
				final Fuel fuel = new Fuel();
				fuels.add(fuel);

				// recupera os atributos de carro
				fuel.id = c.getLong(idxId);
				fuel.id_car = c.getLong(idxIdCar);
				fuel.date = c.getString(idxIdDate);
				fuel.value_u = c.getDouble(idxIdValueU);
				fuel.value_p = c.getDouble(idxIdValueP);
				fuel.odometer = c.getLong(idxIdOdometer);

				Log.i(CATEGORIA, "Expense: " + fuel.toString());

			} while (c.moveToNext());
		}

		return fuels;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
