package br.com.twolions.daoobjects;

import android.provider.BaseColumns;

public class ItemLog implements BaseColumns {

	public static String[] colunas = new String[]{ItemLog._ID, ItemLog.ID_CAR,
			ItemLog.DATE, ItemLog.VALUE_U, ItemLog.VALUE_P, ItemLog.ODOMETER,
			ItemLog.SUBJECT, ItemLog.TYPE, Note.TEXT};

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	public static final String AUTHORITY = "br.com.twolions";

	public long id;
	public long id_car;
	public String date;
	public int type;
	public String subject;
	public double value_p;
	public double value_u;
	public long odometer;
	public String text;

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	public static final String ID_CAR = "id_car";
	public static final String DATE = "date";
	public static final String TYPE = "type";
	public static final String SUBJECT = "subject";
	public static final String VALUE_P = "value_p";
	public static final String VALUE_U = "value_u";
	public static final String ODOMETER = "odometer";
	public static final String TEXT = "text";

	public ItemLog() {
	}

	public ItemLog(long id_car, String date, int type, String subject,
			double value_p, double value_u, long odometer, String text) {
		super();
		this.id_car = id_car;
		this.date = date;
		this.type = type;
		this.subject = subject;
		this.value_p = value_p;
		this.value_u = value_u;
		this.odometer = odometer;
		this.text = text;

	}
	public ItemLog(long id, long id_car, String date, int type, String subject,
			double value_p, double value_u, long odometer, String text) {
		super();
		this.id = id;
		this.id_car = id_car;
		this.date = date;
		this.type = type;
		this.subject = subject;
		this.value_p = value_p;
		this.value_u = value_u;
		this.odometer = odometer;
		this.text = text;
	}

	public String toString() {
		return "Date: " + date + ", Value_u: " + value_u + ", Value_p: "
				+ value_p + ", Odometer: " + odometer + ", Subject: " + subject
				+ ", Text: " + text + ", Type: " + type;
	}
}
