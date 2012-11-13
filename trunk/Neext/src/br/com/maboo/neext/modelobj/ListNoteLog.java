package br.com.maboo.neext.modelobj;

import java.io.Serializable;
import java.util.List;

public class ListNoteLog implements Serializable {
	private static final long serialVersionUID = -2251881666082662021L;
	public static final String KEY = "itemlog";
	public List<ItemNote> itens;

	public ListNoteLog(List<ItemNote> itens) {
		this.itens = itens;
	}
}
