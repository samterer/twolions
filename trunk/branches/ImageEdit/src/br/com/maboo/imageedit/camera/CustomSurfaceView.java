package br.com.maboo.imageedit.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * @author Tatyabhau Chavan
 * 
 */
public class CustomSurfaceView extends SurfaceView {

	// this constructor used when requested as an XML resource
	public CustomSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomSurfaceView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("MySurfaceView", "onDraw");
		super.onDraw(canvas);
	}

}
