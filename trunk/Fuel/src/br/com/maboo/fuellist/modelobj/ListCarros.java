package br.com.maboo.fuellist.modelobj;

import java.io.Serializable;
import java.util.List;

public class ListCarros implements Serializable {
	private static final long serialVersionUID = -2251881666082662021L;
	public static final String KEY = "carro";
	public List<Carro> carros;
	public ListCarros(List<Carro> carros) {
		this.carros = carros;
	}
}
