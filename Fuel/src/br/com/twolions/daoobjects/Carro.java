package br.com.twolions.daoobjects;

import java.io.Serializable;

import android.provider.BaseColumns;
import br.com.twolions.util.Constants;

/**
 * Classe entidade para armazenar os valores de Carro
 * 
 * 
 */
public class Carro implements BaseColumns, Serializable {

	public static String[] colunas = new String[]{Carro._ID, Carro.NOME,
			Carro.PLACA, Carro.TIPO};

	/**
	 * Pacote do Content Provider. Precisa ser único.
	 */
	public static final String AUTHORITY = Constants.AUTHORITY;

	private long id;
	private String nome;
	private String placa;
	private String tipo;

	private static final long serialVersionUID = 6601006766832473959L;
	public static final String KEY = "carro";

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(final String placa) {
		this.placa = placa;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(final String tipo) {
		this.tipo = tipo;
	}

	public Carro() {
	}

	public Carro(final String nome, final String placa, final String tipo) {
		super();
		this.nome = nome;
		this.placa = placa;
		this.tipo = tipo;
	}

	public Carro(final long id, final String nome, final String placa,
			final String tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.placa = placa;
		this.tipo = tipo;
	}

	// Ordenação default para inserir no order by
	public static final String DEFAULT_SORT_ORDER = Constants.DEFAULT_SORT_ORDER;

	public static final String NOME = "nome";
	public static final String PLACA = "placa";
	public static final String TIPO = "tipo";

	@Override
	public String toString() {
		return "Id: " + id + ", Nome: " + nome + ", Placa: " + placa
				+ ", Tipo: " + tipo;
	}
}
