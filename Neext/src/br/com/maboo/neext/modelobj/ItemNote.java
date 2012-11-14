package br.com.maboo.neext.modelobj;

import java.io.Serializable;

import android.provider.BaseColumns;
import br.com.maboo.neext.util.Constants;

public class ItemNote implements BaseColumns, Serializable {

	public static String[] colunas = new String[] { ItemNote._ID,
			ItemNote.TYPE, ItemNote.DATE, ItemNote.SUBJECT, ItemNote.TEXT };

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	// public static final String AUTHORITY = Constants.AUTHORITY;

	private long id;
	// a date deve sempre estar no formato
	// dd/mm/aaaa-hh:mm
	private int type;
	private String date;
	private String subject;
	private String text;

	private static final long serialVersionUID = 6601006766832473959L;
	public static final String KEY = "itemNote";

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(final int type) {
		this.type = type;
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

	public static final String DEFAULT_SORT_ORDER = Constants.DEFAULT_SORT_ORDER;

	public static final String TYPE = "type";

	public static final String DATE = "date";

	public static final String SUBJECT = "subject";

	public static final String TEXT = "text";

	public ItemNote() {
	}

	public ItemNote(final int type, final String date, final String subject,
			final String text) {
		super();

		this.type = type;

		this.date = date;

		this.subject = subject;

		this.text = text;

	}

	public ItemNote(final long id, final int type, final String date,
			final String subject, final String text) {
		super();

		this.id = id;

		this.type = type;

		this.date = date;

		this.subject = subject;

		this.text = text;
	}

	public String toString() {
		return "Id: " + id + ", Type: " + type + ", Date: " + date
				+ ", Subject: " + subject + ", Text: " + text;
	}
}
