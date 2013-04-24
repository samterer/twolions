package br.com.maboo.tubarao.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import br.com.maboo.tubarao.R;
import br.com.maboo.tubarao.core.GameView;
import br.com.maboo.tubarao.sprite.Sprite;
import br.com.maboo.tubarao.sprite.TubaraoSprite;

public class TubaraoScreen extends GameView {

	private int GROUND = getDeviceScreenHeigth() - 200;
	private int QTD_OBJ = 200;

	private Vector<Sprite> objetos;

	private TubaraoSprite tub;

	private boolean isDerubaObjs = false;

	public TubaraoScreen(Context context, AttributeSet attrs) {
		super(context, attrs);

		mountScreen(context);

		organizeLayout(context);

		// carrega objetos
		objetos = new Vector<Sprite>();

		carregaObjetos(context);

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

	private void carregaObjetos(Context context) {
		for (int i = 0; i < QTD_OBJ; i++) {
			Sprite objeto = new TubaraoSprite(context.getResources()
					.getDrawable(R.drawable.obj_pneu46x42));

			objeto.setPosition(getRandomX(), -60);
			objetos.add(objeto);

			getLayerManager().add(objeto);
		}

		iniciaGame();
	}

	private void iniciaGame() {
		isDerubaObjs = true;
	}

	private int getRandomX() {
		ArrayList<Integer> myList = new ArrayList<Integer>();
		for (int i = 100; i <= (getDeviceScreenWidth() - 100); i++) {
			myList.add(i);
		}
		Collections.shuffle(myList);

		return myList.get(23);
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

	private void moveItensDown() {
		Sprite objeto = (Sprite) objetos.elementAt(objetos.size() - 1);
		objeto.move(objeto.getX(), 3);
	}

	private void eliminaItensCaidos() {
		// elimina os itens fora da tela
		for (int i = 0; i < objetos.size(); i++) {
			Sprite objeto = (Sprite) objetos.elementAt(i);
			if (objeto.getY() > GROUND + 200) {
				objeto.setVisible(false);
				objetos.remove(objeto);
			}
		}

	}

	protected void loop() {
		// anima balao (flutua)
		if (tub != null) {
			if (tub.isVisible() && !tub.isStopAnimation()) {
				tub.animation();
			}
		}

		// derruba itens
		if (isDerubaObjs) {
			if (objetos != null) {
				moveItensDown(); // derruba itens

				eliminaItensCaidos(); // elima itens fora da tela
			}
		}

	}
}
