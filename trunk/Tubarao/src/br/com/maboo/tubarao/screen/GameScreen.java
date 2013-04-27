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
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import br.com.maboo.tubarao.R;
import br.com.maboo.tubarao.core.GameActivity;
import br.com.maboo.tubarao.core.GameSurfaceView;
import br.com.maboo.tubarao.dados.DadosBitmap;
import br.com.maboo.tubarao.sprite.ObjetoSprite;
import br.com.maboo.tubarao.sprite.Sprite;
import br.com.maboo.tubarao.sprite.TubaraoSprite;

public class GameScreen extends GameSurfaceView {

	/**
	 * Nivel do jogo
	 */
	private int nivel = 4;

	/*
	 * UI constants
	 */
	private static final String KEY_TEMPO = "mPontos";
	private static final String KEY_PONTOS = "mTempo";

	private int GROUND = getDeviceScreenHeigth() - 200;
	private int VELOCIDADE_ENTRE_OBJS = 500;
	private int ESPERA_ENTRE_NIVEIS = 4000;

	private Vector<ObjetoSprite> objetos;

	private TubaraoSprite tub;

	private boolean isDerubaObjs = false;

	private Resources res;

	private DadosBitmap dados; // imagens das sprites

	public static TextView tPontos;
	public static TextView tTempo;

	private int mPontos;
	private long mTempo;

	long timer = 0l;

	private Paint textPaint;

	public GameScreen(Context context, AttributeSet attrs) {
		super(context, attrs, new Handler() {
			public void handleMessage(Message m) {
				mStatusText.setVisibility(m.getData().getInt("viz"));
				mStatusText.setText(m.getData().getString("text"));
			}
		});

		this.res = context.getResources();

		carga();

		montaTela();
	}

	public void carga() {
		Log.i("appLog", "## carga ##");

		dados = new DadosBitmap(res);

		initBackground();

		initText();

		initTubarao();

		initObjetos();

	}

	/**
	 * Cria o background
	 */
	private void initBackground() {
		Log.i("appLog", "## initBackground ##");

		// bg
		Sprite mFinalbitmap = new Sprite();
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		mFinalbitmap.Initialize(b, b.getHeight(), b.getWidth(), true);
		mFinalbitmap.setVisible(true);
		mFinalbitmap.setPosition(0, 0);

		getLayerManager().add(mFinalbitmap);
	}

	/**
	 * Inicia os campos para pontos\tempo entre outros
	 */
	private void initText() {
		Log.i("appLog", "## initText ##");

		textPaint = new Paint();
		textPaint.setARGB(255, 255, 255, 255);
		textPaint.setTextSize(26);

	}

	/**
	 * Inicia todas as imagens relacionadas ao tubarão
	 */
	private void initTubarao() {
		Log.i("appLog", "## initText ##");

		// tubarao
		tub = new TubaraoSprite(dados.getImgTubNormal()[0]);
		tub.setArrayBitmaps(dados.getImgTubNormal());
		tub.setVisible(true);
		tub.setRunAnimation(true);

		tub.setPosition(getDeviceScreenWidth() / 2, GROUND);

		getLayerManager().add(tub);
	}

	/**
	 * Inicia todas as imagens relacionadas aos objetos
	 */
	private void initObjetos() {
		Log.i("appLog", "## initObjetos ##");

		// carrega objetos
		objetos = new Vector<ObjetoSprite>();

		int[] array_id = DadosBitmap.TIPO_OBJETOS_POR_NIVEL[nivel];

		for (int j = 0; j < array_id.length; j++) {

			int id_da_vez = array_id[randomInterger(array_id.length)];

			ObjetoSprite obj = new ObjetoSprite(
					(dados.getImgObjetos(id_da_vez)));

			obj.setPeso(DadosBitmap.PESO_OBJETOS[id_da_vez]);
			obj.setPontuacao(DadosBitmap.PONTUACAO_OBJETOS[id_da_vez]);

			obj.setVisible(false);

			objetos.add(obj);
			getLayerManager().add(obj);
		}

		setCoordenadaRandom(objetos, getDeviceScreenWidth());
	}

	public void montaTela() {
		Log.i("appLog", "## montaTela ##");

		startDownObjs();

		doStart();

		timer = System.currentTimeMillis(); // inicia temporizador do jogo

		setFocusable(true);
	}

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// pontos
		tPontos = GameActivity.tPontos;
		tTempo = GameActivity.tTempo;

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

		// Log.i("appLog", "onTouchEvent: x/y > " + x + "/" + y);

		if (mMode != STATE_RUNNING) {
			setState(STATE_RUNNING);
		}

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

					obj.startDown(getDeviceScreenHeigth(), obj.getPeso());

					begin = System.currentTimeMillis();

					cont++;
				}

			}
		} else { // incrementa o nivel

			TimerTask task = new TimerTask() {
				public void run() {

					initObjetos();

					startDownObjs(); // inicia novamente a fase

					VELOCIDADE_ENTRE_OBJS -= 50;

				}
			};
			cont = 0;

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

	private void clearSpritesOutScreen() {
		// elimina os itens fora da tela
		if (objetos != null && objetos.size() > 0) {
			for (int i = 0; i < objetos.size(); i++) {
				ObjetoSprite objeto = (ObjetoSprite) objetos.elementAt(i);
				if (objeto.getY() > getDeviceScreenHeigth()) { // item perdido
					objeto.setVisible(false);
					getLayerManager().remove(objeto);
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

		if (mMode == STATE_RUNNING) {

			canvas.drawText("pontos: " + mPontos, 20, 40, textPaint);

			canvas.drawText("tempo: " + mTempo + "s", getWidth() - 200, 40,
					textPaint);
		}

	}

	public void loop() {
		// anima tub
		// Log.d("game","## lopping entrando... ##");
		if (tub != null) {
			if (tub.isVisible()) {
				if (tub.isRunAnimation()) {
					tub.animation();
				}
			}
		}

		// derruba itens
		if (isDerubaObjs) {
			moveItensDown(); // derruba itens
			// aumentaVelocidadeQueda();// aumenta velocidade do item abaixo do
			// meio da tela
			clearSpritesOutScreen(); // elima itens fora da tela
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
		// Log.d("game","## lopping saindo... ##");
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
