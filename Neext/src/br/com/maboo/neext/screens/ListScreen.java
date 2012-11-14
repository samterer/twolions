package br.com.maboo.neext.screens;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.maboo.neext.R;
import br.com.maboo.neext.adapters.ListAdapter;
import br.com.maboo.neext.core.NeextActivity;
import br.com.maboo.neext.dao.ItemNoteDAO;
import br.com.maboo.neext.interfaces.InterfaceBar;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.modelobj.ListNote;
import br.com.maboo.neext.transaction.Transaction;
import br.com.maboo.neext.util.Constants;

public class ListScreen extends NeextActivity implements InterfaceBar,
		Transaction {

	private String TAG = Constants.LOG_APP;

	protected static final int INSERIR_EDITAR = 1;

	public static ItemNoteDAO dao;

	private List<ItemNote> itens;

	private ListView listview_log;

	private Long id_item;

	private int type;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		dao = new ItemNoteDAO(this);

		montaTela(icicle);

		organizeBt();

	}

	/****************************************************************
	 * ESTADO
	 ****************************************************************/

	@SuppressWarnings("unchecked")
	private void montaTela(Bundle icicle) {

		setContentView(R.layout.list_layout);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setAdapter(new ListAdapter(this, itens));

		itens = (List<ItemNote>) getLastNonConfigurationInstance();

		effect(); // effect for opening

		Log.i(TAG, "Lendo estado: getLastNonConfigurationInstance()");

		if (icicle != null) {

			ListNote lista = (ListNote) icicle.getSerializable(ListNote.KEY);

			Log.i(TAG, "Lendo estado: savedInstanceState(carros)");

			this.itens = lista.itens;

		}

		if (itens != null) { // Atualiza o ListView diretamente

			listview_log.setAdapter(new ListAdapter(this, itens));

		} else {

			startTransaction(this);

		}

	}

	public void execute() throws Exception {

		this.itens = getItens();

	}

	public Object onRetainNonConfigurationInstance() {
		Log.i(TAG, "Salvando Estado: onRetainNonConfigurationInstance()");

		return itens;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.i(TAG, "Salvando Estado: onSaveInstanceState(bundle)");

		// Salvar o estado da tela
		outState.putSerializable(ListNote.KEY, new ListNote(itens));
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	/****************************************************************
	 * SERVICES
	 ****************************************************************/

	public void effect() {
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		listview_log.setLayoutAnimation(controller);
	}

	private List<ItemNote> getItens() {
		List<ItemNote> list = null;

		try {

			list = dao.listarItemNotes();

		} catch (NullPointerException e) {

			e.printStackTrace();

		}

		return list;
	}

	/**
	 * Abre a tela apenas de visualizaçãod o item
	 */
	private void openViewItem() {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, ViewItemScreen.class);

		// id do item
		it.putExtra(ItemNote._ID, id_item);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

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

	public void createItem(View v) {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// tipo de tarefa
		it.putExtra("task", "create");

		// Passa o id do carro como parâmetro
		it.putExtra(ItemNote.TYPE, type);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

	public void changeColor(View v) {

	}

	public void update() {
		// TODO Auto-generated method stub

	}
}
