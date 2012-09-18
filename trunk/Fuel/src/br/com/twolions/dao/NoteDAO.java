package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.twolions.daoobjects.Note;
import br.com.twolions.sql.DBConnection;

public class NoteDAO extends DBConnection {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String base_name = "db_itsmycar";
	// Nome da tabela
	public static final String table_name = "note";

	// private ManagerDAO dao;

	public NoteDAO(final Context ctx) {
		super(ctx, base_name);
		// dao = new ManagerDAO(ctx, base_name, table_name, db);
	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(final Note note) {
		long id = note.id;

		if (id != 0) {
			atualizar(note);
		} else {
			// Insere novo
			id = inserir(note);
		}

		return id;
	}

	public long inserir(final Note note) {
		final ContentValues values = new ContentValues();
		values.put(Note.ID_CAR, note.id_car);
		values.put(Note.DATE, String.valueOf(note.date));
		values.put(Note.SUBJECT, note.subject);
		values.put(Note.TEXT, note.text);

		final long id = inserir(values, table_name);
		return id;
	}

	public int atualizar(final Note note) {
		final ContentValues values = new ContentValues();
		values.put(Note.ID_CAR, note.id_car);
		values.put(Note.DATE, String.valueOf(note.date));
		values.put(Note.SUBJECT, note.subject);
		values.put(Note.TEXT, note.text);

		final String _id = String.valueOf(note.id);

		final String where = Note._ID + "=?";
		final String[] whereArgs = new String[]{_id};

		final int count = atualizar(values, where, whereArgs, table_name);

		return count;
	}

	public int deletar(final long id) {
		final String where = Note._ID + "=?";

		final String _id = String.valueOf(id);
		final String[] whereArgs = new String[]{_id};

		final int count = deletar(where, whereArgs, table_name);

		return count;
	}

	public Note buscarNote(final long id) {
		// select * from note where _id=?
		final Cursor c = db.query(true, table_name, Note.colunas, Note._ID
				+ "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			final Note note = new Note();

			// L� os dados
			note.id = c.getLong(0);
			note.id_car = c.getLong(1);
			note.date = c.getString(2);
			note.subject = c.getString(3);
			note.text = c.getString(4);

			return note;
		}

		return null;
	}

	// Retorna uma lista com todos os notes pelo id do carro
	public List<Note> listarNotes(final long id_Car) {
		final Cursor c = db.query(table_name, Note.colunas, Note.ID_CAR + "='"
				+ id_Car + "'", null, null, null, null);

		final List<Note> notes = new ArrayList<Note>();

		if (c.moveToFirst()) {

			// Recupera os �ndices das colunas
			final int idxId = c.getColumnIndex(Note._ID);
			final int idxIdCar = c.getColumnIndex(Note.ID_CAR);
			final int idxIdDate = c.getColumnIndex(Note.DATE);
			final int idxIdSubject = c.getColumnIndex(Note.SUBJECT);
			final int idxIdText = c.getColumnIndex(Note.TEXT);

			// Loop at� o final
			do {
				final Note note = new Note();
				notes.add(note);

				// recupera os atributos de carro
				note.id = c.getLong(idxId);
				note.id_car = c.getLong(idxIdCar);
				note.date = c.getString(idxIdDate);
				note.subject = c.getString(idxIdSubject);
				note.text = c.getString(idxIdText);

				Log.i(CATEGORIA, "Expense: " + note.toString());

			} while (c.moveToNext());
		}

		return notes;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}
}
