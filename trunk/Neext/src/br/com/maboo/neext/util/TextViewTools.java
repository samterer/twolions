package br.com.maboo.neext.util;

import java.util.Calendar;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class TextViewTools {

	public static void insertFontInAllFields(Vector<TextView> v, Typeface tf) {

		for (int i = 0; i < v.size(); i++) {
			TextView et = (TextView) v.elementAt(i);

			et.setTypeface(tf);
		}

	}

	// retorna o tempo entre a data atual e a data passada
	// dd / mm / yyyy
	// [0][2] / [3][5] / [6][7][8][10]
	// hh : mm : 00
	// [0][2] : [3][5]
	public static String getLastEdit(String hour, String date, Context context) {

		int year = Integer.valueOf(date.substring(6, 10)).intValue();
		int mouth = Integer.valueOf(date.substring(3, 5)).intValue();
		int day = Integer.valueOf(date.substring(0, 2)).intValue();
		
		int hh =  Integer.valueOf(hour.substring(0, 2)).intValue();
		int mm = Integer.valueOf(hour.substring(3, 5)).intValue();

		String dateStart = mouth+"/"+day+"/"+year+" "+hh+":"+mm+":"+00;
		Log.i("appLog", "## dateStart " +dateStart+ " ##");
		
		final Calendar c = Calendar.getInstance();
	    int mYear = c.get(Calendar.YEAR);
	    int mMonth = c.get(Calendar.MONTH) + 1;
	    int mDay = c.get(Calendar.DAY_OF_MONTH);
	    
	    int mHh =  c.getTime().getHours();
		int mMm = c.getTime().getMinutes();
		
		String dateStop = mMonth+"/"+mDay+"/"+mYear+" "+ mHh +":"+ mMm +":"+00;
		Log.i("appLog", "## dateStop " +dateStop+ " ##");

		final DateTimeFormatter format = DateTimeFormat
				.forPattern("MM/dd/yyyy HH:mm:ss");

		DateTime dt1 = format.parseDateTime(dateStart);
		DateTime dt2 = format.parseDateTime(dateStop);

		final Period period = new Period(dt1, dt2);

		Log.i("appLog", period.getDays() + " days ##");
		Log.i("appLog", period.getHours() + " hours ##");
		Log.i("appLog", period.getMinutes() + " minutes ##");
		Log.i("appLog", period.getSeconds() + " seconds ##");
		
		// ha alguns dias
		if (period.getDays() > 1) {
			return "há "+period.getDays()+" dias";
		}
		// ha algumas horas
		else if (period.getHours() > 1 && period.getDays() <= 1) {
			return "há "+period.getHours()+" horas";
		}
		// ha alguns minutos
		else if (period.getMinutes() > 1 && period.getHours() < 2 && period.getDays() < 1) {
			return "há "+period.getMinutes()+" minutos";
		}
		// ha algum tempo 
		else {
			return "há algum tempo";
		}

	}
}
