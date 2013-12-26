package br.com.maboo.node.map;

import android.app.ActionBar.LayoutParams;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GeoPointMaker implements OnTouchListener {

	private int windowWidth;
	private int windowHeight;

	public GeoPointMaker(int windowWidth, int windowHeight) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();

		boolean selec = false;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int x_cord = (int) event.getRawX();
			int y_cord = (int) event.getRawY();

			if (x_cord > windowWidth) {
				x_cord = windowWidth;
			}
			if (y_cord > windowHeight) {
				y_cord = windowHeight;
			}

			layoutParams.leftMargin = x_cord - 25;
			layoutParams.topMargin = y_cord - 75;

			v.setLayoutParams(layoutParams);
			break;
		default:
			break;
		}
		return true;
	}

}
