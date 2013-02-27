package br.com.maboo.neext.model;

import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.screens.ListScreen;
import br.com.maboo.neext.util.Constants;

public class ItemModel {

	private static String TAG = Constants.LOG_APP;

	/**
	 * Buscar o itemLog pelo id_item
	 * 
	 * @param id
	 * @return
	 */
	public static ItemNote buscarItemNote(final long id) {
		return ListScreen.dao.buscarItemNote(id);
	}

	/**
	 * Salvar o itemLog
	 * 
	 * @param itemLog
	 */
	public static long salvarItemNote(final ItemNote itemLog) {
		return ListScreen.dao.salvar(itemLog);
	}

	/**
	 * Excluir o itemLog
	 * 
	 * @param id
	 */
	public static void excluirItemNote(final long id) {
		ListScreen.dao.deletar(id);
	}

}
