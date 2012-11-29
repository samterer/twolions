package br.com.maboo.neext.modelobj;

import java.io.Serializable;

import android.provider.BaseColumns;
import br.com.maboo.neext.util.Constants;

public class ItemNote implements BaseColumns, Serializable {

	public static String[] colunas = new String[] { ItemNote._ID,
			ItemNote.TYPE, ItemNote.DATE, ItemNote.SUBJECT, ItemNote.TEXT, ItemNote.CHECK, ItemNote.DATE_NOTIFICATION };

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	// public static final String AUTHORITY = Constants.AUTHORITY;

	private long id;
	// a date deve sempre estar no formato
	// dd/mm/aaaa-hh:mm
	private String type;
	private String date;
	private String subject;
	private String text;

	// check_item
	private boolean check_item;

	// notification
	// dd/mm/aaaa-hh:mm
	private String date_notification;

	private static final long serialVersionUID = 6601006766832473959L;
	public static final String KEY = "itemNote";

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	// check_item
	public boolean isCheck() {
		return check_item;
	}

	public void setCheck(boolean check) {
		this.check_item = check;
	}

	// notification
	public String getDate_notification() {
		return date_notification;
	}

	public void setDate_notification(String date_notification) {
		this.date_notification = date_notification;
	}

	public static final String DEFAULT_SORT_ORDER = Constants.DEFAULT_SORT_ORDER;

	public static final String TYPE = "type";

	public static final String DATE = "date";

	public static final String SUBJECT = "subject";

	public static final String TEXT = "text";

	// check_item
	public static final String CHECK = "check_item";

	// notification
	public static final String DATE_NOTIFICATION = "date_notification";

	public ItemNote() {
	}

	public ItemNote(final String type, final String date, final String subject,
			final String text, final boolean check,
			final String date_notification) {
		super();

		this.type = type;

		this.date = date;

		this.subject = subject;

		this.text = text;

		this.check_item = check;

		this.date_notification = date_notification;

	}

	public ItemNote(final long id, final String type, final String date,
			final String subject, final String text, final boolean check,
			final String date_notification) {
		super();

		this.id = id;

		this.type = type;

		this.date = date;

		this.subject = subject;

		this.text = text;

		this.check_item = check;

		this.date_notification = date_notification;
	}

	public String toString() {
		return "Id: " + id + ", Type: " + type + ", Date: " + date
				+ ", Subject: " + subject + ", Text: " + text + ", Check: " + check_item + ", Date_notification: " + date_notification;
	}
}
