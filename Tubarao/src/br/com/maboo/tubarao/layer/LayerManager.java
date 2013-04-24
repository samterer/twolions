package br.com.maboo.tubarao.layer;

import java.util.ArrayList;

import br.com.maboo.tubarao.sprite.Sprite;

public class LayerManager extends ArrayList<Layer> {

	LayerManager layerManager;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Adiciona um item ao gerenciador.
	 * */
	protected void addItem(Sprite s) {
		layerManager.add(s);
	}

	/**
	 * retorno item do indice informado
	 * 
	 * @param indice
	 * */
	protected Layer getItem(int indice) {
		return layerManager.get(indice);
	}

	/**
	 * Retorna todos os itens da layer.
	 * */
	protected LayerManager getItens() {
		return layerManager;
	}

	/**
	 * Remove o item passado por paramentro.
	 * */
	protected void removeItem(Layer layer) {
		layerManager.remove(layer);
	}

	/**
	 * Remove o item pela posição da layer
	 * */
	protected void removeItem(int id) {
		removeItem(layerManager.get(id));
	}

}
