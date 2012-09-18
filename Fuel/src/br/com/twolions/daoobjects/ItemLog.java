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

	private long id;
	private long id_car;
	private String date;
	private int type;
	private String subject;
	private double value_p;
	private double value_u;
	private long odometer;
	private String text;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public long getId_car() {
		return id_car;
	}

	public void setId_car(final long id_car) {
		this.id_car = id_car;
	}

	public String getDate() {
		return date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public double getValue_p() {
		return value_p;
	}

	public void setValue_p(final double value_p) {
		this.value_p = value_p;
	}

	public double getValue_u() {
		return value_u;
	}

	public void setValue_u(final double value_u) {
		this.value_u = value_u;
	}

	public long getOdometer() {
		return odometer;
	}

	public void setOdometer(final long odometer) {
		this.odometer = odometer;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

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

	public ItemLog(final long id_car, final String date, final int type,
			final String subject, final double value_p, final double value_u,
			final long odometer, final String text) {
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
	public ItemLog(final long id, final long id_car, final String date,
			final int type, final String subject, final double value_p,
			final double value_u, final long odometer, final String text) {
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

	@Override
	public String toString() {
		return "Id: " + id + ", Id_Car: " + id_car + ", Date: " + date
				+ ", Value_u: " + value_u + ", Value_p: " + value_p
				+ ", Odometer: " + odometer + ", Subject: " + subject
				+ ", Text: " + text + ", Type: " + type;
	}
}
