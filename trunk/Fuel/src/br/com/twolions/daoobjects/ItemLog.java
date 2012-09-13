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
	public double value_u;
	public double value_p;
	public long odometer;
	public String subject;
	public String type;
	public String text;

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	public static final String ID_CAR = "id_car";
	public static final String DATE = "date";
	public static final String VALUE_U = "value_u";
	public static final String VALUE_P = "value_p";
	public static final String ODOMETER = "odometer";
	public static final String SUBJECT = "subject";
	public static final String TEXT = "text";
	public static final String TYPE = "type";

	public ItemLog() {
	}

	public ItemLog(long id_car, String date, double value_u, double value_p,
			long odometer, String subject, String text, String type) {
		super();
		this.id_car = id_car;
		this.date = date;
		this.value_u = value_u;
		this.value_p = value_p;
		this.odometer = odometer;
		this.type = type;
		this.subject = subject;
		this.text = text;

	}
	public ItemLog(long id, long id_car, String date, double value_u,
			double value_p, long odometer, String subject, String text,
			String type) {
		super();
		this.id = id;
		this.id_car = id_car;
		this.date = date;
		this.value_u = value_u;
		this.value_p = value_p;
		this.odometer = odometer;
		this.type = type;
		this.subject = subject;
		this.text = text;
	}

	public String toString() {
		return "Date: " + date + ", Value_u: " + value_u + ", Value_p: "
				+ value_p + ", Odometer: " + odometer + ", Subject: " + subject
				+ ", Text: " + text + ", Type: " + type;
	}
}
