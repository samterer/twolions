package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.twolions.daoobjects.Expense;

public class ExpenseDAO extends ManagerDAO {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String NOME_BANCO = "db_itsmycar";
	// Nome da tabela
	public static final String NOME_TABELA = "expense";

	public ExpenseDAO(Context ctx) {
		super(ctx, NOME_BANCO, NOME_TABELA);
	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(Expense expense) {
		long id = expense.id;

		if (id != 0) {
			atualizar(expense);
		} else {
			// Insere novo
			id = inserir(expense);
		}

		return id;
	}

	public long inserir(Expense expense) {
		ContentValues values = new ContentValues();
		values.put(Expense.ID_CAR, expense.id_car);
		values.put(Expense.DATE, String.valueOf(expense.date));
		values.put(Expense.SUBJECT, expense.subject);
		values.put(Expense.VALUE, expense.value);
		values.put(Expense.TIPO, expense.tipo);

		long id = inserir(values);
		return id;
	}

	public int atualizar(Expense expense) {
		ContentValues values = new ContentValues();
		values.put(Expense.ID_CAR, expense.id_car);
		values.put(Expense.DATE, String.valueOf(expense.date));
		values.put(Expense.SUBJECT, expense.subject);
		values.put(Expense.VALUE, expense.value);
		values.put(Expense.TIPO, expense.tipo);

		String _id = String.valueOf(expense.id);

		String where = Expense._ID + "=?";
		String[] whereArgs = new String[]{_id};

		int count = atualizar(values, where, whereArgs);

		return count;
	}

	public int deletar(long id) {
		String where = Expense._ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[]{_id};

		int count = deletar(where, whereArgs);

		return count;
	}

	public Expense buscarExpense(long id) {
		// select * from Expense where _id=?
		Cursor c = dbCon.getInstance().query(true, NOME_TABELA,
				Expense.colunas, Expense._ID + "=" + id, null, null, null,
				null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			Expense expense = new Expense();

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
	public List<Expense> listarExpenses(long id_Car) {
		Cursor c = dbCon.getInstance().query(NOME_TABELA, Expense.colunas,
				Expense.ID_CAR + "='" + id_Car + "'", null, null, null, null);

		List<Expense> expenses = new ArrayList<Expense>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(Expense._ID);
			int idxIdCar = c.getColumnIndex(Expense.ID_CAR);
			int idxIdDate = c.getColumnIndex(Expense.DATE);
			int idxIdSubject = c.getColumnIndex(Expense.SUBJECT);
			int idxIdValue = c.getColumnIndex(Expense.VALUE);
			int idxIdTipo = c.getColumnIndex(Expense.TIPO);

			// Loop até o final
			do {
				Expense expense = new Expense();
				expenses.add(expense);

				// recupera os atributos de carro
				expense.id = c.getLong(idxId);
				expense.id_car = c.getLong(idxIdCar);
				expense.date = c.getString(idxIdDate);
				expense.subject = c.getString(idxIdSubject);
				expense.value = c.getDouble(idxIdValue);
				expense.tipo = c.getString(idxIdTipo);

			} while (c.moveToNext());
		}

		return expenses;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (dbCon != null) {
			dbCon.getInstance().close();
		}
	}
}
