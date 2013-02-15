package br.com.maboo.neext.util;

import java.util.Vector;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.Time;
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
	public static String getLastEdit(String hour, String date, Context context){
		String result = "";
		
		Time dt = new Time(); 
		// campo 2 = /
		// campo 5 = /
		dt.set(Integer.valueOf(date.substring(0, 1)).intValue(), Integer
				.valueOf(date.substring(3, 4)).intValue(),
				Integer.valueOf(date.substring(6, 9)).intValue());
		dt.normalize(true);
		
		dt.monthDay += 1;
	
		
		Toast.makeText(context, hour+" "+dt, Toast.LENGTH_SHORT).show();
		
		
		return result;
	}

}
