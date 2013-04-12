package br.com.maboo.fuellist.rules;

import android.content.Context;
import android.util.Log;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.util.AndroidUtils;
import br.com.maboo.fuellist.util.Constants;

public class ItemRules {

	private static Double value_u;
	private static Double value_p;
	private static long odometer;
	private static Context context;

	private static String TAG = Constants.LOG_APP;

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
			Log.i(TAG, "rule 'value_p [" + item.getValue_p() + "]'");

			value_u = item.getValue_u();
			Log.i(TAG, "rule 'value_p [" + item.getValue_u() + "]'");

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
