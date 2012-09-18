package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.twolions.daoobjects.Expense;
import br.com.twolions.sql.DBConnection;

public class ExpenseDAO extends DBConnection {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String base_name = "db_itsmycar";
	// Nome da tabela
	public static final String table_name = "expense";

	// private ManagerDAO dao;

	public ExpenseDAO(final Context ctx) {
		super(ctx, base_name);
		// dao = new ManagerDAO(ctx, base_name, table_name, db);
	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(final Expense expense) {
		long id = expense.id;

		if (id != 0) {
			atualizar(expense);
		} else {
			// Insere novo
			id = inserir(expense);
		}

		return id;
	}

	public long inserir(final Expense expense) {
		final ContentValues values = new ContentValues();
		values.put(Expense.ID_CAR, expense.id_car);
		values.put(Expense.DATE, String.valueOf(expense.date));
		values.put(Expense.SUBJECT, expense.subject);
		values.put(Expense.VALUE, expense.value);
		values.put(Expense.TIPO, expense.tipo);

		final long id = inserir(values, table_name);
		return id;
	}

	public int atualizar(final Expense expense) {
		final ContentValues values = new ContentValues();
		values.put(Expense.ID_CAR, expense.id_car);
		values.put(Expense.DATE, String.valueOf(expense.date));
		values.put(Expense.SUBJECT, expense.subject);
		values.put(Expense.VALUE, expense.value);
		values.put(Expense.TIPO, expense.tipo);

		final String _id = String.valueOf(expense.id);

		final String where = Expense._ID + "=?";
		final String[] whereArgs = new String[]{_id};

		final int count = atualizar(values, where, whereArgs, table_name);

		return count;
	}

	public int deletar(final long id) {
		final String where = Expense._ID + "=?";

		final String _id = String.valueOf(id);
		final String[] whereArgs = new String[]{_id};

		final int count = deletar(where, whereArgs, table_name);

		return count;
	}

	public Expense buscarExpense(final long id) {
		// select * from Expense where _id=?
		final Cursor c = db.query(true, table_name, Expense.colunas,
				Expense._ID + "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			final Expense expense = new Expense();

			// Lê os dados
			expense.id = c.getLong(0);
			expense.id_car = c.getLong(1);
			expense.date = c.getString(2);
			expense.subject = c.getString(3);
			expense.value = c.getDouble(4);
			expense.tipo = c.getString(5);

			return expense;
		}

		return null;
	}

	// Retorna uma lista com todos os Expenses pelo id do carro
	public List<Expense> listarExpenses(final long id_Car) {
		final Cursor c = db.query(table_name, Expense.colunas, Expense.ID_CAR
				+ "='" + id_Car + "'", null, null, null, null);

		final List<Expense> expenses = new ArrayList<Expense>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			final int idxId = c.getColumnIndex(Expense._ID);
			final int idxIdCar = c.getColumnIndex(Expense.ID_CAR);
			final int idxIdDate = c.getColumnIndex(Expense.DATE);
			final int idxIdSubject = c.getColumnIndex(Expense.SUBJECT);
			final int idxIdValue = c.getColumnIndex(Expense.VALUE);
			final int idxIdTipo = c.getColumnIndex(Expense.TIPO);

			// Loop até o final
			do {
				final Expense expense = new Expense();
				expenses.add(expense);

				// recupera os atributos de carro
				expense.id = c.getLong(idxId);
				expense.id_car = c.getLong(idxIdCar);
				expense.date = c.getString(idxIdDate);
				expense.subject = c.getString(idxIdSubject);
				expense.value = c.getDouble(idxIdValue);
				expense.tipo = c.getString(idxIdTipo);

				Log.i(CATEGORIA, "Expense: " + expense.toString());

			} while (c.moveToNext());
		}

		return expenses;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
