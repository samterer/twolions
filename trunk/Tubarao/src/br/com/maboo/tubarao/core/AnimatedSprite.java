package br.com.maboo.tubarao.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class AnimatedSprite {
	private Bitmap bitmap;
	private Drawable drawable;
	private int xPos;
	private int yPos;
	private Rect sRectangle;
	private int fps;
	private int numFrames;
	private int currentFrame;
	private long frameTimer;
	private int height;
	private int width;
	private boolean loop;
	public boolean dispose;
	public boolean isTouch = false;
	
	public AnimatedSprite() {
		sRectangle = new Rect(0, 0, 0, 0);
		frameTimer = 0;
		currentFrame = 0;
		xPos = 80;
		yPos = 200;
		dispose = false;
	}
	
	public void Initialize(Drawable drawable, int height, int width, int fps, int frameCount, boolean loop) {
	//	this.bitmap = bitmap;
		this.drawable = drawable;
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
	
	/**
	 * @param x
	 *            the x to set
	 * @param y
	 *            the y to set
	 */
	public void setPosition(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setXPos(int value) {
		xPos = value - (width/2);
	}
	
	public void setYPos(int value) {
		yPos = value - (height/2);
	}
	
	
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void Update(long gameTime) {
		if( gameTime > frameTimer + fps) {
			frameTimer = gameTime;
			currentFrame += 1;
			
			if( currentFrame >= numFrames ) {
				currentFrame = 0;
				
				if(!loop)
					dispose = true;			
				
			}
			
			sRectangle.left = currentFrame * width;
			sRectangle.right = sRectangle.left + width;
		}
	}
	
	public void draw(Canvas canvas) {
		Rect dest = new Rect(getXPos(), getYPos(), getXPos() + width,
										getYPos() + height);
	//	canvas.drawBitmap(bitmap, sRectangle, dest, null);
		drawable.setBounds(sRectangle);
		drawable.draw(canvas);
	}
	
	/**
	 * Retorna true caso o usuario tenha clicado sobre o obj do tipo
	 * Sprite(Drawable)
	 */
	public boolean isTouch(float touchX, float touchY) {
		
		if (drawable.copyBounds().contains((int) touchX, (int) touchY)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setTouch(boolean isTouch) {
		this.isTouch = isTouch;
	}

	public boolean isTouch() {
		return isTouch;
	}
	
}
