package br.com.twolions.service;

import java.util.ArrayList;
import java.util.List;

import br.com.twolions.object.Carro;

public class CarroService {

	public CarroService() {
		// TODO Auto-generated constructor stub
	}

	private static String[] carros_nome = { "Fiesta", "Monza", "Passat",
			"Ferrari", "Belina", "Opala", "YBR" };

	public static List<Carro> getCarros() {
		List<Carro> list = new ArrayList<Carro>();
		// cria lista para teste
		for (int i = 0; i < carros_nome.length; i++) {

			Carro c = new Carro();
			c.setNome(carros_nome[i]);

			list.add(c);
		}

		return list;
	}
}
