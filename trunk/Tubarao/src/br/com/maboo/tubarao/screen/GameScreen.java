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
import br.com.maboo.tubarao.core.GameView;
import br.com.maboo.tubarao.dados.DadosBitmap;
import br.com.maboo.tubarao.sprite.ObjetoSprite;
import br.com.maboo.tubarao.sprite.Sprite;
import br.com.maboo.tubarao.sprite.TubaraoSprite;

public class GameScreen extends GameView {

	/**
	 * Nivel do jogo
	 */
	private int QTD_OBJ = 4;

	private int LISTA_LEVE = 0;
	private int LISTA_MEDIA = 1;
	private int LISTA_PESADA = 2;

	/*
	 * UI constants
	 */
	private static final String KEY_TEMPO = "mPontos";
	private static final String KEY_PONTOS = "mTempo";

	private int GROUND = getDeviceScreenHeigth() - 200;
	private int VELOCIDADE_ENTRE_OBJS = 1000;
	private int ESPERA_ENTRE_NIVEIS = 1000;

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

		// int[] array_id = DadosBitmap.TIPO_OBJETOS_POR_NIVEL[nivel];
		
		int id_da_vez = 0;

		for (int j = 0; j < (QTD_OBJ*2); j++) {

			id_da_vez = DadosBitmap.TIPO_OBJETOS_POR_NIVEL[LISTA_LEVE][randomInterger(DadosBitmap.TIPO_OBJETOS_POR_NIVEL[LISTA_LEVE].length)];

			ObjetoSprite obj = new ObjetoSprite(
					(dados.getImgObjetos(id_da_vez)));

			obj.setPeso(DadosBitmap.PESO_OBJETOS[id_da_vez]);
			obj.setPontuacao(DadosBitmap.PONTUACAO_OBJETOS[id_da_vez]);

			obj.setVisible(false);

			objetos.add(obj);
			getLayerManager().add(obj);
		}
		
		for (int j = 0; j < (QTD_OBJ/2); j++) {

			id_da_vez = DadosBitmap.TIPO_OBJETOS_POR_NIVEL[LISTA_MEDIA][randomInterger(DadosBitmap.TIPO_OBJETOS_POR_NIVEL[LISTA_MEDIA].length)];

			ObjetoSprite obj = new ObjetoSprite(
					(dados.getImgObjetos(id_da_vez)));

			obj.setPeso(DadosBitmap.PESO_OBJETOS[id_da_vez]);
			obj.setPontuacao(DadosBitmap.PONTUACAO_OBJETOS[id_da_vez]);

			obj.setVisible(false);

			objetos.add(obj);
			getLayerManager().add(obj);
		}

		setCoordenadaRandom(objetos);
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
	public void setCoordenadaRandom(Vector<ObjetoSprite> objetos) {

		// tira a largura do alimento para que algum deles não fique pra fora da
		// tela na posição x..
		int widthScreen = getDeviceScreenWidth() - objetos.get(0).getWidth();
		widthScreen -= 30;

		for (int i = 0; i < objetos.size(); i++) {

			Random r = new Random(System.currentTimeMillis() + (i * 12345));

			// transforma metade da largura, na posição y.
			int yAux = (50);

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

	private long begin = 0l;

	private void moveItensDown() {
		if (objetos.size() > 0) { // verifica se os objetos desse nivel já
									// cairam
			for (int i = 0; i < objetos.size(); i++) {
				ObjetoSprite obj = objetos.get(i);

				 // cria objeto na tela e lança ele em queda
				// (se ele já não estiver na tela)
				if (!obj.isVisible()) {

					long timeNow = System.currentTimeMillis();

					if (timeNow > (begin + VELOCIDADE_ENTRE_OBJS)) {
						obj.onDown(getDeviceScreenHeigth());

						obj.setVisible(true);

						begin = System.currentTimeMillis();

					}

				}
			}
		} else { // incrementa o nivel

			TimerTask task = new TimerTask() {
				public void run() {

					VELOCIDADE_ENTRE_OBJS -= 55;

					initObjetos();

					startDownObjs(); // inicia novamente a fase

				}
			};

			pauseDownObjs(); // para de cair objetos

			if (objetos != null) { // limpa objetos da tela
				if (objetos.size() > 0) {
					objetos.removeAllElements();
					objetos = new Vector<ObjetoSprite>();
				}
			}

			QTD_OBJ += QTD_OBJ; // incrementa a qnt de objetos do nivel

			new Timer().schedule(task, ESPERA_ENTRE_NIVEIS);
		}

	}

	/**
	 * Aumenta a velocidade da queda de acordo com o peso
	 */
	private void aumentaVelocidadeQueda() {
		for (int i = 0; i < objetos.size(); i++) {
			ObjetoSprite obj = objetos.get(i);
			if (obj.getY() > (getDeviceScreenHeigth() / 2)) {
				obj.setVelocidade(obj.getPeso() + (int) 0.3);
			}
		}
	}

	/**
	 * Limpa itens fora da tela
	 * 
	 * Lembrando, quando um objeto excede os limites da tela, ele conta como um
	 * objeto a menos na tela (descrementObjsInScreen())
	 */
	private void clearSpritesOutScreen() {
		// elimina os itens fora da tela
		if (objetos != null)
			if (objetos.size() > 0) {
				for (int i = 0; i < objetos.size(); i++) {
					ObjetoSprite obj = objetos.get(i);
					if (obj.getY() > getDeviceScreenHeigth()) { // item
																	// perdido
						obj.setVisible(false);
						getLayerManager().remove(obj);
						
						objetos.remove(obj);
					}
				}
			}
	}

	/**
	 * Verifica se o tubarao pegou o item
	 * 
	 * Lembrando, quando um objeto é pego, ele conta como um objeto a menos na
	 * tela (descrementObjsInScreen())
	 */
	private void gotItem() {
		for (int i = 0; i < objetos.size(); i++) {
			ObjetoSprite obj = objetos.get(i);
			if (tub.collidesWith(obj, true)) {
				mPontos++;

				obj.setVisible(false);
				getLayerManager().remove(obj);

				objetos.remove(obj);
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

		// derruba itens
		if (isDerubaObjs) {
			moveItensDown(); // derruba itens
		}

		if (isDerubaObjs) {
			// aumentaVelocidadeQueda();// aumenta velocidade dos itens caindo
		}

		// verifica se pegou item
		if (objetos != null && tub != null) {
			gotItem();

		}

		if (isDerubaObjs) { // elima itens fora da tela
			clearSpritesOutScreen();
		}

		// anima tub
		if (tub != null) {
			if (tub.isVisible()) {
				if (tub.isRunAnimation()) {
					tub.animation();
				}
			}
		}

		if (tTempo != null) { // incrementa o tempo
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
