package br.com.maboo.tubarao.screen;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import br.com.maboo.tubarao.GameActivity;
import br.com.maboo.tubarao.R;
import br.com.maboo.tubarao.core.GameView;
import br.com.maboo.tubarao.sprite.ObjetoSprite;
import br.com.maboo.tubarao.sprite.TubaraoSprite;

public class GameScreen extends GameView {

	private int GROUND = getDeviceScreenHeigth() - 200;
	private int QTD_OBJ = 5;
	private int VELOCIDADE_ENTRE_OBJS = 1500;
	private int ESPERA_ENTRE_NIVEIS = 4000;

	private Vector<ObjetoSprite> objetos;

	private TubaraoSprite tub;

	private boolean isDerubaObjs = false;

	private Resources res;

	public static TextView tPontos;
	public static TextView tTempo;

	private int mPontos;

	long start = 0l;

	Drawable[] ds_tubarao;
	Drawable[] ds_objetos;

	public GameScreen(Context context, AttributeSet attrs) {
		super(context, attrs, 55);

		this.res = context.getResources();

		carregaImagens();

		mountScreen();

		carregaObjetos();

		startDownObjs();

		startThread();

		start = System.currentTimeMillis(); // inicia temporizador do jogo

		setFocusable(true);
	}

	private void mountScreen() {

		// tubarao
		tub = new TubaraoSprite(ds_tubarao);
		tub.setPosition(getDeviceScreenWidth() / 2, GROUND);
		getLayerManager().add(tub);

	}

	public void carregaImagens() {

		ds_tubarao = new Drawable[] {
				res.getDrawable(R.drawable.tub125x115_right),
				res.getDrawable(R.drawable.tub125x115_left) };

		ds_objetos = new Drawable[] {
				res.getDrawable(R.drawable.obj_pneu46x42),
				res.getDrawable(R.drawable.obj_garrafa16x42),
				res.getDrawable(R.drawable.obj_lata42x38),
				res.getDrawable(R.drawable.obj_barril39x48) };
	}

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// pontos
		tPontos = GameActivity.tPontos;
		tTempo = GameActivity.tTempo;

	}

	private void carregaObjetos() {

		// carrega objetos
		objetos = new Vector<ObjetoSprite>();

		for (int i = 0; i < QTD_OBJ; i++) {
			ObjetoSprite obj = new ObjetoSprite(ds_objetos[randomInterger(ds_objetos.length)]);
			obj.setVisible(false);

			objetos.add(obj);
			getLayerManager().add(obj);
		}

		setCoordenadaRandom(objetos, getDeviceScreenWidth());

	}

	private int randomInterger(int limit) {
		int random = 0 + Double.valueOf(Math.random() * (limit - 0)).intValue();
		return random;
	}

	/**
	 * seta as de posições x, y para iniciar os alimentos, gera y negativo para
	 * não aparecer na tela inicialmente.
	 */
	public static void setCoordenadaRandom(Vector<ObjetoSprite> objetos,
			int widthScreen) {

		// tira a largura do alimento para que algum deles não fique pra fora da
		// tela na posição x..
		widthScreen -= objetos.get(0).getWidth();

		for (int i = 0; i < objetos.size(); i++) {

			Random r = new Random(System.currentTimeMillis() + (i * 12345));

			// transforma metade da largura, na posição y.
			int yAux = (20);

			int x = r.nextInt(widthScreen);
			int y = r.nextInt(yAux);

			// passa y negativo para colocar a cima da tela.
			objetos.get(i).setPosition(x, -y);
		}
	}

	private void startDownObjs() {
		isDerubaObjs = true;
	}

	private void pauseDownObjs() {
		isDerubaObjs = false;
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
		if (newX > (tub.getX() + 10)) { // indo para a direita
			tub.setImage(0);

			if (newX > getDeviceScreenWidth() - tub.getWidth() - 20) {
				result = true;
			}
		} else if (newX < (tub.getX() - 10)) { // indo para a esquerda
			tub.setImage(1);

			if (newX < 20) {
				result = true;
			}
		}
		return result;
	}

	private int cont = 0;
	private long begin = 0l;

	private void moveItensDown() {
		if (cont < objetos.size()) {

			ObjetoSprite obj = (ObjetoSprite) objetos.elementAt(cont);

			if (!obj.isVisible()) {

				long timeNow = System.currentTimeMillis();

				if (timeNow > (begin + VELOCIDADE_ENTRE_OBJS)) {
					obj.setVisible(true);

					obj.startDown(getDeviceScreenHeigth(), getFps());

					begin = System.currentTimeMillis();

					cont++;
				}

			}
		} else {  //incrementa o nivel

			TimerTask task = new TimerTask() {
				public void run() {

					carregaObjetos(); // carrega novos itens

					startDownObjs(); // inicia novamente a fase

					VELOCIDADE_ENTRE_OBJS -= 100;

				}
			};
			cont = 0;
			QTD_OBJ += QTD_OBJ;
			pauseDownObjs();

			objetos.removeAllElements();

			new Timer().schedule(task, ESPERA_ENTRE_NIVEIS);
		}

	}

	private void aumentaVelocidadeQueda() {
		for (int i = 0; i < objetos.size(); i++) {
			ObjetoSprite obj = (ObjetoSprite) objetos.elementAt(i);
			if (obj.getY() > (getDeviceScreenHeigth() / 2)) {
				obj.setVelocidade(obj.getVelocidade() + (int) 0.3);
			}
		}
	}

	private void eliminaItensCaidos() {
		// elimina os itens fora da tela
		if (objetos != null && objetos.size() > 0) {
			for (int i = 0; i < objetos.size(); i++) {
				ObjetoSprite objeto = (ObjetoSprite) objetos.elementAt(i);
				if (objeto.getY() > getDeviceScreenHeigth()) { // item perdido
					objeto.setVisible(false);
				}
			}
		}
	}

	/**
	 * Verifica se pegou item
	 */
	private void gotItem() {
		for (int i = 0; i < objetos.size(); i++) {
			ObjetoSprite objeto = (ObjetoSprite) objetos.elementAt(i);
			if (tub.collidesWith(objeto, true)) {
				mPontos++;
				objeto.setVisible(false);
				objetos.remove(objeto);
			}
		}
	}

	public synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	protected void loop() {
		// anima tub
		if (tub != null) {
			if (tub.isVisible() && !tub.isStopAnimation()) {
				tub.animation();
			}
		}

		// derruba itens
		if (isDerubaObjs) {
			moveItensDown(); // derruba itens
			aumentaVelocidadeQueda();// aumenta velocidade do item abaixo do
										// meio da tela
			eliminaItensCaidos(); // elima itens fora da tela
		}

		// verifica se pegou item
		if (objetos != null && tub != null) {
			gotItem();

		}

		if (tPontos != null) {
			tPontos.setText("pontos: " + mPontos);
		}

		if (tTempo != null) {

			long mTempo = System.currentTimeMillis() - start;
			mTempo = mTempo / 1000;

			tTempo.setText("tempo: " + mTempo + "s");
		}
	}

}
