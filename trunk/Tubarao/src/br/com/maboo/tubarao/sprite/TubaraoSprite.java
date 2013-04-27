package br.com.maboo.tubarao.sprite;

import android.graphics.Bitmap;

public class TubaraoSprite extends Sprite {

	// animacao do balao flutuando
	private int speedUpDown = 5;
	private long begin = 0l;
	private long trocaDirecao = 0L;
	private boolean runAnimation = true;

	public TubaraoSprite(Bitmap bitmap) {

		setImage(bitmap);

		begin = System.currentTimeMillis();
	}

	/*
	 * Metodo responsável pelo movimento de sobe e desce do balao
	 */
	public void animation() {
		if (runAnimation) {
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

	public boolean isRunAnimation() {
		return runAnimation;
	}

	public void setRunAnimation(boolean runAnimation) {
		this.runAnimation = runAnimation;
	}


}
