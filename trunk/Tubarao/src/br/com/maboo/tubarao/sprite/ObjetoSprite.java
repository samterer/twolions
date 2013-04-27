package br.com.maboo.tubarao.sprite;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;

public class ObjetoSprite extends Sprite {

	private int pontuacao = 0;

	public ObjetoSprite(Bitmap bitmap) {
		setImage(bitmap);
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
		move(0, getPeso());
		new Timer().schedule(task, fps * 2);
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

}
