package br.com.twolions.rules;

import android.content.Context;
import android.util.Log;
import br.com.twolions.R;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.util.AndroidUtils;
import br.com.twolions.util.Constants;

public class ItemRules {

	private static Double value_u;
	private static Double value_p;
	private static long odometer;
	private static Context context;

	public ItemRules(ItemLog item) {

	}

	/**
	 * Gerencia todas as regras
	 * 
	 * @param cod
	 * @return result
	 * 
	 *         Se o retorno for true o usuario pode salvar o item
	 * 
	 */
	public static boolean ruleManager(ItemLog item, Context context) {

		ItemRules.context = context;

		boolean result = false;

		if (item != null) {

			value_p = item.getValue_p();
			value_u = item.getValue_u();

			odometer = item.getOdometer();

		} else {
			Log.i(Constants.LOG_APP, "o valor de item é nulo");
		}

		// chama primeira regra
		result = ruleOne();

		return result;
	}

	/**
	 * 1 - o value_p deve ser no minimo o dobro de value_u
	 * 
	 * @return
	 */
	private static boolean ruleOne() {

		if (value_p >= (value_u * 2)) {
			return true;
		}

		// ainda existem campos não preenchidos
		AndroidUtils.alertDialog(context, R.string.t_r_1,
				context.getString(R.string.t_r_t_1));

		return false;
	}

}
