package br.com.maboo.tubarao.core;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import br.com.maboo.tubarao.layer.LayerManager;

public abstract class GameView extends FrameLayout {

	// Loop principal do jogo
	private GameThread loopThread;

	// Array com todas as imagens a serem exibidas no onDraw
	private LayerManager layerManager;

	// Dados do obj principal do dispositivo
	private Context context;

	// Dados da tela
	private DisplayMetrics dm;
	
	// Fps por segundo
	private int fps;

	public GameView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
		this.fps = 25;

		this.context = context;
	}

	public GameView(Context context, AttributeSet attrs, int fps) {
		super(context, attrs);

		this.context = context;
		
		this.fps = fps;

		startThread(fps);

		layerManager = new LayerManager();
	}

	/**
	 * Inicia argumentos da tela.
	 * */
	protected void init() {
		dm = new DisplayMetrics();
	}

	/**
	 * Inicia argumentos da tela.
	 * */
	protected abstract void loop();

	/**
	 * Pinta todos os elementos da tela.
	 * 
	 * @return
	 * */
	synchronized public void onDraw(Canvas canvas) {
		// super.draw(canvas);
		for (int i = getLayerManager().size(); --i >= 0;) {

			// Layer l = getLayerManager().get(i);

			getLayerManager().get(i).onDraw(canvas);

		}

		loop();
	}

	/**
	 * GameThread
	 * 
	 * @param gameView
	 *            view que será atualizada
	 * @param fps
	 *            frmaes por segundos.
	 * */
	private void startThread(long fps) {
		loopThread = new GameThread(this, fps);
	}

	/**
	 * start
	 * 
	 * starta a thread do jogo.
	 * */
	protected void startThread() {
		loopThread.start();
	}

	/**
	 * @param fps
	 *            the loopThread.fps to set
	 */
	public void setFps(long fps) {
		loopThread.setFps(fps);
	}

	public LayerManager getLayerManager() {
		return layerManager;
	}

	/**
	 * stopThread
	 * 
	 * finaliza o loop.
	 * */
	protected void stopThread() {
		loopThread.stop();
	}

	@Override
	// Chamado quando a tela é redimensioada, ou iniciada...
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);

		init();
	}

	// Retorna altura da tela
	public int getDeviceScreenHeigth() {

		// int height = getLayoutParams().height;

		// return height;

		dm = context.getResources().getDisplayMetrics();

		return dm.heightPixels;
	}

	// Retorna largura da tela
	public int getDeviceScreenWidth() {

		// int width = getLayoutParams().width;

		// return width;

		dm = context.getResources().getDisplayMetrics();

		return dm.widthPixels;
	}

	public int getFps() {
		return (int) 1000/fps;
	}

	
}
