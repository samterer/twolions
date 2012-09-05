package br.com.twolions.daoobjects;

import android.provider.BaseColumns;

public class Fuel implements BaseColumns {

	public static String[] colunas = new String[]{Fuel._ID, Fuel.ID_CAR,
			Fuel.DATE, Fuel.VALUE_U, Fuel.VALUE_P, Fuel.ODOMETER};

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	public static final String AUTHORITY = "br.com.twolions";

	public long id;
	public long id_car;
	public String date;
	public double value_u;
	public double value_p;
	public long odometer;

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	public static final String ID_CAR = "id_car";
	public static final String DATE = "date";
	public static final String VALUE_U = "value_u";
	public static final String VALUE_P = "value_p";
	public static final String ODOMETER = "odometer";

	public Fuel() {
	}

	public Fuel(long id_car, String date, double value_u, double value_p,
			long odometer) {
		super();
		this.id_car = id_car;
		this.date = date;
		this.value_u = value_u;
		this.value_p = value_p;
		this.odometer = odometer;
	}
	public Fuel(long id, long id_car, String date, double value_u,
			double value_p, long odometer) {
		super();
		this.id = id;
		this.id_car = id_car;
		this.date = date;
		this.value_u = value_u;
		this.value_p = value_p;
		this.odometer = odometer;
	}

	@Override
	public String toString() {
		return "Date: " + date + ", Value_u: " + value_u + ", Value_p: "
				+ value_p + ", Odometer: " + odometer;
	}
}
