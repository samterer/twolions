package br.com.maboo.fuellist.modelobj;

import java.io.Serializable;
import java.util.List;

public class ListItemLog implements Serializable {
	private static final long serialVersionUID = -2251881666082662021L;
	public static final String KEY = "itemlog";
	public List<ItemLog> itens;
	public ListItemLog(List<ItemLog> itens) {
		this.itens = itens;
	}
}
