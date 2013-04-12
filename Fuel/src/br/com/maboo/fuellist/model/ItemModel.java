package br.com.maboo.fuellist.model;

import java.util.List;

import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.screens.ListItemScreen;
import br.com.maboo.fuellist.util.Constants;

public class ItemModel {

	private static String TAG = Constants.LOG_APP;

	/**
	 * Buscar o itemLog pelo id_item
	 * 
	 * @param id
	 * @return
	 */
	public static ItemLog buscarItemLog(final long id) {
		return ListItemScreen.dao.buscarItemLog(id);
	}

	/**
	 * Salvar o itemLog
	 * 
	 * @param itemLog
	 */
	public static long salvarItemLog(final ItemLog itemLog) {
		return ListItemScreen.dao.salvar(itemLog);
	}

	/**
	 * Excluir o itemLog
	 * 
	 * @param id
	 */
	public static void excluirItemLog(final long id) {
		ListItemScreen.dao.deletar(id);
	}

	/**
	 * Recupera o odometro mais recente desse veiculo (no caso, o maior)
	 */
	public static long buscarUltOdometroPorItem(final long id_car) {
		long result = 0L;

		// percorre a lista de itens desse veiculo do tipo fuel
		List<ItemLog> list = null;

		try {
			list = ListItemScreen.dao.listarItemLogsPorTipoECar(""
					+ Constants.FUEL, "" + id_car);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (list.size() == 0 || list == null) { // esse veiculo não possui itens
												// do tipo fuel
			return 0;
		}

		// RETORNA O MAIOR ODOMETER
		int menor, maior;
		menor = Integer.parseInt(String.valueOf(list.get(0).getOdometer()));
		maior = Integer.parseInt(String.valueOf(list.get(0).getOdometer()));
		for (int i = 0; i < list.size(); i++) {

			int valor = Integer.parseInt(String.valueOf(list.get(i)
					.getOdometer()));

			if (valor < menor) {
				menor = valor;
			}

			if (valor > maior) {
				maior = valor;
			}
		}

		result = maior;

		return result;
	}
}
