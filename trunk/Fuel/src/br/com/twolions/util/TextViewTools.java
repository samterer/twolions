package br.com.twolions.util;

import java.util.Vector;

import android.graphics.Typeface;
import android.widget.TextView;

public class TextViewTools {

	public static void insertFontInAllFields(Vector<TextView> v, Typeface tf) {

		for (int i = 0; i < v.size(); i++) {
			TextView et = (TextView) v.elementAt(i);

			et.setTypeface(tf);
		}

	}

	// convert date

}
