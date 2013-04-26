package br.com.maboo.tubarao.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class LayerBitmap {
	protected Bitmap originalBitmap;
	protected int x;
	protected int y;
	protected Rect sRectangle;
	protected int currentFrame;
	protected int height;
	protected int width;
	protected boolean visible;

	public LayerBitmap() {
		originalBitmap = null;
		sRectangle = new Rect(0, 0, 0, 0);
		currentFrame = 0;
		x = 0;
		y = 0;
		visible = false;
	}

	public void setImage(Bitmap bitmap) {
		Initialize(bitmap, bitmap.getHeight(), bitmap.getWidth(), true);
	}

	public void Initialize(Bitmap bitmap, int height, int width, boolean visible) {
		this.originalBitmap = bitmap;
		this.height = height;
		this.width = width;
		this.sRectangle.top = 0;
		this.sRectangle.bottom = height;
		this.sRectangle.left = 0;
		this.sRectangle.right = width;
		this.visible = visible;

		sRectangle.left = currentFrame * width;
		sRectangle.right = sRectangle.left + width;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void move(int toX, int yoY) {
		
		this.x += toX;
		this.y += yoY;

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int value) {
		this.x = value;
	}

	public void setY(int value) {
		this.y = value;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * @return the currentDrawable
	 */
	public Bitmap getImage() {
		return originalBitmap;
	}

	public void draw(Canvas canvas) {

		if (visible) {		
			Rect dest = new Rect(x, y, x + width, y + height);
			canvas.drawBitmap(originalBitmap, sRectangle, dest, new Paint());
		}
	}
}
