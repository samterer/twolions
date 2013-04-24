package br.com.maboo.tubarao.core;

public class GameThread implements Runnable {

	private long fps = 75;

	private GameView view;

	private boolean running = true;

	private Thread thread;

	/**
	 * Construtor GameThread
	 * 
	 * @param gameView
	 *            view que será atualizada
	 * @param fps
	 *            frmaes por segundos.
	 * */
	public GameThread(GameView view, long fps) {
		this.view = view;
		if (fps > 0) {
			this.fps = fps;
		}
	}

	/**
	 * Laço do jogo
	 * */
	public void run() {

		long ticksPS = 1000 / fps;
		long startTime = 0;
		long sleepTime = 0;

		while (running) {

			startTime = System.currentTimeMillis();

			synchronized (view) {
				view.postInvalidate();
			}

			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);

			try {
				if (sleepTime > 0)
					Thread.sleep(sleepTime);
				else
					Thread.sleep(10);
			} catch (Exception e) {
			}
		}

	}

	/**
	 * Método que inicia laço do jogo
	 * */
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Finaliza loop do jogo
	 * */
	public void stop() {
		running = false;

		thread = null;
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		thread = null;
	}

	public void resume() {
		running = true;
		thread = new Thread();
		thread.start();
	}

	/**
	 * @return the fps
	 */
	public long getFps() {
		return fps;
	}

	/**
	 * @param fps
	 *            the fps to set
	 */
	public void setFps(long fps) {
		this.fps = fps;
	}
}
