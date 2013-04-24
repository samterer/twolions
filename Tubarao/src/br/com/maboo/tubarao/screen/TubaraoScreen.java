package br.com.maboo.tubarao.screen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import br.com.maboo.tubarao.R;
import br.com.maboo.tubarao.core.GameView;
import br.com.maboo.tubarao.sprite.TubaraoSprite;

public class TubaraoScreen extends GameView {

	int GROUND = getDeviceScreenHeigth() - 300;

	TubaraoSprite tub;

	public TubaraoScreen(Context context, AttributeSet attrs) {
		super(context, attrs);

		mountScreen(context);

		organizeLayout(context);

		startThread();

		setFocusable(true);
	}

	private void mountScreen(Context context) {
		// tubarao
		tub = new TubaraoSprite(context.getResources().getDrawable(
				R.drawable.tub125x115));

		tub.setPosition(getDeviceScreenWidth() / 2, GROUND);

		getLayerManager().add(tub);

	}

	private void organizeLayout(Context context) {

	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		// Log.i("game", "onTouchEvent: x/y > " + x + "/" + y);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// inicia o movimento (imagem pressionada)
			tub.setTouch(tub.isTouch(x, y));

			break;

		case MotionEvent.ACTION_MOVE:
			// arrasta o sprite
			if (tub.isTouch() && !isBlockMove((int) x - (tub.getWidth() / 2))) {
				tub.setPosition((int) x - (tub.getWidth() / 2), tub.getY());
			}

			break;
		case MotionEvent.ACTION_UP:
			// finalizou o movimento
			tub.setTouch(false);

			break;
		}

		return true;
	}

	/**
	 * Limite da tela verifica se o tub não vai sair da tela
	 */
	private boolean isBlockMove(int newX) {
		boolean result = false;
		if (newX > tub.getX()) { // indo para a direita
			tub.setFrame(1);

			if (newX > getDeviceScreenWidth() - tub.getWidth()) {
				result = true;
			}
		} else { // indo para a esquerda
			tub.setFrame(0);

			if (newX < 20) {
				result = true;
			}
		}
		return result;
	}

	protected void loop() {

	}
}
