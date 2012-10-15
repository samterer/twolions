package br.com.twolions.screens;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import br.com.twolions.R;
import br.com.twolions.core.FuelActivity;
import br.com.twolions.dao.ItemLogDAO;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.daoobjects.ListItemLog;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.object.ListItemAdapter;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.util.Constants;

public class ListLogScreen extends FuelActivity implements OnItemClickListener,
		InterfaceBar, Transaction {
	protected static final int INSERIR_EDITAR = 1;

	private String TAG = Constants.LOG_APP;

	public static ItemLogDAO dao;

	private List<ItemLog> itens;

	private ListView listview_log;

	private Long id_item;
	private Long id_car;
	private int type;

	// menu
	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		dao = new ItemLogDAO(this);

		// id do carro da vez
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id_car = extras.getLong(Carro._ID);
			if (id_car != null) {
				montaTela(icicle);
				organizeBt();
			}
		}

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/
	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {
		setContentView(R.layout.list_log);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setAdapter(new ListItemAdapter(this, itens));
		listview_log.setOnItemClickListener(this);

		itens = (List<ItemLog>) getLastNonConfigurationInstance();

		effect(); // effect for opening

		Log.i(TAG, "Lendo estado: getLastNonConfigurationInstance()");
		if (icicle != null) {
			// Recuperamos a lista de carros salva pelo
			// onSaveInstanceState(bundle)
			ListItemLog lista = (ListItemLog) icicle
					.getSerializable(ListItemLog.KEY);
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
		outState.putSerializable(ListItemLog.KEY, new ListItemLog(itens));
	}

	public void execute() throws Exception {
		this.itens = getItens();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.i(TAG,
				"O Estado da Tela foi Mudado: onConfigurationChanged(newConfig)");
		if (!isMenuVisible) {
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
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			update();
		}
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

						ListLogScreen.this.onItemLongClick(av, v, pos, id);

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

		// prepara animação (left to right)
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		if (exibe) {
			// Toast.makeText(this, "exibindo...", Toast.LENGTH_SHORT).show();

			RelativeLayout item = (RelativeLayout) view
					.findViewById(R.id.r_item_log);

			// item.setLayoutAnimation(controller);
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
			// Toast.makeText(this, "apagando...", Toast.LENGTH_SHORT).show();

			RelativeLayout item = (RelativeLayout) view
					.findViewById(R.id.r_item_log);
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

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(ItemLog.TYPE, type);

		// Passa o id do carro como parâmetro
		it.putExtra(Carro._ID, id_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/
	private String oldPosition = null;
	private String getSelectedItemOfList;

	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

		getSelectedItemOfList = (parent.getItemAtPosition(pos)).toString();

		// Log.i(TAG, "getSelectedItemOfList [" + getSelectedItemOfList + "]");

		// get the row the clicked button is in
		id_car = itens.get(pos).getId_car();
		id_item = itens.get(pos).getId();

		// open list item log
		openViewItem();

	}

	/**
	 * Abre lista de itens
	 */
	private void openViewItem() {

		// Log.i(TAG, "OPEN LIST CAR [" + id_car + "]");

		// abre lista de logs do carro
		final Intent it = new Intent(this, ViewItemScreen.class);

		// Abre a tela de edição
		startActivity(it);

	}

	public void onItemLongClick(AdapterView<?> arg0, View view, int pos, long id) {

		ItemLog item = itens.get(pos);
		id_item = item.getId();
		id_car = item.getId_car();
		type = item.getType();

		showBtsEditDelete(view, true);

	}

	public void deleteConConfirm(View v) {
		deleteConConfirm();
	}

	/**
	 * Recupera o id do carro, e abre a tela de edição
	 * 
	 * @param v
	 */
	public void editItem(View v) {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// id do item
		it.putExtra(ItemLog._ID, id_item);
		// passa o tipo do item
		it.putExtra(ItemLog.TYPE, type);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}

	public void btBarLeft(View v) {

		// go next screen
		finish();

	}

	public void organizeBt() {
		// bt left
		ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_cancel);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

	}

	public void onBackPressed() { // call my backbutton pressed method when
									// boolean==true
		Log.i(TAG, "Clicked back");

	}

	MenuDialog customMenuDialog;
	boolean isMenuVisible = false;

	public void btBarRight(View v) {

		if (customMenuDialog == null) {
			customMenuDialog = new MenuDialog(this);

			isMenuVisible = true;

			customMenuDialog.show();
		} else {
			customMenuDialog.dismiss();

			isMenuVisible = false;
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

		public void onBackPressed() { // call my backbutton pressed method when
			// boolean==true

			Log.i(TAG, "Clicked back");

		}

	}
}
