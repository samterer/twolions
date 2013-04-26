package br.com.maboo.tubarao.core;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import br.com.maboo.tubarao.R;
import br.com.maboo.tubarao.screen.GameScreen;

public class GameActivity extends ActivityCircle {

	private static final int MENU_PAUSE = 4;
	private static final int MENU_RESUME = 5;
	private static final int MENU_CLEAR = 6;
	private static final int MENU_STOP = 7;

	private GameScreen mGameScreen;

	public static TextView tPontos;
	public static TextView tTempo;

	/**
	 * Invoked during init to give the Activity a chance to set up its Menu.
	 * 
	 * @param menu
	 *            the Menu to which entries may be added
	 * @return true
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_CLEAR, 0, R.string.menu_clear);
		menu.add(0, MENU_STOP, 0, R.string.menu_exit);
		menu.add(0, MENU_PAUSE, 0, R.string.menu_pause);
		menu.add(0, MENU_RESUME, 0, R.string.menu_resume);

		return true;
	}

	/**
	 * Invoked when the user selects an item from the Menu.
	 * 
	 * @param item
	 *            the Menu entry which was selected
	 * @return true if the Menu item was legit (and we consumed it), false
	 *         otherwise
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CLEAR:
			mGameScreen.clearSprites();
			return true;
		case MENU_STOP:
			finish();
			return true;
		case MENU_PAUSE:
			mGameScreen.pause();
			return true;
		case MENU_RESUME:
			mGameScreen.unpause();
			return true;
		}

		return false;
	}

	/**
	 * Invoked when the Activity is created.
	 * 
	 * @param savedInstanceState
	 *            a Bundle containing state saved from a previous execution, or
	 *            null if this is a new execution
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// tell system to use the layout defined in our XML file
		setContentView(R.layout.s_game);

		tPontos = (TextView) findViewById(R.id.pontos);
		tTempo = (TextView) findViewById(R.id.tempo);

		// get handles to the LunarView from XML, and its LunarThread
		mGameScreen = (GameScreen) findViewById(R.id.view);

		// give the LunarView a handle to the TextView used for messages
		mGameScreen.setTextView((TextView) findViewById(R.id.text));

		if (savedInstanceState == null) {
			// we were just launched: set up a new game
			mGameScreen.setState(GameScreen.STATE_READY);
			Log.i("game", "SIS is null");
		} else {
			// we are being restored: resume a previous game
			mGameScreen.restoreState(savedInstanceState);
			Log.i("game", "SIS is nonnull");
		}
	}

	/**
	 * Invoked when the Activity loses user focus.
	 */
	protected void onPause() {
		super.onPause();
		mGameScreen.pause(); // pause game when Activity pauses
	}

	/**
	 * Notification that something is about to happen, to give the Activity a
	 * chance to save state.
	 * 
	 * @param outState
	 *            a Bundle into which this Activity should save its state
	 */
	protected void onSaveInstanceState(Bundle outState) {
		// just have the View's thread save its state into our Bundle
		super.onSaveInstanceState(outState);
		mGameScreen.saveState(outState);
	}
}