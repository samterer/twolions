package br.com.twolions.util;

import java.util.Vector;

import android.content.Context;
import android.widget.EditText;
import br.com.twolions.R;

public class EditTextTools {

	
	/**
	 * Verifica se algum dos campos do tipo editText gravados dentro do vector
	 * se encontra vazio, se o caso for verdadeiro, ele passa o hint deste
	 * campo para a cor vermelha e devolver o boolean para o método que o chamou
	 * @param v
	 * @param context
	 * @return boolean
	 */
	public static boolean isEmptyEdit(Vector<EditText> v, Context context) {
		boolean result = true;
		int cont = 0;

		for (int i = 0; i < v.size(); i++) {
			EditText et = (EditText) v.elementAt(i);

			if (et.getText().equals("") || et.length() < 1) {
				cont--;// campo vazio

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
			
			// Não existe conexão
						AndroidUtils.alertDialog(context,
								R.string.a_f_item, context.getString(R.string.t_f_item));
			
		}

		return result;
	}

}
