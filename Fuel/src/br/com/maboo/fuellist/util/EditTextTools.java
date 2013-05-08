package br.com.maboo.fuellist.util;

import java.util.Vector;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.EditText;
import br.com.maboo.fuellist.R;

public class EditTextTools {

	/**
	 * Verifica se algum dos campos do tipo editText gravados dentro do vector
	 * se encontra vazio, se o caso for verdadeiro, ele passa o hint deste campo
	 * para a cor vermelha e devolver o boolean para o m�todo que o chamou
	 * 
	 * @param v
	 * @param context
	 * @param alert
	 *            (alert or noalert)
	 * @return boolean
	 */
	public static boolean isEmptyEdit(Vector<EditText> v, Context context,
			String alert) {
		boolean result = true;
		int cont = 0;

		for (int i = 0; i < v.size(); i++) {
			EditText et = (EditText) v.elementAt(i);

			if (et.getText().equals("") || et.length() < 1) {
				cont--;// campo vazio

				Log.i("appLog", "Field [" + et.getTag() + "] empty.");

				et.setHintTextColor(context.getResources().getColor(
						R.color.vermelho));

			} else {

				cont++;

			}
		}
		if (cont == v.size()) {

			result = false; // todos os campos foram preenchidos

		} else {

			result = true; // ainda existem campos n�o preenchidos

			// verifica se gera ou n�o o alerta
			if (alert.equals("alert")) {
				// ainda existem campos n�o preenchidos
				AndroidUtils.alertDialog(context, R.string.a_f_item,
						context.getString(R.string.t_f_item));
			}

		}
		return result;
	}

	public static void insertFontInAllFields(Vector<EditText> v, Typeface tf) {

		for (int i = 0; i < v.size(); i++) {
			EditText et = (EditText) v.elementAt(i);

			et.setTypeface(tf);
		}

	}

}
