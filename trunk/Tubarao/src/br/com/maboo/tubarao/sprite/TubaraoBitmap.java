package br.com.maboo.tubarao.sprite;

import android.graphics.Bitmap;

public class TubaraoBitmap extends SpriteBitmap {

	// animacao do balao flutuando
	private int speedUpDown = 5;
	private long begin = 0l;
	private long trocaDirecao = 0L;
	private boolean stopAnimation = false;

	public TubaraoBitmap(Bitmap bitmap) {

		setImage(bitmap);

		begin = System.currentTimeMillis();
	}

	/*
	 * Metodo respons�vel pelo movimento de sobe e desce do balao
	 */
	public void animation() {
		if (!stopAnimation) {
			long timeNow = System.currentTimeMillis();
			if (timeNow > (begin + 50)) {
				move(0, speedUpDown);
				begin = System.currentTimeMillis();
			}
			if ((timeNow > (trocaDirecao + 550))) {
				speedUpDown *= -1;
				trocaDirecao = System.currentTimeMillis();
				begin = 0;
				if (speedUpDown < -1.5) {
					speedUpDown *= 0.3;
				}
				if (speedUpDown > 1.5) {
					speedUpDown *= -0.3;
				}
			}
		}
	}

	public boolean isStopAnimation() {
		return stopAnimation;
	}

	public void setStopAnimation(boolean stopAnimation) {
		this.stopAnimation = stopAnimation;
	}
}