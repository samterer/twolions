package br.com.maboo.tubarao.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import br.com.maboo.tubarao.R;
import br.com.maboo.tubarao.layer.LayerManager;

@SuppressLint("DrawAllocation") public abstract class GameView extends SurfaceView implements Callback {

	/*
	 * State-tracking constants
	 */
	public static final int STATE_LOSE = 1;
	public static final int STATE_PAUSE = 2;
	public static final int STATE_READY = 3;
	public static final int STATE_RUNNING = 4;
	public static final int STATE_WIN = 5;

	/** Array com todas as imagens a serem exibidas no onDraw */
	private LayerManager mSprites;

	/** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
	protected int mMode;

	/** Message handler used by thread to interact with TextView */
	private Handler mHandler;

	private SurfaceHolder mHolder;

	private GameThread mThread;

	private Resources res;

	/** Pointer to the text view to display "Paused.." etc. */
	protected static TextView mStatusText;

	// Dados da tela
	private DisplayMetrics dm;

	public GameView(Context context, AttributeSet attrs, Handler handler) {
		super(context, attrs);

		mSprites = new LayerManager(); // nova layer de sprites

		mThread = new GameThread(this); // instancia a thread
												// principal
		res = context.getResources();

		mHandler = handler;
		mHolder = getHolder(); // recupera o holder
		mHolder.addCallback(this);

	}

	synchronized protected void onDraw(Canvas canvas) {
		canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint());

		for (int i = mSprites.size() - 1; i > 0; i--) {
			mSprites.get(i).draw(canvas);
		}

		canvas.restore();

	}

	public abstract void loop();

	// Chamado quando a tela é redimensioada, ou iniciada...
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);

		dm = new DisplayMetrics();
	}

	public void clearSprites() {
		synchronized (mSprites) {
			mSprites.clear();
			mSprites = new LayerManager();
		}
	}

	/**
	 * Restores game state from the indicated Bundle. Typically called when the
	 * Activity is being restored after having been previously destroyed.
	 * 
	 * @param icicle
	 *            Bundle containing the game state
	 */
	protected synchronized void restoreState(Bundle icicle) {
		synchronized (getHolder()) {
			setState(STATE_PAUSE);
		}
	}

	/**
	 * Dump game state to the provided Bundle. Typically called when the
	 * Activity is being suspended.
	 * 
	 * @return Bundle with this view's state
	 */
	protected Bundle saveState(Bundle icicle) {
		synchronized (getHolder()) {
			//
		}
		return icicle;
	}

	/**
	 * Resumes from a pause.
	 */
	public void unpause() {
		// Move the real time clock up to now
		synchronized (getHolder()) {
			// mLastTime = System.currentTimeMillis() + 100;
			getThread().setmLastTime(System.currentTimeMillis() + 100);
		}
		setState(STATE_RUNNING);
	}

	/**
	 * Starts the game, setting parameters for the current difficulty.
	 */
	public void doStart() {
		synchronized (getHolder()) {
			// mLastTime = System.currentTimeMillis() + 100;
			getThread().setmLastTime(System.currentTimeMillis() + 100);
			setState(STATE_RUNNING);
		}
	}

	/**
	 * Pauses the physics update & animation.
	 */
	public void pause() {
		synchronized (getHolder()) {
			if (mMode == STATE_RUNNING)
				setState(STATE_PAUSE);
		}
	}

	/**
	 * Sets the game mode. That is, whether we are running, paused, in the
	 * failure state, in the victory state, etc.
	 * 
	 * @see #setState(int, CharSequence)
	 * @param mode
	 *            one of the STATE_* constants
	 */
	public void setState(int mode) {
		synchronized (getHolder()) {
			setState(mode, null);
		}
	}

	/* Callback invoked when the surface dimensions change. */
	public void setSurfaceSize(int width, int height) {
		// synchronized to make sure these all change atomically
		synchronized (getHolder()) {
			//
		}
	}

	/**
	 * Fetches the animation thread corresponding to this LunarView.
	 * 
	 * @return the animation thread
	 */
	public GameThread getThread() {
		return mThread;
	}

	/**
	 * Standard window-focus override. Notice focus lost so we can pause on
	 * focus lost. e.g. user switches to take a call.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (!hasWindowFocus)
			pause();
	}

	/**
	 * Installs a pointer to the text view used for messages.
	 */
	public void setTextView(TextView textView) {
		mStatusText = textView;
	}

	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		setSurfaceSize(width, height);
	}

	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		// timer the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created
		mThread.setRunning(true);
		mThread.start();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		mThread.setRunning(false);
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the game mode. That is, whether we are running, paused, in the
	 * failure state, in the victory state, etc.
	 * 
	 * @param mode
	 *            one of the STATE_* constants
	 * @param message
	 *            string to add to screen or null
	 */
	public void setState(int mode, CharSequence message) {
		/*
		 * This method optionally can cause a text message to be displayed to
		 * the user when the mode changes. Since the View that actually renders
		 * that text is part of the main View hierarchy and not owned by this
		 * thread, we can't touch the state of that View. Instead we use a
		 * Message + Handler to relay commands to the main thread, which updates
		 * the user-text View.
		 */
		synchronized (getHolder()) {
			mMode = mode;

			if (mMode == STATE_RUNNING) {
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("text", "");
				b.putInt("viz", View.INVISIBLE);
				msg.setData(b);
				mHandler.sendMessage(msg);
			} else {

				CharSequence str = "";
				if (mMode == STATE_READY)
					str = res.getText(R.string.mode_ready);
				else if (mMode == STATE_PAUSE)
					str = res.getText(R.string.mode_pause);
				else if (mMode == STATE_LOSE)
					str = res.getText(R.string.mode_lose);
				else if (mMode == STATE_WIN)
					// fim de jogo com vitoria

					if (message != null) {
						str = message + "\n" + str;
					}

				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("text", str.toString());
				b.putInt("viz", View.VISIBLE);
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		}
	}

	public LayerManager getLayerManager() {
		return mSprites;
	}

	// Retorna altura da tela
	public int getDeviceScreenHeigth() {

		dm = res.getDisplayMetrics();

		return dm.heightPixels;
	}

	// Retorna largura da tela
	public int getDeviceScreenWidth() {

		dm = res.getDisplayMetrics();

		return dm.widthPixels;
	}

}