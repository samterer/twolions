package br.com.maboo.tubarao.layer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Layer {

	protected Drawable originalDrawable = null;

	protected int height = 0;
	protected int width = 0;

	protected int x = 0;
	protected int y = 0;

	protected boolean visible = true;

	/**
	 * Contrutor
	 * 
	 * @param originalDrawable
	 *            : imagem da animação
	 * 
	 *            Para sprites com apenas um frame.
	 * */
	public Layer(Drawable drawable) {
		setImage(drawable);
	}

	/**
	 * setImage, altera a imagem do sprite mantendo os valores de linhas e
	 * colunas.
	 * 
	 * @param originalDrawable
	 *            : nova imagem.
	 * */

	protected void setImage(Drawable d) {

		this.originalDrawable = d;

		this.height = originalDrawable.getIntrinsicHeight();
		this.width = originalDrawable.getIntrinsicWidth();

	}

	/**
	 * @return the currentDrawable
	 */
	public Drawable getImage() {
		return originalDrawable;
	}

	/**
	 * onDraw
	 * 
	 * @param canvas
	 * 
	 *            responsável por desenhar o sprite na tela.
	 * */
	public void onDraw(Canvas canvas) {
		if (visible) {
		
			originalDrawable.setBounds(x, y, x+width, y+height);
	
			//	Log.i("game", "draw coord: width * frame: " + width * frame);
			//	Log.i("game", "draw coord:  width * (frame + 1): " + width* (frame + 1));
			
			originalDrawable.draw(canvas);
		}
	}

	/**
	 * @param x
	 *            the x to set
	 * @param y
	 *            the y to set
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x
	 *            the x to set
	 * @param y
	 *            the y to set
	 */
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
