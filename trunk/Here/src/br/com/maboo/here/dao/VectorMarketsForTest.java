package br.com.maboo.here.dao;

import br.com.maboo.here.marker.Market;
import br.com.maboo.here.marker.VectorMarket;
import br.com.maboo.here.util.Coordinate;


/**
 * Classe de teste para imitar a base de dados
 * 
 * Quando instanciada, automaticamente cria uma lista de markets.
 * 
 * @author jeff
 * 
 */
public class VectorMarketsForTest {

	private String[] name = { "CONTRUINDIO", "SHOPPING GALERIA",
			"SUPERMERCADO BAHIA", "DEGA'S VIDEO" };

	private int[] id = { 0, 1, 2, 3 };

	private double[] latitude = { -22.975525, -22.863146, -22.974782,
			-22.973977 };
	private double[] longitude = { -47.088325, -47.023072, -47.086887,
			-47.086930 };

	// vector (padrao que sera incluido na base)
	private VectorMarket vListaMarket;

	public VectorMarketsForTest() {
		init();
	}

	public void init() {

		vListaMarket = new VectorMarket();

		criaLista();

	}

	public void criaLista() {

		for (int i = 0; i < name.length; i++) {

			Market m = new Market();

			m.setId(id[i]);

			m.setNome(name[i]);

			// location
			Coordinate coordinate = new Coordinate(latitude[i], longitude[i]);

			m.setLatitude(coordinate.getLatitudeE6());

			m.setLongitude(coordinate.getLongitudeE6());

			vListaMarket.add(m);
		}

	}

	/****************************************
	 * GET\SET
	 ****************************************/

	public VectorMarket getvListaMarket() {
		return vListaMarket;
	}

}
