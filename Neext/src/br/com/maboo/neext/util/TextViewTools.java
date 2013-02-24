package br.com.maboo.neext.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ParseException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		try {
			Date date1 = sdf.parse(date);
			Date date2 = sdf.parse(dateFormat.format(cal.getTime()));
			long differenceMilliSeconds = date2.getTime() - date1.getTime();
			Log.i("appLog", "diferenca em milisegundos: "
					+ differenceMilliSeconds);
			Log.i("appLog", "diferenca em segundos: "
					+ (differenceMilliSeconds / 1000));
			Log.i("appLog", "diferenca em minutos: "
					+ (differenceMilliSeconds / 1000 / 60));
			Log.i("appLog", "diferenca em horas: "
					+ (differenceMilliSeconds / 1000 / 60 / 60));
			Log.i("appLog", "diferenca em dias: "
					+ (differenceMilliSeconds / 1000 / 60 / 60 / 24));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		Toast.makeText(
				context,
				"save date: '" + hour + " " + date + "' | current date: '"
						+ dateFormat.format(cal.getTime()) + "' ",
				Toast.LENGTH_LONG).show();

		return result;
	}
}
