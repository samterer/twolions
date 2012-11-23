package br.com.maboo.neext.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class LinedEditText extends EditText {

	private Rect mRect;
	private Paint mPaint;

	public LinedEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(Color.BLUE); // SET YOUR OWN COLOR HERE

	}

	protected void onDraw(Canvas canvas) {
		// int count = getLineCount();

		int height = getHeight();
		int line_height = getLineHeight();

		int count = height / line_height;

		if (getLineCount() > count) {
			count = getLineCount();
		}

		Rect r = mRect;
		Paint paint = mPaint;

		int baseline = getLineBounds(0, r);// first line
		for (int i = 0; i < count; i++) {

			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
			baseline += getLineHeight();// next line
		}

		super.onDraw(canvas);
	}

	public LinedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public LinedEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

}