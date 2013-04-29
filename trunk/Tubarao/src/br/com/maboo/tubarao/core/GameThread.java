package br.com.maboo.tubarao.core;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private int fps = 50;

	/** Used to figure out elapsed time between frames */
	private long mLastTime;

	private GameView view;

	private boolean mRun = false;

	/** Handle to the surface manager object we interact with */
	private SurfaceHolder mSurfaceHolder;

	public GameThread(GameView view) {

		this.view = view;

		mSurfaceHolder = view.getHolder();

	}

	public void setRunning(boolean run) {
		mRun = run;
	}

	private boolean sleep = false;
	private boolean pleaseWait = false;
	private long millis = 0L;

	public void run() {
		long ticksPS = 1000 / fps;
		fps = (int) ticksPS;
		long startTime = 0;
		long sleepTime = 0;

		while (mRun) {
			Canvas c = null;
			startTime = System.currentTimeMillis();

			try {
				c = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {
					if (view.mMode == GameView.STATE_RUNNING) {
						view.onDraw(c);

						view.loop();
					}

				}
			} finally {

				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}

			try {
				long timeTake = System.currentTimeMillis() - startTime;
				if (timeTake < this.fps) {
					synchronized (this) {
						wait(fps - timeTake);
					}
				}
				Thread.yield();
				synchronized (this) {
					while (pleaseWait)
						wait();
				}
				if (sleep) {
					sleep = false;
					synchronized (this) {
						sleep(millis);
					}
				}

				/*
				 * sleepTime = ticksPS - (System.currentTimeMillis() -
				 * startTime);
				 * 
				 * try { if (sleepTime > 0) { Thread.sleep(sleepTime); } else {
				 * Thread.sleep(10); }
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public long getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public long getmLastTime() {
		return mLastTime;
	}

	public void setmLastTime(long mLastTime) {
		this.mLastTime = mLastTime;
	}

}