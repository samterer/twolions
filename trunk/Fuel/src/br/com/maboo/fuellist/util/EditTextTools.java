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
	 * para a cor vermelha e devolver o boolean para o método que o chamou
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
		if (cont == 2) {

			result = false; // todos os campos foram preenchidos

		} else {

			result = true; // ainda existem campos não preenchidos

			// verifica se gera ou não o alerta
			if (alert.equals("alert")) {
				// ainda existem campos não preenchidos
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

	/**
	 * Convert numero para decimal
	 * 
	 * @author michel
	 * */
	public static String formatDecimal(String numAdd, int numValorProduto) {

		/*
		 * Pode ser adicionado até 100.000,00
		 */
		if (numValorProduto < 10000000 || numAdd.equals("")) {

			// se for vazio, então é para decrementar o número.
			if (numAdd.equals("")) {
				// apaga um número
				if (numValorProduto > 0) {
					numValorProduto = numValorProduto / 10;
				}
			} else if (numAdd.equals("00")) {

				numValorProduto = numValorProduto * 100; // adiciona dois zeros

			} else {

				int numInt = Integer.parseInt(numAdd); // número normal.

				numValorProduto = (numValorProduto * 10) + numInt;

			}

			String strNum = String.valueOf(numValorProduto);

			if (strNum.length() == 1) {
				strNum = "00" + strNum;
			} else if (strNum.length() == 2) {
				strNum = "0" + strNum;
			}

			StringBuilder strBuilder = new StringBuilder(strNum);
			// strBuilder.insert(strNum.length() - 2, ",");

			int length = strBuilder.length();

			// adiciona o ponto do milhar.
			if (length >= 7) {
				strBuilder.insert(length - 6, ".");
				// adiciona o ponto.
				if (length >= 10) {
					strBuilder.insert(length - 9, ".");
				}
			}

			return strBuilder.toString();
		}

		return "";
	}

}
