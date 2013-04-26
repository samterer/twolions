package br.com.maboo.tubarao.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class LayerBitmap {
	protected Bitmap originalBitmap;
	protected int x;
	protected int y;
	protected Rect sRectangle;
	protected int fps;
	protected int numFrames;
	protected int currentFrame;
	protected long frameTimer;
	protected int height;
	protected int width;
	protected boolean loop;
	protected boolean dispose;
	protected boolean visible;

	public LayerBitmap() {
		originalBitmap = null;
		sRectangle = new Rect(0, 0, 0, 0);
		frameTimer = 0;
		currentFrame = 0;
		x = 0;
		y = 0;
		dispose = false;
		visible = false;
	}

	public void setImage(Bitmap bitmap) {
		Initialize(bitmap, bitmap.getHeight(), bitmap.getWidth(), 25, 0, false);

	}

	public void Initialize(Bitmap bitmap, int height, int width, int fps,
			int frameCount, boolean loop) {
		this.originalBitmap = bitmap;
		this.height = height;
		this.width = width;
		this.sRectangle.top = 0;
		this.sRectangle.bottom = height;
		this.sRectangle.left = 0;
		this.sRectangle.right = width;
		this.fps = 1000 / fps;
		this.numFrames = frameCount;
		this.loop = loop;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int value) {
		x = value - (width / 2);
	}

	public void setY(int value) {
		y = value - (height / 2);
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

	public void Update(long gameTime) {
		if (gameTime > frameTimer + fps) {
			frameTimer = gameTime;
			currentFrame += 1;

			if (currentFrame >= numFrames) {
				currentFrame = 0;

				if (!loop)
					dispose = true;

			}

			sRectangle.left = currentFrame * width;
			sRectangle.right = sRectangle.left + width;
		}
	}

	public void draw(Canvas canvas) {
		Rect dest = new Rect(getX(), getY(), getX() + width, getY() + height);
		canvas.drawBitmap(originalBitmap, sRectangle, dest, null);
	}
}
