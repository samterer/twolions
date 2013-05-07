package br.com.maboo.fuellist.rules;

import android.content.Context;
import android.util.Log;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.util.AndroidUtils;
import br.com.maboo.fuellist.util.Constants;

public class Rules {

	private static Context context;

	private static String TAG = Constants.LOG_APP;

	/**
	 * Gerencia todas as regras de combustivel
	 * 
	 * @param cod
	 * @return result
	 * 
	 *         Se o retorno for true o usuario pode salvar o item
	 * 
	 */
	public static boolean ruleFuel(ItemLog item, Context context) {

		Rules.context = context;

		boolean result = false;

		if (item != null) {

			Log.i(TAG, "## rule 'value_p [" + item.getValue_p() + "]' ##");

			Log.i(TAG, "## rule 'value_p [" + item.getValue_u() + "]' ##");

		} else {
			Log.i(Constants.LOG_APP, "o valor de item é nulo");
		}

		// chama primeira regra
		result = ruleOne(item.getValue_p(), item.getValue_u());

		return result;
	}

	/**
	 * 1 - o value_p deve ser no minimo o dobro de value_u
	 * 
	 * @return
	 */
	private static boolean ruleOne(Double value_p, Double value_u) {

		Double limite = value_u * 2;
		if (value_p >= limite) {
			return true;
		}

		// ainda existem campos não preenchidos
		AndroidUtils.alertDialog(context, R.string.t_r_1,
				context.getString(R.string.t_r_t_1));

		return false;
	}

}
