package br.com.maboo.tubarao.layer;

import java.util.ArrayList;

public class LayerManager extends ArrayList<Layer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * retorno item do indice informado
	 * 
	 * @param indice
	 * */
	protected Layer getItem(int indice) {
		return get(indice);
	}

}
