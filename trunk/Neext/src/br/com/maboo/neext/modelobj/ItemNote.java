package br.com.maboo.neext.modelobj;

import java.io.Serializable;

import android.provider.BaseColumns;

public class ItemNote implements BaseColumns, Serializable {

	public static String[] colunas = new String[] { ItemNote._ID,
			ItemNote.DATE, ItemNote.SUBJECT, ItemNote.TEXT };

	/**
	 * Pacote do Content Provider. Precisa ser �nico.
	 */
	// public static final String AUTHORITY = Constants.AUTHORITY;

	private long id;
	// a date deve sempre estar no formato
	// dd/mm/aaaa-hh:mm
	private String date;
	private String subject;
	private String text;

	private static final long serialVersionUID = 6601006766832473959L;
	public static final String KEY = "itemlog";

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	// public static final String DEFAULT_SORT_ORDER =
	// Constants.DEFAULT_SORT_ORDER;

	public static final String DATE = "date";

	public static final String SUBJECT = "subject";

	public static final String TEXT = "text";

	public ItemNote() {
	}

	public ItemNote(final String date, final String subject, final String text) {
		super();

		this.date = date;

		this.subject = subject;

		this.text = text;

	}

	public ItemNote(final long id, final String date, final String subject,
			final String text) {
		super();

		this.id = id;

		this.date = date;

		this.subject = subject;

		this.text = text;
	}

	@Override
	public String toString() {
		return "Id: " + id + ", Date: " + date + ", Subject: " + subject
				+ ", Text: " + text;
	}
}
