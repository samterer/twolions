package br.com.maboo.neext.screens;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

		listeningGesture();

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

	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// atualiza a lista na tela
		update();

	}

	public void update() {

		// Pega a lista de carros e exibe na tela
		itens = getItens();

		listview_log.setAdapter(new ListAdapter(this, itens));

		effect(); // efeito alpha

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

	public void deleteConConfirm() {
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage(R.string.a_dt_item);
		// Método executado se escolher Sim
		alerta.setPositiveButton(R.string.a_y,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						deleteItem();
					}
				});
		// Método executado se escolher Não
		alerta.setNegativeButton(R.string.a_n,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//
					}
				});
		// Exibe o alerta de confirmação
		alerta.show();
	}

	// delete car
	public void deleteItem() {
		if (id_item != null) {
			excluirItem(id_item);

			// OK
			setResult(RESULT_OK);

			// atualiza a lista na tela
			update();
		}

	}

	protected void excluirItem(long id) {
		dao.deletar(id);
	}

	public void deleteConConfirm(View v) {

		deleteConConfirm();

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

	public void showBtsEditDelete(boolean exibe) throws NullPointerException {

		Log.i(TAG, "showBtsEditDelete > [ exibe:+" + exibe + "+] element: "
				+ element.toString());

		RelativeLayout item = (RelativeLayout) element
				.findViewById(R.id.r_item_log);

		// prepara animação (left to right)
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		if (exibe) {

			if (item.getVisibility() == View.GONE) { // para que ele não
														// repita a animação
				return;
			}

			item.setVisibility(View.GONE);

			LinearLayout tb_edicao = (LinearLayout) element
					.findViewById(R.id.tb_edicao);

			tb_edicao.setLayoutAnimation(controller);
			tb_edicao.setVisibility(View.VISIBLE);

			// prepara animação (right to left)
			controller = AnimationUtils.loadLayoutAnimation(this,
					R.anim.anime_slide_to_left);

			item.setLayoutAnimation(controller);

		} else {

			if (item.getVisibility() == View.VISIBLE) { // para que ele não
														// repita a animação
				return;
			}

			item.setVisibility(View.VISIBLE);

			LinearLayout tb_edicao = (LinearLayout) element
					.findViewById(R.id.tb_edicao);
			tb_edicao.setVisibility(View.GONE);

		}

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

	/******************************************************************************
	 * GESTURE
	 ******************************************************************************/
	private int REL_SWIPE_MIN_DISTANCE;
	private int REL_SWIPE_MAX_OFF_PATH;
	private int REL_SWIPE_THRESHOLD_VELOCITY;

	private static int position = 0;
	private static View element;

	private void listeningGesture() {

		// As paiego pointed out, it's better to use density-aware measurements.
		DisplayMetrics dm = getResources().getDisplayMetrics();
		REL_SWIPE_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);

		final GestureDetector gestureDetector = new GestureDetector(
				new MyGestureDetector());

		View.OnTouchListener gestureListener = new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent e) {

				Log.i(TAG, "onTouch > onTouch!");

				position = listview_log.pointToPosition((int) e.getX(),
						(int) e.getY());

				Log.i(TAG, "onTouch > [position: " + position + "]");

				element = v;

				return gestureDetector.onTouchEvent(e);

			}

		};

		listview_log.setOnTouchListener(gestureListener);

		// Long-click still works in the usual way.
		listview_log
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {

						ItemNote item = itens.get(position);
						id_item = item.getId();

						// showBtsEditDelete(true);

						return true;
					}
				});

		listview_log
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int pos, long id) {

						Log.i(TAG, "onItemClick > onItemClick in position["
								+ pos + "]");

						// element = null;

						// element = view;

						// position = pos;

					}
				});
	}

	// Do not use LitView.setOnItemClickListener(). Instead, I override
	// SimpleOnGestureListener.onSingleTapUp() method, and it will call to this
	// method when
	// it detects a tap-up event.
	private void myOnItemClick(int position) {

		Log.i(TAG, "myOnItemClick > [ position: " + position + "]");

		// get the row the clicked button is in
		id_item = itens.get(position).getId();

		// open list item log
		openViewItem();
	}

	private void onLTRFling() {

		Log.i(TAG, "onLTRFling > Left-to-right fling in position[" + position
				+ "]");

		try {
			/*
			 * Toast.makeText(this, "Left-to-right fling in position[" +
			 * position + "]", Toast.LENGTH_SHORT).show();
			 */

			ItemNote item = itens.get(position);

			id_item = item.getId();

			showBtsEditDelete(true);

		} catch (Exception e) {
			Log.i(TAG, "! element esta null");
		}

	}

	private void onRTLFling() {

		Log.i(TAG, "onRTLFling > Right-to-left fling in position[" + position
				+ "]");

		try {
			/*
			 * Toast.makeText(this, "Right-to-left fling in position[" +
			 * position + "]", Toast.LENGTH_SHORT).show();
			 */

			ItemNote item = itens.get(position);
			id_item = item.getId();

			showBtsEditDelete(false);

		} catch (Exception e) {
			Log.i(TAG, "! element esta null");
		}
	}

	class MyGestureDetector extends SimpleOnGestureListener {

		// Detect a single-click and call my own handler.
		public boolean onSingleTapUp(MotionEvent e) {

			// ListView lv = listview_log;
			int pos = listview_log.pointToPosition((int) e.getX(),
					(int) e.getY());

			if (pos < 0) { // as vezes a position na list retornava a mesma
							// posição mas negativo
				pos = pos * (-1);
			}

			myOnItemClick(pos);

			return false;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH)
				return false;

			if (e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

				onRTLFling();

			} else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

				onLTRFling();
			}

			return false;
		}

	}

}
