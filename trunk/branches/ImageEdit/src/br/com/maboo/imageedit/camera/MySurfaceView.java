package br.com.maboo.imageedit.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import br.com.maboo.imageedit.R;

@SuppressLint("DrawAllocation")
public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	public MySurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(icon, 10, 10, new Paint());
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = null;
		try {
			canvas = holder.lockCanvas(null);
			synchronized (holder) {
				onDraw(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
