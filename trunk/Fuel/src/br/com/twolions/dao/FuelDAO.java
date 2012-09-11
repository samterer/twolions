package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.twolions.daoobjects.Fuel;

public class FuelDAO extends ManagerDAO {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String NOME_BANCO = "db_itsmycar";
	// Nome da tabela
	public static final String NOME_TABELA = "fuel";

	public FuelDAO(Context ctx) {
		super(ctx, NOME_BANCO, NOME_TABELA);
	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(Fuel fuel) {
		long id = fuel.id;

		if (id != 0) {
			atualizar(fuel);
		} else {
			// Insere novo
			id = inserir(fuel);
		}

		return id;
	}

	public long inserir(Fuel fuel) {
		ContentValues values = new ContentValues();
		values.put(Fuel.ID_CAR, fuel.id_car);
		values.put(Fuel.DATE, String.valueOf(fuel.date));
		values.put(Fuel.VALUE_U, fuel.value_u);
		values.put(Fuel.VALUE_P, fuel.value_p);
		values.put(Fuel.ODOMETER, fuel.odometer);

		long id = inserir(values);
		return id;
	}

	public int atualizar(Fuel fuel) {
		ContentValues values = new ContentValues();
		values.put(Fuel.ID_CAR, fuel.id_car);
		values.put(Fuel.DATE, String.valueOf(fuel.date));
		values.put(Fuel.VALUE_U, fuel.value_u);
		values.put(Fuel.VALUE_P, fuel.value_p);
		values.put(Fuel.ODOMETER, fuel.odometer);

		String _id = String.valueOf(fuel.id);

		String where = Fuel._ID + "=?";
		String[] whereArgs = new String[]{_id};

		int count = atualizar(values, where, whereArgs);

		return count;
	}

	public int deletar(long id) {
		String where = Fuel._ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[]{_id};

		int count = deletar(where, whereArgs);

		return count;
	}

	public Fuel buscarFuel(long id) {
		// select * from Fuel where _id=?
		Cursor c = dbCon.getInstance().query(true, NOME_TABELA, Fuel.colunas,
				Fuel._ID + "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			Fuel fuel = new Fuel();

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
	public List<Fuel> listarFuels(long id_Car) {
		Cursor c = dbCon.getInstance().query(NOME_TABELA, Fuel.colunas,
				Fuel.ID_CAR + "='" + id_Car + "'", null, null, null, null);

		List<Fuel> fuels = new ArrayList<Fuel>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(Fuel._ID);
			int idxIdCar = c.getColumnIndex(Fuel.ID_CAR);
			int idxIdDate = c.getColumnIndex(Fuel.DATE);
			int idxIdValueU = c.getColumnIndex(Fuel.VALUE_U);
			int idxIdValueP = c.getColumnIndex(Fuel.VALUE_P);
			int idxIdOdometer = c.getColumnIndex(Fuel.ODOMETER);

			// Loop até o final
			do {
				Fuel fuel = new Fuel();
				fuels.add(fuel);

				// recupera os atributos de carro
				fuel.id = c.getLong(idxId);
				fuel.id_car = c.getLong(idxIdCar);
				fuel.date = c.getString(idxIdDate);
				fuel.value_u = c.getDouble(idxIdValueU);
				fuel.value_p = c.getDouble(idxIdValueP);
				fuel.odometer = c.getLong(idxIdOdometer);

			} while (c.moveToNext());
		}

		return fuels;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (dbCon != null) {
			dbCon.getInstance().close();
		}
	}
}
