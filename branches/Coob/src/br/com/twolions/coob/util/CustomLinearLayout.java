package br.com.twolions.coob.util;

import android.content.Context;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout {

	private int color;

	public CustomLinearLayout(Context context) {
		super(context);

	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
