package br.com.twolions.screens;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.twolions.R;
import br.com.twolions.adapters.ListItemAdapter;
import br.com.twolions.core.MabooActivity;
import br.com.twolions.dao.ItemLogDAO;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.modelobj.Carro;
import br.com.twolions.modelobj.ItemLog;
import br.com.twolions.modelobj.ListItemLog;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.util.AndroidUtils;
import br.com.twolions.util.Constants;

public class ListItemScreen extends MabooActivity implements InterfaceBar,
		OnItemClickListener, Transaction {
	protected static final int INSERIR_EDITAR = 1;

	private String TAG = Constants.LOG_APP;

	public static ItemLogDAO dao;

	private List<ItemLog> itens;

	private ListView listview_log;

	private Long id_item;
	private Long id_car;
	private String name_car;

	private int type;

	// menu
	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	private MenuDialog customMenuDialog;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		try {

			dao = new ItemLogDAO(this);

		} catch (SQLException e) {
			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

			e.printStackTrace();

		}

		// id do carro da vez
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			id_car = extras.getLong(Carro._ID);
			if (id_car != null) {

				name_car = extras.getString(Carro.NOME);

				montaTela(icicle);

			}
		}

		organizeBt();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/
	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {

		setContentView(R.layout.list_log);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setAdapter(new ListItemAdapter(this, itens));

		// cria title
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(name_car);

		// tamanho da fonte, para não estourar o espaço
		if (title.getText().length() > 10) {
			title.setTextSize(15);
		}

		// listview_log.setOnItemClickListener(this);
		// listeningGesture();

		itens = (List<ItemLog>) getLastNonConfigurationInstance();

		effect(); // effect for opening

		Log.i(TAG, "Lendo estado: getLastNonConfigurationInstance()");

		if (icicle != null) {

			// Recuperamos a lista de carros salva pelo
			// onSaveInstanceState(bundle)
			ListItemLog lista = (ListItemLog) icicle
					.getSerializable(ListItemLog.KEY);

			// Log.i(TAG, "Lendo estado: savedInstanceState(carros)");

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
		outState.putSerializable(ListItemLog.KEY, new ListItemLog(itens));
	}

	public void execute() throws Exception {
		this.itens = getItens();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Log.i(TAG,
				"O Estado da Tela foi Mudado: onConfigurationChanged(newConfig)");

		if (!customMenuDialog.isShowing()) {
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				// abre lista de logs do carro
				final Intent it = new Intent(this, ViewGraphicScreen.class);
				// Abre a tela de edição
				startActivity(it);
			}
		}

	}

	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarCarro retornar, seja se foi para adicionar
		// vamos atualizar a lista
		// if (codigoRetorno == RESULT_OK) {
		// atualiza a lista na tela
		update();
		// }

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	public void effect() {

		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		listview_log.setLayoutAnimation(controller);

	}

	public void update() {

		Log.i(TAG, "Update in list items.");

		// Pega a lista de carros e exibe na tela
		itens = getItens();

		listview_log.setAdapter(new ListItemAdapter(this, itens));

		effect(); // efeito alpha

		confListForLongClick();
	}

	private void confListForLongClick() {

		listview_log.setOnItemClickListener(this);

		listview_log
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> av, View v,
							int pos, long id) {

						ListItemScreen.this.onItemLongClick(av, v, pos, id);

						return true;
					}
				});

	}

	private List<ItemLog> getItens() {
		List<ItemLog> list = null;

		try {
			list = dao.listarItemLogs(id_car);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void showBtsEditDelete(final View view, boolean exibe) {

		// final RelativeLayout item = (RelativeLayout)
		// view.findViewById(R.id.r_item_log);

		// prepara animação (left to right)
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		if (exibe) {

			RelativeLayout item = (RelativeLayout) view
					.findViewById(R.id.r_item_log);
			if (item.getVisibility() == View.GONE) { // para que ele não
														// repita a animação
				return;
			}

			item.setVisibility(View.GONE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);

			tb_edicao.setLayoutAnimation(controller);
			tb_edicao.setVisibility(View.VISIBLE);

			// prepara animação (right to left)
			controller = AnimationUtils.loadLayoutAnimation(this,
					R.anim.anime_slide_to_left);

			item.setLayoutAnimation(controller);

		} else {

			RelativeLayout item = (RelativeLayout) view
					.findViewById(R.id.r_item_log);
			if (item.getVisibility() == View.VISIBLE) { // para que ele não
														// repita a animação
				return;
			}

			item.setVisibility(View.VISIBLE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);
			tb_edicao.setVisibility(View.GONE);

		}

		final Handler handler = new Handler();
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						RelativeLayout item = (RelativeLayout) view
								.findViewById(R.id.r_item_log);
						if (item.getVisibility() == View.VISIBLE) { // para que
																	// ele não
							// repita a animação
							return;
						}

						item.setVisibility(View.VISIBLE);

						LinearLayout tb_edicao = (LinearLayout) view
								.findViewById(R.id.tb_edicao);
						tb_edicao.setVisibility(View.GONE);
					}
				});
			}
		}, 3000);

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

	/**
	 * Excluir o carro
	 * 
	 * @param id
	 */
	protected void excluirItem(long id) {
		dao.deletar(id);
	}

	private void createItem(int type) {

		// closed menu for select item
		customMenuDialog.dismiss();

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// tipo de tarefa
		it.putExtra("task", "create");

		// Passa o id do carro como parâmetro
		it.putExtra(ItemLog.TYPE, type);

		// Passa o id do carro como parâmetro
		it.putExtra(Carro._ID, id_car);

		// Passa também o nome do carro para ser usado no titulo
		it.putExtra(Carro.NOME, name_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	/**
	 * Recupera o id do carro, e abre a tela de edição
	 * 
	 * @param v
	 */
	public void editItem(View v) {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		it.putExtra("task", "edit");

		// id do item
		it.putExtra(ItemLog._ID, id_item);

		// Passa também o nome do carro para ser usado no titulo
		it.putExtra(Carro.NOME, name_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}

	/**
	 * Abre a tela apenas de visualizaçãod o item
	 */
	private void openViewItem() {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, ViewItemScreen.class);

		// id do item
		it.putExtra(ItemLog._ID, id_item);

		// Passa também o nome do carro para ser usado no titulo
		it.putExtra(Carro.NOME, name_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/

	// private static int position = 0;
	// private static View element;

	public void deleteConConfirm(View v) {

		deleteConConfirm();

	}

	public void btBarLeft(View v) {

		finish(); // go next screen

	}

	public void organizeBt() {

		// bt left
		ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_back);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	/******************************************************************************
	 * GESTURE
	 ******************************************************************************/
	// private int REL_SWIPE_MIN_DISTANCE;
	// private int REL_SWIPE_MAX_OFF_PATH;
	// private int REL_SWIPE_THRESHOLD_VELOCITY;
	//
	// private void listeningGesture() {
	//
	// // As paiego pointed out, it's better to use density-aware measurements.
	// DisplayMetrics dm = getResources().getDisplayMetrics();
	// REL_SWIPE_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
	// REL_SWIPE_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
	// REL_SWIPE_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f +
	// 0.5);
	//
	// final GestureDetector gestureDetector = new GestureDetector(
	// new MyGestureDetector());
	//
	// View.OnTouchListener gestureListener = new View.OnTouchListener() {
	//
	// public boolean onTouch(View v, MotionEvent e) {
	//
	// Log.i(TAG, "onTouch > onTouch!");
	//
	// position = listview_log.pointToPosition((int) e.getX(),
	// (int) e.getY());
	//
	// Log.i(TAG, "onTouch > [position: " + position + "]");
	//
	// element = v;
	//
	// return gestureDetector.onTouchEvent(e);
	//
	// }
	//
	// };
	//
	// listview_log.setOnTouchListener(gestureListener);
	//
	// // Long-click still works in the usual way.
	// listview_log
	// .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	// public boolean onItemLongClick(AdapterView<?> parent,
	// View view, int position, long id) {
	//
	// ItemLog item = itens.get(position);
	// id_item = item.getId();
	//
	// showBtsEditDelete(true);
	//
	// return true;
	// }
	// });
	//
	// listview_log
	// .setOnItemClickListener(new AdapterView.OnItemClickListener() {
	// public void onItemClick(AdapterView<?> parent, View view,
	// int pos, long id) {
	//
	// // Log.i(TAG, "onItemClick > onItemClick in position[" +
	// // pos + "]");
	//
	// // element = null;
	//
	// // element = view;
	//
	// // position = pos;
	//
	// }
	// });
	// }

	public void onItemLongClick(AdapterView<?> parent, View view, int pos,
			long id) {

		ItemLog item = itens.get(pos);
		id_item = item.getId();

		showBtsEditDelete(view, true);

	}

	// Do not use LitView.setOnItemClickListener(). Instead, I override
	// SimpleOnGestureListener.onSingleTapUp() method, and it will call to this
	// method when
	// it detects a tap-up event.
	// private void myOnItemClick(int position) {
	//
	// Log.i(TAG, "myOnItemClick > [ position: " + position + "]");
	//
	// // get the row the clicked button is in
	// id_car = itens.get(position).getId_car();
	// id_item = itens.get(position).getId();
	//
	// // open list item log
	// openViewItem();
	// }

	public void onItemClick(final AdapterView<?> parent, View view,
			final int pos, final long id) {

		Log.i(TAG, "myOnItemClick > [ position: " + pos + "]");

		// get the row the clicked button is in
		id_car = itens.get(pos).getId_car();
		id_item = itens.get(pos).getId();

		// open list item log
		openViewItem();

	}

	// private void onLTRFling() {
	//
	// Log.i(TAG, "onLTRFling > Left-to-right fling in position[" + position
	// + "]");
	//
	// try {
	// /*
	// * Toast.makeText(this, "Left-to-right fling in position[" +
	// * position + "]", Toast.LENGTH_SHORT).show();
	// */
	//
	// ItemLog item = itens.get(position);
	//
	// id_item = item.getId();
	// id_car = item.getId_car();
	// type = item.getType();
	//
	// showBtsEditDelete(true);
	//
	// } catch (Exception e) {
	// Log.i(TAG, "! element esta null");
	// }
	//
	// }

	// private void onRTLFling() {
	//
	// Log.i(TAG, "onRTLFling > Right-to-left fling in position[" + position
	// + "]");
	//
	// try {
	// /*
	// * Toast.makeText(this, "Right-to-left fling in position[" +
	// * position + "]", Toast.LENGTH_SHORT).show();
	// */
	//
	// ItemLog item = itens.get(position);
	// id_item = item.getId();
	// id_car = item.getId_car();
	// type = item.getType();
	//
	// showBtsEditDelete(false);
	//
	// } catch (Exception e) {
	// Log.i(TAG, "! element esta null");
	// }
	// }

	// class MyGestureDetector extends SimpleOnGestureListener {
	//
	// // Detect a single-click and call my own handler.
	// public boolean onSingleTapUp(MotionEvent e) {
	//
	// // ListView lv = listview_log;
	// int pos = listview_log.pointToPosition((int) e.getX(),
	// (int) e.getY());
	//
	// if (pos < 0) { // as vezes a position na list retornava a mesma
	// // posição mas negativo
	// pos = pos * (-1);
	// }
	//
	// myOnItemClick(pos);
	//
	// return false;
	// }
	//
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	// if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH)
	// return false;
	//
	// if (e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE
	// && Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
	//
	// // onRTLFling();
	//
	// } else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE
	// && Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
	//
	// // onLTRFling();
	// }
	//
	// return false;
	// }
	//
	// }

	/******************************************************************************
	 * BARRA DE ITENS
	 ******************************************************************************/

	public void btBarRight(View v) {

		if (customMenuDialog == null) { // instancia o menu apenas uma vez

			customMenuDialog = new MenuDialog(this);

		}

		if (!customMenuDialog.isShowing()) {
			customMenuDialog.show();
		}

	}

	private class MenuDialog extends AlertDialog {
		public MenuDialog(Context context) {
			super(context);

			View cus_menu = getLayoutInflater().inflate(R.layout.custom_menu,
					null);

			setView(cus_menu);

		}

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			ImageView btFuel = (ImageView) findViewById(R.id.btFuel);
			btFuel.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					createItem(Constants.FUEL);

				}

			});

			ImageView btExpense = (ImageView) findViewById(R.id.btExpense);
			btExpense.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					createItem(Constants.EXPENSE);

				}

			});

			ImageView btNote = (ImageView) findViewById(R.id.btNote);
			btNote.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					createItem(Constants.NOTE);

				}

			});

			ImageView btRepair = (ImageView) findViewById(R.id.btRepair);
			btRepair.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					createItem(Constants.REPAIR);

				}

			});

		}

		/**
		 * Verifica onde foi o clique do usuario, se foi no menu de item (ok),
		 * se não (fecha o menu)
		 */
		public boolean onTouchEvent(MotionEvent event) {

			// I only care if the event is an UP action
			if (event.getAction() == MotionEvent.ACTION_UP) {

				// create a rect for storing the window rect
				Rect r = new Rect(0, 0, 0, 0);

				// retrieve the windows rect
				this.getWindow().getDecorView().getHitRect(r);

				// check if the event position is inside the window rect
				boolean intersects = r.contains((int) event.getX(),
						(int) event.getY());

				// if the event is not inside then we can close the activity
				if (!intersects) {

					// close the activity
					customMenuDialog.dismiss();

					// notify that we consumed this event
					return true;
				}
			}
			// let the system handle the event
			return super.onTouchEvent(event);
		}

	}

}
