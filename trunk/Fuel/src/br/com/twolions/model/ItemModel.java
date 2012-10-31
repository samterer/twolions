package br.com.twolions.model;

import java.util.List;

import br.com.twolions.modelobj.ItemLog;
import br.com.twolions.screens.ListItemScreen;
import br.com.twolions.util.Constants;

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
	public static void salvarItemLog(final ItemLog itemLog) {
		ListItemScreen.dao.salvar(itemLog);
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
	 * Recupera o odometro mais recente desse veiculo (no caso, com a data mais
	 * proxima da atual)
	 */
	public static long buscarUltOdometroPorItem(final long id) {
		long result = 0L;

		// percorre a lista de itens desse veiculo do tipo fuel
		List<ItemLog> list = null;

		try {
			list = ListItemScreen.dao.listarItemLogsPorTipo(String
					.valueOf(Constants.FUEL));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// TODO
		// RETORNA O MAIOR ODOMETER

		// // recupera o que possui a ultima data
		// Vector<String> vDatas = new Vector<String>();
		// for (int i = 0; i < list.size(); i++) {
		// // Log.i(TAG, "item [" + list.get(i).getDate() +
		// // "] na lista por type");
		//
		// // formata date
		// String dateFromBase = list.get(i).getDate();
		//
		// StringBuffer sb = new StringBuffer();
		// for (int j = 0; j < dateFromBase.length(); j++) {
		//
		// if (dateFromBase.charAt(j) == '-') { // insere valor da
		// // data
		//
		// // Log.i(TAG, "date [" + sb.toString() + "]");
		//
		// vDatas.add(sb.toString());
		//
		// sb = new StringBuffer();
		//
		// break;
		//
		// }
		//
		// Log.i(TAG, "insert [" + dateFromBase.charAt(j) + "]");
		//
		// sb.append(dateFromBase.charAt(j));
		//
		// }
		// }
		//
		// // cria o array de inteiros
		// int[] aDates = new int[vDatas.size()];
		// int n = 0;
		//
		// for (int j = 0; j < vDatas.size(); j++) {
		// String s = (String) vDatas.elementAt(j);
		//
		// Log.i(TAG, "s [" + s + "] na lista por type");
		//
		// for (int i = 0; i < s.length(); i++) {
		//
		// if (i > 0) {
		//
		// if (s.charAt(i - 1) != '/') {
		//
		// if (i - 1 == 3) {
		// n += Integer.parseInt(String.valueOf(s
		// .charAt(i - 1))) + 10;
		// } else if (i - 1 == 6) {
		// n += Integer.parseInt(String.valueOf(s
		// .charAt(i - 1))) + 100;
		// } else {
		// n += Integer.parseInt(String.valueOf(s
		// .charAt(i - 1)));
		// }
		//
		// Log.i(TAG, "n [" + n + "] na lista por type");
		//
		// }
		//
		// }
		//
		// if (i >= s.length() - 1) {
		//
		// Log.i(TAG, "aDates[j] = n [" + n + "] na lista por type");
		//
		// aDates[j] = n;
		//
		// n = 0;
		//
		// break;
		// }
		//
		// }
		//
		// }
		//
		// for (int i : aDates) {
		// Log.i(TAG, "item [" + i + "] na lista por type");
		// }
		//
		// int menor, maior;
		// menor = aDates[0];
		// maior = aDates[0];
		// for (int valor : aDates) {
		// if (valor < menor)
		// menor = valor;
		// if (valor > maior)
		// maior = valor;
		// }
		//
		// // recupera o odometer
		// for (int i = 0; i < aDates.length; i++) {
		//
		// }

		// att o valor do odometro ao result

		return result;
	}
}
