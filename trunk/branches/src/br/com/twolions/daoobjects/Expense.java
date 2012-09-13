package br.com.twolions.daoobjects;

import android.provider.BaseColumns;

public class Expense implements BaseColumns {

	public static String[] colunas = new String[]{Expense._ID, Expense.ID_CAR,
			Expense.DATE, Expense.SUBJECT, Expense.TIPO};

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	public static final String AUTHORITY = "br.com.twolions";

	public long id;
	public long id_car;
	public String date;
	public String subject;
	public double value;
	public String tipo;

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	public static final String ID_CAR = "id_car";
	public static final String DATE = "date";
	public static final String SUBJECT = "subject";
	public static final String VALUE = "value";
	public static final String TIPO = "tipo";

	public Expense() {
	}

	public Expense(long id_car, String date, String subject, double value,
			String tipo) {
		super();
		this.id_car = id_car;
		this.date = date;
		this.subject = subject;
		this.value = value;
		this.tipo = tipo;
	}

	public Expense(long id, long id_car, String date, String subject,
			double value, String tipo) {
		super();
		this.id = id;
		this.id_car = id_car;
		this.date = date;
		this.subject = subject;
		this.value = value;
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Date: " + date + ", Subject: " + subject + ", Value: " + value
				+ ", Tipo: " + tipo;
	}
}
