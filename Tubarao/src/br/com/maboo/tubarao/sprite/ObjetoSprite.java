package br.com.maboo.tubarao.sprite;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.drawable.Drawable;

public class ObjetoSprite extends Sprite {

	public ObjetoSprite(Drawable drawable) {
		super(drawable);
		
		if(peso == 0) {
			peso = 4;
		}
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
		move(0, peso);
		new Timer().schedule(task, fps * 2);
	}

}
