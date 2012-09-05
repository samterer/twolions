package br.com.twolions.daoobjects;

import android.provider.BaseColumns;

/**
 * Classe entidade para armazenar os valores de Carro
 * 
 * 
 */
public class Carro {

	public static String[] colunas = new String[]{Carros._ID, Carros.NOME,
			Carros.PLACA, Carros.TIPO};

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	public static final String AUTHORITY = "br.com.twolions";

	public long id;
	public String nome;
	public String placa;
	public String tipo;

	public Carro() {
	}

	public Carro(String nome, String placa, String tipo) {
		super();
		this.nome = nome;
		this.placa = placa;
		this.tipo = tipo;
	}

	public Carro(long id, String nome, String placa, String tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.placa = placa;
		this.tipo = tipo;
	}

	/**
	 * Classe interna para representar as colunas e ser utilizada por um Content
	 * Provider
	 * 
	 * Filha de BaseColumns que já define (_id e _count), para seguir o padrão
	 * Android
	 */
	public static final class Carros implements BaseColumns {

		// Não pode instanciar esta Classe
		private Carros() {
		}

		/*// content://br.livro.android.provider.carro/carros
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/carros");

		// Mime Type para todos os carros
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.carros";

		// Mime Type para um único carro
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.carros";
*/
		// Ordenação default para inserir no order by
		public static final String DEFAULT_SORT_ORDER = "_id ASC";

		public static final String NOME = "nome";
		public static final String PLACA = "placa";
		public static final String TIPO = "tipo";

		// Método que constrói uma Uri para um Carro específico, com o seu id
		// A Uri é no formato
		// "content://br.livro.android.provider.carro/carros/id"
		/*public static Uri getUriId(long id) {
			// Adiciona o id na URI default do /carros
			Uri uriCarro = ContentUris.withAppendedId(Carros.CONTENT_URI, id);
			return uriCarro;
		}*/
	}

	@Override
	public String toString() {
		return "Nome: " + nome + ", Placa: " + placa + ", Tipo: " + tipo;
	}
}
