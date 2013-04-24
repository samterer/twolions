package br.com.maboo.tubarao.sprite;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.drawable.Drawable;

public class ObjetoSprite extends Sprite {

	// animacao do balao flutuando
	private int speedUpDown = 5;
	private long begin = 0l;
	private long trocaDirecao = 0L;
	private boolean stopAnimation = false;

	public ObjetoSprite(Drawable drawable) {
		super(drawable);

		begin = System.currentTimeMillis();
	}

	public ObjetoSprite(Drawable drawable, int widthFrame, int heightFrame) {
		super(drawable, widthFrame, heightFrame);

		begin = System.currentTimeMillis();
	}

	public synchronized void startDown(final int limiteY, final int fps) {

		TimerTask task = new TimerTask() {
			public void run() {
				if (getY() > limiteY) {
					setVisible(false);
					cancel();
				} else {
					startDown(limiteY, fps);
				}
			}
		};
		move(0, velocidade);
		new Timer().schedule(task, fps * 2);
	}

}
