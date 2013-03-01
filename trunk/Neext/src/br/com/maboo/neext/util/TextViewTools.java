package br.com.maboo.neext.util;

import java.util.Calendar;
import java.util.Vector;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ParseException;
import android.widget.TextView;

public class TextViewTools {

	public static void insertFontInAllFields(Vector<TextView> v, Typeface tf) {

		for (int i = 0; i < v.size(); i++) {
			TextView et = (TextView) v.elementAt(i);

			et.setTypeface(tf);
		}

	}

	// retorna o tempo entre a data atual e a data passada
	public static String getLastEdit(String hour, String date, Context context) {
		String result = "";

		final Calendar now = Calendar.getInstance(); // data atual

		try {
			
			//ano
			if(Integer.valueOf(date.substring(6, 9)).intValue() < now.getTime().getYear()) {
				// a mais de um anos
				return "há muito tempo atrás - "+now.getTime().getYear();
			} else {
					if(Integer.valueOf(date.substring(0, 1)).intValue() < now.getTime().getDay()+1) {
						// ha uns dias
						return "há alguns dias atrás";
					} else {
						// ha alguma tempo
						return "há alguma tempo atrás";
					}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}
}
