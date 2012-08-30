package br.com.twolions.object;

import java.io.Serializable;

public class Carro implements Serializable {
	private static final long serialVersionUID = 6601006766832473959L;
	public static final String KEY = "carro";
	public String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
