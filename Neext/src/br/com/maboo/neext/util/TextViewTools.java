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

		Calendar xmas = Calendar.getInstance(); // data da ultima alteração
		xmas.set(Integer.valueOf(date.substring(6, 9)).intValue(), Integer
				.valueOf(date.substring(3, 4)).intValue(),
				Integer.valueOf(date.substring(0, 1)).intValue());

		final Calendar now = Calendar.getInstance(); // data atual

		try {
			long date1 = xmas.getTimeInMillis();
			long date2 = now.getTimeInMillis();
			long differenceMilliSeconds = date2 - date1;

			// return valor
			long dias = differenceMilliSeconds / 1000 / 60 / 60 / 24;
			long horas = differenceMilliSeconds / 1000 / 60 / 60;
			long minutos = differenceMilliSeconds / 1000 / 60;
			long segundos = differenceMilliSeconds / 1000;

			if (dias > 0) {
				return "" + dias + " dias atrás";
			} else if (horas > 0) {
				return "" + horas + " horas atrás";
			} else if (minutos > 0) {
				return "" + minutos + " minutos atrás";
			} else {
				return "" + segundos + " segundos atrás";
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		//
		// Toast.makeText(
		// context,
		// "save date: '" + hour + " " + date + "' | current date: '"
		// + dateFormat.format(cal.getTime()) + "' ",
		// Toast.LENGTH_LONG).show();

		return result;
	}
}
