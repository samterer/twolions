package br.com.twolions.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.Carro.Carros;
import br.com.twolions.sql.DBConnection;

public class CarroDAO extends DBConnection {
	private static final String CATEGORIA = "base";

	// Nome do banco
	private static final String base_name = "db_itsmycar";
	// Nome da tabela
	public static final String table_name = "carro";

	// protected SQLiteDatabase db;

	// protected SQLiteDatabase db;

	// private ManagerDAO dao;

	public CarroDAO(Context ctx) {
		super(ctx, base_name);
		// Abre o banco de dados já existente
		// try {
		// db = ctx.openOrCreateDatabase(base_name, Context.MODE_PRIVATE,
		// null);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// db = new DBConnection(ctx, base_name);
		// dao = new ManagerDAO(ctx, base_name, table_name, db);
	}
	// Salva o carro, insere um novo ou atualiza
	public long salvar(Carro carro) {
		long id = carro.getId();

		if (id != 0) {
			atualizar(carro);
		} else {
			// Insere novo
			id = inserir(carro);
		}

		return id;
	}

	// Insere um novo carro
	public long inserir(Carro carro) {
		ContentValues values = new ContentValues();
		values.put(Carros.NOME, carro.getNome());
		values.put(Carros.PLACA, carro.getPlaca());
		values.put(Carros.TIPO, carro.getTipo());

		long id = inserir(values, table_name);
		return id;
	}

	// Insere um novo carro
	// public long inserir(ContentValues valores) {
	// long id = db.insert(table_name, "", valores);
	// return id;
	// }

	// Atualiza o carro no banco. O id do carro é utilizado.
	public int atualizar(Carro carro) {
		ContentValues values = new ContentValues();
		values.put(Carros.NOME, carro.getNome());
		values.put(Carros.PLACA, carro.getPlaca());
		values.put(Carros.TIPO, carro.getTipo());

		String _id = String.valueOf(carro.getId());

		String where = Carros._ID + "=?";
		String[] whereArgs = new String[]{_id};

		int count = atualizar(values, where, whereArgs, table_name);

		return count;
	}

	// Atualiza o carro com os valores abaixo
	// A cláusula where é utilizada para identificar o carro a ser atualizado
	// public int atualizar(ContentValues valores, String where, String[]
	// whereArgs) {
	// int count = db.update(table_name, valores, where, whereArgs);
	// Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
	// return count;
	// }

	// Deleta o carro com o id fornecido
	public int deletar(long id) {
		String where = Carros._ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[]{_id};

		int count = deletar(where, whereArgs, table_name);

		return count;
	}

	// Deleta o carro com os argumentos fornecidos
	// public int deletar(String where, String[] whereArgs) {
	// int count = db.delete(table_name, where, whereArgs);
	// Log.i(CATEGORIA, "Deletou [" + count + "] registros");
	// return count;
	// }

	// Busca o carro pelo id
	public Carro buscarCarro(long id) {
		// select * from carro where _id=?
		Cursor c = db.query(true, table_name, Carro.colunas, Carros._ID + "="
				+ id, null, null, null, null, null);

		if (c.getCount() > 0) {

			// Posicinoa no primeiro elemento do cursor
			c.moveToFirst();

			Carro carro = new Carro();

			// Lê os dados
			carro.setId(c.getLong(0));
			carro.setNome(c.getString(1));
			carro.setPlaca(c.getString(2));
			carro.setTipo(c.getString(3));

			return carro;
		}

		return null;
	}

	// Retorna um cursor com todos os carros
	public Cursor getCursor() {
		try {
			return db.query(table_name, Carro.colunas, null, null, null, null,
					null, null);
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar os carros: " + e.toString());
			return null;
		}
	}

	// Retorna uma lista com todos os carros
	public List<Carro> listarCarros() {
		Cursor c = getCursor();

		List<Carro> carros = new ArrayList<Carro>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(Carros._ID);
			int idxNome = c.getColumnIndex(Carros.NOME);
			int idxPlaca = c.getColumnIndex(Carros.PLACA);
			int idxTipo = c.getColumnIndex(Carros.TIPO);

			// Loop até o final
			do {
				Carro carro = new Carro();
				carros.add(carro);

				// recupera os atributos de carro
				carro.setId(c.getLong(idxId));
				carro.setNome(c.getString(idxNome));
				carro.setPlaca(c.getString(idxPlaca));
				carro.setTipo(c.getString(idxTipo));

				Log.i(CATEGORIA, "Carro: " + carro.toString());

			} while (c.moveToNext());
		}

		return carros;
	}

	// Busca o carro pelo nome "select * from carro where nome=?"
	public Carro buscarCarroPorNome(String nome) {
		Carro carro = null;

		try {
			// Idem a: SELECT _id,nome,placa,ano from CARRO where nome = ?
			Cursor c = db.query(table_name, Carro.colunas, Carros.NOME + "='"
					+ nome + "'", null, null, null, null);

			// Se encontrou...
			if (c.moveToNext()) {

				carro = new Carro();

				// utiliza os métodos getLong(), getString(), getInt(), etc para
				// recuperar os valores
				carro.setId(c.getLong(0));
				carro.setNome(c.getString(1));
				carro.setPlaca(c.getString(2));
				carro.setTipo(c.getString(3));
			}
		} catch (SQLException e) {
			Log.e(CATEGORIA,
					"Erro ao buscar o carro pelo nome: " + e.toString());
			return null;
		}

		return carro;
	}

	// Busca um carro utilizando as configurações definidas no
	// SQLiteQueryBuilder
	// Utilizado pelo Content Provider de carro
	// public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
	// String selection, String[] selectionArgs, String groupBy,
	// String having, String orderBy) {
	// Cursor c = queryBuilder.query(db, projection, selection, selectionArgs,
	// groupBy, having, orderBy);
	// return c;
	// }

	// Fecha o banco
	public void fechar() {
		// fecha o banco de dados
		if (db != null) {
			db.close();
		}
	}

}
