package br.com.maboo.tubarao.core;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameSurfaceThread extends Thread {

	private int fps = 60;

	/** Used to figure out elapsed time between frames */
	private long mLastTime;

	private GameSurfaceView view;

	private boolean mRun = false;

	/** Handle to the surface manager object we interact with */
	private SurfaceHolder mSurfaceHolder;

	public GameSurfaceThread(GameSurfaceView view) {

		this.view = view;

		mSurfaceHolder = view.getHolder();

	}

	public void setRunning(boolean run) {
		mRun = run;
	}

	public void run() {
		long ticksPS = 1000 / fps;
		long startTime;
		long sleepTime;

		while (mRun) {
			Canvas c = null;
			startTime = System.currentTimeMillis();

			try {
				c = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {
					if (view.mMode == GameSurfaceView.STATE_RUNNING) {
						view.onDraw(c);
					}
				}
			} finally {

				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}

			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
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