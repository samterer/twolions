package br.com.maboo.neext.util;

import br.com.maboo.neext.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class LinedTextView extends TextView {

	private Rect mRect;
	private Paint mPaint;
	private boolean check = false;

	public LinedTextView(Context context) {
		super(context);
	
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(Color.WHITE); // SET YOUR OWN COLOR HERE

	}

	protected void onDraw(Canvas canvas) {
		
		if(check) {
			//Rect r = mRect;
			//Paint paint = mPaint;

			//int baseline = getLineBounds(0, r);// line
			canvas.drawLine(mRect.exactCenterY(), 0, mRect.right,0, mPaint);	
			//canvas.drawColor(color.amarelo);
		} else {
			//canvas.drawColor(color.transparente);
		}

		super.onDraw(canvas);
	}

	public LinedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public LinedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}
	
	public void check() {
		if(!check) {
			this.check = true;
		}
	}
	
	public void unCheck() {
		if(check) {
			this.check = false;
		}
	}

}