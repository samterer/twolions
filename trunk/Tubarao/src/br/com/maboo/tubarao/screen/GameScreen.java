package br.com.maboo.tubarao.screen;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import br.com.maboo.tubarao.R;
import br.com.maboo.tubarao.core.GameActivity;
import br.com.maboo.tubarao.core.GameSurfaceView;
import br.com.maboo.tubarao.sprite.ObjetoBitmap;
import br.com.maboo.tubarao.sprite.TubaraoBitmap;

public class GameScreen extends GameSurfaceView {

	/*
	 * UI constants
	 */
	private static final String KEY_TEMPO = "mPontos";
	private static final String KEY_PONTOS = "mTempo";

	private int GROUND = getDeviceScreenHeigth() - 200;
	private int QTD_OBJ = 5;
	private int VELOCIDADE_ENTRE_OBJS = 1500;
	private int ESPERA_ENTRE_NIVEIS = 4000;

	private Vector<ObjetoBitmap> objetos;

	private TubaraoBitmap tub;

	private boolean isDerubaObjs = false;

	private Resources res;

	public static TextView tPontos;
	public static TextView tTempo;

	private int mPontos;
	private long mTempo;

	long timer = 0l;

	Bitmap[] bs_tubarao;
	Bitmap[] bs_objetos;

	public GameScreen(Context context, AttributeSet attrs) {
		super(context, attrs, new Handler() {
			public void handleMessage(Message m) {
				mStatusText.setVisibility(m.getData().getInt("viz"));
				mStatusText.setText(m.getData().getString("text"));
			}
		});

		this.res = context.getResources();

		carregaImagens();

		mountScreen();

		carregaObjetos();

		startDownObjs();

		timer = System.currentTimeMillis(); // inicia temporizador do jogo

		doStart();

		setFocusable(true);
	}

	private Paint textPaint;

	private void mountScreen() {

		// TODO
		textPaint = new Paint();
		textPaint.setARGB(255, 255, 255, 255);
		textPaint.setTextSize(26);

		// tubarao
		tub = new TubaraoBitmap(bs_tubarao[0]);
		tub.setArrayBitmaps(bs_tubarao);
		tub.setVisible(true);

		tub.setPosition(getDeviceScreenWidth() / 2, GROUND);

		getSprites().add(tub);

	}

	public void carregaImagens() {

		bs_tubarao = new Bitmap[] {
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_right)),
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_left)) };

		bs_objetos = new Bitmap[] {
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.obj_pneu46x42)),
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.obj_garrafa16x42)),
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.obj_lata42x38)),
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.obj_barril39x48)) };
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
		objetos = new Vector<ObjetoBitmap>();

		for (int i = 0; i < QTD_OBJ; i++) {
			ObjetoBitmap obj = new ObjetoBitmap();
			obj.setImage((bs_objetos[randomInterger(bs_objetos.length)]));
			obj.setVisible(false);

			objetos.add(obj);
			getSprites().add(obj);
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
	public static void setCoordenadaRandom(Vector<ObjetoBitmap> objetos,
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

			if (mMode != STATE_RUNNING) {
				setState(STATE_RUNNING);
			}

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

			ObjetoBitmap obj = (ObjetoBitmap) objetos.elementAt(cont);

			if (!obj.isVisible()) {

				long timeNow = System.currentTimeMillis();

				if (timeNow > (begin + VELOCIDADE_ENTRE_OBJS)) {
					obj.setVisible(true);

					obj.startDown(getDeviceScreenHeigth(), (int) getThread()
							.getFps());

					begin = System.currentTimeMillis();

					cont++;
				}

			}
		} else { // incrementa o nivel

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
			ObjetoBitmap obj = (ObjetoBitmap) objetos.elementAt(i);
			if (obj.getY() > (getDeviceScreenHeigth() / 2)) {
				obj.setVelocidade(obj.getVelocidade() + (int) 0.3);
			}
		}
	}

	private void eliminaItensCaidos() {
		// elimina os itens fora da tela
		if (objetos != null && objetos.size() > 0) {
			for (int i = 0; i < objetos.size(); i++) {
				ObjetoBitmap objeto = (ObjetoBitmap) objetos.elementAt(i);
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
			ObjetoBitmap objeto = (ObjetoBitmap) objetos.elementAt(i);
			if (tub.collidesWith(objeto, true)) {
				mPontos++;

				objeto.setVisible(false);
				objetos.remove(objeto);
			}
		}
	}

	public synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mMode == STATE_RUNNING) {

			canvas.drawText(getThread().getFps() + " fps",
					getDeviceScreenWidth() - 100,
					getDeviceScreenHeigth() - 100, textPaint);

			canvas.drawText(mTempo + "s tempo", getDeviceScreenWidth() - 120,
					getDeviceScreenHeigth() - 150, textPaint);

			canvas.drawText(mPontos + " pontos", getDeviceScreenWidth() - 140,
					getDeviceScreenHeigth() - 200, textPaint);
		}
	}

	protected synchronized void loop() {
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
			// tPontos.setText("pontos: " + mPontos);
		}

		if (tTempo != null) {

			mTempo = System.currentTimeMillis() - timer;
			mTempo = mTempo / 1000;

		}
	}

	public synchronized void restoreState(Bundle icicle) {
		super.restoreState(icicle);

		mPontos = icicle.getInt(KEY_PONTOS);
		mTempo = icicle.getInt(KEY_TEMPO);

	}

	public Bundle saveState(Bundle map) {
		super.saveState(map);

		map.putInt(KEY_PONTOS, Integer.valueOf(mPontos));
		map.putLong(KEY_TEMPO, Long.valueOf(mTempo));

		return map;
	}

}
