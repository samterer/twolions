package br.com.maboo.tubarao.layer;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Layer {

	protected Drawable currentDrawable;
	protected Vector<Drawable> drawables;

	protected int height = 0;
	protected int width = 0;

	protected int x = 0;
	protected int y = 0;

	protected int widthFrame = 0;

	protected int frame = 0;

	protected boolean visible = true;

	protected int indexImage = 0;

	/**
	 * Contrutor
	 * 
	 * @param currentDrawable
	 *            : imagem da animação
	 * 
	 *            Para sprites com apenas um frame.
	 * */
	public Layer(Drawable drawable) {
		setImage(drawable);
	}

	/**
	 * Construtor
	 * 
	 * @param currentDrawable
	 *            : imagem da animação
	 * 
	 *            Divida a imagem em frame e os guarda em um array
	 */
	public Layer(Drawable d, int widthFrame, int heightFrame) {

		drawables = new Vector<Drawable>();

		int qtdImg = 0;

		if (widthFrame > 0) {
			if (widthFrame % 2 == 0) {

				width = widthFrame;
				height = heightFrame;

				qtdImg = d.getIntrinsicWidth() / widthFrame;

				for (int i = 0; i < qtdImg; i++) {

					d.setBounds(widthFrame * i, y, widthFrame * (i + 1),
							heightFrame);

					Log.i("game", "coord: widthFrame * i: " + widthFrame * i);
					Log.i("game", "coord: widthFrame * (i + 1): " + widthFrame
							* (i + 1));

					drawables.add(d);

				}

				this.currentDrawable = getImage(0);
			}
		}
	}

	public Drawable getImage(int indexImage) {
		if (drawables != null && drawables.size() > 0) {
			Drawable d = (Drawable) drawables.elementAt(indexImage);
			return d;
		}
		return null;
	}

	/**
	 * setImage, altera a imagem do sprite mantendo os valores de linhas e
	 * colunas.
	 * 
	 * @param currentDrawable
	 *            : nova imagem.
	 * */

	protected void setImage(Drawable d) {

		this.currentDrawable = d;

		this.height = currentDrawable.getIntrinsicHeight();
		this.width = currentDrawable.getIntrinsicWidth();

	}

	protected void setImage(Drawable d, int widthFrame, int heightFrame) {

		this.currentDrawable = d;

		this.height = heightFrame;
		this.width = widthFrame;

	}

	/**
	 * @return the currentDrawable
	 */
	public Drawable getImage() {
		return currentDrawable;
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
			// caso esteja visivel, pinta na tela.
			currentDrawable.setBounds(x + width * frame, y, x + width
					* (frame + 1), y + height);
			Log.i("game", "draw coord: width * frame: " + width * frame);
			Log.i("game", "draw coord:  width * (frame + 1): " + width
					* (frame + 1));
			currentDrawable.draw(canvas);
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
