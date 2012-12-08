package br.com.twolions.modelobj;

import android.provider.BaseColumns;

public class Note implements BaseColumns {

	public static String[] colunas = new String[]{Note._ID, Note.ID_CAR,
			Note.DATE, Note.SUBJECT, Note.TEXT};

	/**
	 * Pacote do Content Provider. Precisa ser �nico.
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

	public Note(final long id_car, final String date, final String subject,
			final String text) {
		super();
		this.id_car = id_car;
		this.date = date;
		this.subject = subject;
		this.text = text;
	}

	public Note(final long id, final long id_car, final String date,
			final String subject, final String text) {
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