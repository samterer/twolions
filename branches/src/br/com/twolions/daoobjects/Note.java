package br.com.twolions.daoobjects;

import android.provider.BaseColumns;

public class Note implements BaseColumns {

	public static String[] colunas = new String[]{Note._ID, Note.ID_CAR,
			Note.DATE, Note.SUBJECT, Note.TEXT};

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	public static final String AUTHORITY = "br.com.twolions";

	public long id;
	public long id_car;
	public String date;
	public String subject;
	public String text;

	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	public static final String ID_CAR = "id_car";
	public static final String DATE = "date";
	public static final String SUBJECT = "subject";
	public static final String TEXT = "text";

	public Note() {
	}

	public Note(long id_car, String date, String subject, String text) {
		super();
		this.id_car = id_car;
		this.date = date;
		this.subject = subject;
		this.text = text;
	}

	public Note(long id, long id_car, String date, String subject, String text) {
		super();
		this.id = id;
		this.id_car = id_car;
		this.date = date;
		this.subject = subject;
		this.text = text;
	}

	@Override
	public String toString() {
		return "Date: " + date + ", Subject: " + subject + ", Text: " + text;
	}
}
