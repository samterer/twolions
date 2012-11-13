package br.com.maboo.neext.screens;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.maboo.neext.R;
import br.com.maboo.neext.adapters.ListItemAdapter;
import br.com.maboo.neext.core.NeextActivity;
import br.com.maboo.neext.interfaces.InterfaceBar;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.modelobj.ListNoteLog;
import br.com.maboo.neext.transaction.Transaction;
import br.com.maboo.neext.util.Constants;

public class ListScreen extends NeextActivity implements InterfaceBar,
		Transaction {

	private String TAG = Constants.LOG_APP;

	private List<ItemNote> itens;

	private ListView listview_log;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// gera lista teste
		geraListaTeste();

		init(icicle);

		organizeBt();

	}

	/****************************************************************
	 * SERVICES
	 ****************************************************************/

	private List<ItemNote> geraListaTeste() {
		List<ItemNote> list = null;

		String[] subj = { "teste1", "teste2", "teste3" };

		String[] date = { "13/11/2012 - 23:42", "18/12/2012 - 23:42",
				"22/11/2009 - 08:42" };

		for (int i = 0; i < subj.length; i++) {

			ItemNote obj = new ItemNote(date[i], subj[i], subj[i] + " texto.");

			list.add(obj);
		}

		return list;
	}

	private void init(Bundle icicle) {

		setContentView(R.layout.list_layout);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setAdapter(new ListItemAdapter(this, itens));

		itens = (List<ItemNote>) getLastNonConfigurationInstance();

		Log.i(TAG, "Lendo estado: getLastNonConfigurationInstance()");

		if (icicle != null) {

			// Recuperamos a lista de carros salva pelo
			// onSaveInstanceState(bundle)
			ListNoteLog lista = (ListNoteLog) icicle
					.getSerializable(ListNoteLog.KEY);

			Log.i(TAG, "Lendo estado: savedInstanceState(carros)");

			this.itens = lista.itens;

		}

		if (itens != null) { // Atualiza o ListView diretamente

			listview_log.setAdapter(new ListItemAdapter(this, itens));

		} else {

			startTransaction(this);

		}

	}

	public Object onRetainNonConfigurationInstance() {
		Log.i(TAG, "Salvando Estado: onRetainNonConfigurationInstance()");

		return itens;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.i(TAG, "Salvando Estado: onSaveInstanceState(bundle)");

		// Salvar o estado da tela
		outState.putSerializable(ListNoteLog.KEY, new ListNoteLog(itens));
	}

	public void execute() throws Exception {
		this.itens = getItens();
	}

	private List<ItemNote> getItens() {
		List<ItemNote> list = null;

		try {
			list = geraListaTeste();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	/****************************************************************
	 * ESTADO
	 ****************************************************************/

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	/****************************************************************
	 * TOUCH
	 ****************************************************************/

	public boolean dispatchTouchEvent(MotionEvent event) {
		// View v = getCurrentFocus();

		boolean ret = super.dispatchTouchEvent(event);

		try {

			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public void organizeBt() {

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_about);

	}

	public void btBarLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarRight(View v) {

		startActivity(new Intent(this, AboutScreen.class));

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	public void update() {
		// TODO Auto-generated method stub

	}
}
