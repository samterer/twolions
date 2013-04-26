package br.com.maboo.tubarao.sprite;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;

public class ObjetoBitmap extends SpriteBitmap {

	public ObjetoBitmap(Bitmap bitmap) {

		setImage(bitmap);

	}

	public ObjetoBitmap() {

		if (peso == 0) {
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
