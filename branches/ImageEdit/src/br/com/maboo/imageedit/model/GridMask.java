package br.com.maboo.imageedit.model;

import java.util.List;

public class GridMask {

	public static final String KEY = "gridMask";
	public List<Mask> itens;

	public GridMask(List<Mask> itens) {
		this.itens = itens;
	}
}