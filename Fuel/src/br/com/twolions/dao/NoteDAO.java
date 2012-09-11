package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.twolions.daoobjects.Note;

public class NoteDAO extends ManagerDAO {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String NOME_BANCO = "db_itsmycar";
	// Nome da tabela
	public static final String NOME_TABELA = "note";

	public NoteDAO(Context ctx) {
		super(ctx, NOME_BANCO, NOME_TABELA);
	}

	// Salva o carro, insere um novo ou atualiza
	public long salvar(Note note) {
		long id = note.id;

		if (id != 0) {
			atualizar(note);
		} else {
			// Insere novo
			id = inserir(note);
		}

		return id;
	}

	public long inserir(Note note) {
		ContentValues values = new ContentValues();
		values.put(Note.ID_CAR, note.id_car);
		values.put(Note.DATE, String.valueOf(note.date));
		values.put(Note.SUBJECT, note.subject);
		values.put(Note.TEXT, note.text);

		long id = inserir(values);
		return id;
	}

	public int atualizar(Note note) {
		ContentValues values = new ContentValues();
		values.put(Note.ID_CAR, note.id_car);
		values.put(Note.DATE, String.valueOf(note.date));
		values.put(Note.SUBJECT, note.subject);
		values.put(Note.TEXT, note.text);

		String _id = String.valueOf(note.id);

		String where = Note._ID + "=?";
		String[] whereArgs = new String[]{_id};

		int count = atualizar(values, where, whereArgs);

		return count;
	}

	public int deletar(long id) {
		String where = Note._ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[]{_id};

		int count = deletar(where, whereArgs);

		return count;
	}

	public Note buscarNote(long id) {
		// select * from note where _id=?
		Cursor c = dbCon.getInstance().query(true, NOME_TABELA, Note.colunas,
				Note._ID + "=" + id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			Note note = new Note();

			// Lê os dados
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
	public List<Note> listarNotes(long id_Car) {
		Cursor c = dbCon.getInstance().query(NOME_TABELA, Note.colunas,
				Note.ID_CAR + "='" + id_Car + "'", null, null, null, null);

		List<Note> notes = new ArrayList<Note>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(Note._ID);
			int idxIdCar = c.getColumnIndex(Note.ID_CAR);
			int idxIdDate = c.getColumnIndex(Note.DATE);
			int idxIdSubject = c.getColumnIndex(Note.SUBJECT);
			int idxIdText = c.getColumnIndex(Note.TEXT);

			// Loop até o final
			do {
				Note note = new Note();
				notes.add(note);

				// recupera os atributos de carro
				note.id = c.getLong(idxId);
				note.id_car = c.getLong(idxIdCar);
				note.date = c.getString(idxIdDate);
				note.subject = c.getString(idxIdSubject);
				note.text = c.getString(idxIdText);

			} while (c.moveToNext());
		}

		return notes;
	}

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (dbCon != null) {
			dbCon.getInstance().close();
		}
	}
}
