package br.com.maboo.tubarao.layer;

import java.util.ArrayList;

public class LayerBitmapManager extends ArrayList<LayerBitmap> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * retorno item do indice informado
	 * 
	 * @param indice
	 * */
	protected LayerBitmap getItem(int indice) {
		return get(indice);
	}

}
