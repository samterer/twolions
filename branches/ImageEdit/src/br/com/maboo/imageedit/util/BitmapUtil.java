package br.com.maboo.imageedit.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Display;
import br.com.maboo.imageedit.R;

public class BitmapUtil {
	
	private static Activity mActivity;
	private static Resources mResources;
	
	public static BitmapUtil getInstance(Activity activity) {
		if(mActivity == null) {			
			mActivity = activity;
			mResources = mActivity.getResources();
		}
		return new BitmapUtil();
	}

	private Bitmap drawTextToBitmap(int gResId, String gText) {
		float scale = mResources.getDisplayMetrics().density;
		Bitmap bitmap = BitmapFactory.decodeResource(mResources, gResId);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		// set default bitmap config if none
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.rgb(61, 61, 61));
		// text size in pixels
		paint.setTextSize((int) (14 * scale));
		// text shadow
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

		// draw text to the Canvas center
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bitmap.getWidth() - bounds.width()) / 2;
		int y = (bitmap.getHeight() + bounds.height()) / 2;

		canvas.drawText(gText, x, y, paint);

		return bitmap;
	}

	public Bitmap rotateBitmap(Bitmap bOriginal) {
		// find the width and height of the screen:
		Display d = mActivity.getWindowManager().getDefaultDisplay();
		int x = d.getWidth();
		int y = d.getHeight();

		// scale it to fit the screen, x and y swapped because my image is wider
		// than it is tall
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bOriginal, y, x, true);

		// create a matrix object
		Matrix matrix = new Matrix();
		matrix.postRotate(90); // anti-clockwise by 90 degrees

		// create a new bitmap from the original using the matrix to transform
		// the result
		Bitmap rotatedBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
						scaledBitmap.getHeight(), matrix, true);

		return rotatedBitmap;
	}

	public Bitmap overlay(Bitmap bOriginal) {
		float scale = mResources.getDisplayMetrics().density;

		Bitmap bmOverlay = Bitmap.createBitmap(bOriginal.getWidth(),
				bOriginal.getHeight(), bOriginal.getConfig());
		Bitmap bmp2 = BitmapFactory.decodeResource(mResources,
				R.drawable.h3_topic);

		Display d = mActivity.getWindowManager().getDefaultDisplay();
		int x = (d.getWidth() / 2 - bmOverlay.getWidth() / 2);
		int y = (d.getHeight() / 2 - bmOverlay.getHeight() / 2);

		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bOriginal, new Matrix(), null);
		canvas.drawBitmap(bmp2, x, y, null);
		return bmOverlay;
	}

}
