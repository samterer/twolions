package br.com.maboo.fuellist.screens;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.adapters.ListItemAdapter;
import br.com.maboo.fuellist.core.FuelListActivity;
import br.com.maboo.fuellist.dao.ItemLogDAO;
import br.com.maboo.fuellist.interfaces.InterfaceBar;
import br.com.maboo.fuellist.modelobj.Carro;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.modelobj.ListItemLog;
import br.com.maboo.fuellist.modelobj.Settings;
import br.com.maboo.fuellist.transaction.Transaction;
import br.com.maboo.fuellist.util.AndroidUtils;
import br.com.maboo.fuellist.util.Constants;

public class ListItemScreen extends FuelListActivity implements InterfaceBar,
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

	private Settings set;

	// item selecionado
	private View itemSelectView;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		try {

			dao = new ItemLogDAO(this);

			setContentView(R.layout.list_log);

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

		getSharedPrefs();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/
	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {

		listview_log = (ListView) findViewById(R.id.listview_log);

		// cria title
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(name_car);

		// tamanho da fonte, para não estourar o espaço
		if (title.getText().length() > 10) {
			title.setTextSize(15);
		}

		// salva itens na memoria
		itens = (List<ItemLog>) getLastNonConfigurationInstance();

		// Log.i("estado", "Lendo estado: getLastNonConfigurationInstance()");

		if (icicle != null) {

			// Recuperamos a lista de carros salva pelo
			// onSaveInstanceState(bundle)
			ListItemLog lista = (ListItemLog) icicle
					.getSerializable(ListItemLog.KEY);

			// Log.i(TAG, "Lendo estado: savedInstanceState(itens)");

			this.itens = lista.itens;

		}

		if (itens != null) { // Atualiza o ListView diretamente

			listview_log.setAdapter(new ListItemAdapter(this, itens, set));

		} else {

			startTransaction(this); // incia tela

		}

	}

	public Object onRetainNonConfigurationInstance() {
		// Log.i("estado",
		// "Salvando Estado: onRetainNonConfigurationInstance()");

		return itens;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Log.i("estado", "Salvando Estado: onSaveInstanceState(bundle)");

		// Salvar o estado da tela
		outState.putSerializable(ListItemLog.KEY, new ListItemLog(itens));
	}

	public void execute() throws Exception {
		this.itens = getItens();
	}

	public void onConfigurationChanged(Configuration newConfig) {

		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		int myOrientation = display.getOrientation();

		if (newConfig.orientation != myOrientation) {
			Log.v("appLog", "rotated");
		}

		super.onConfigurationChanged(newConfig);
	}

	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		update(); // re-carrega a lista

	}

	private void getSharedPrefs() {

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		this.set = new Settings(sharedPrefs);

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	public void effect() {

		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		listview_log.setLayoutAnimation(controller);

	}

	/**
	 * Atualiza a lista de itens (listAdapter)
	 */
	public void update() {

		getSharedPrefs();

		itens = getItens();

		listview_log.setAdapter(new ListItemAdapter(this, itens, set));

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

	/**
	 * Recupera a lista de itens
	 */
	private List<ItemLog> getItens() {
		List<ItemLog> list = null;

		try {
			list = dao.listarItemLogs(id_car);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void showBtsEditDelete(View view, boolean exibe) {
		itemSelectView = view;

		// prepara animação (left to right)
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		if (exibe) {

			RelativeLayout item = (RelativeLayout) itemSelectView
					.findViewById(R.id.r_item_log);

			// if (item.getVisibility() == View.GONE) { // para que ele não
			// // repita a animação
			// return;
			// }
			item.setVisibility(View.GONE);

			LinearLayout tb_edicao = (LinearLayout) itemSelectView
					.findViewById(R.id.tb_edicao);

			tb_edicao.setLayoutAnimation(controller);
			tb_edicao.setVisibility(View.VISIBLE);

			// prepara animação (right to left)
			controller = AnimationUtils.loadLayoutAnimation(this,
					R.anim.anime_slide_to_left);

			item.setLayoutAnimation(controller);

			hideOtherItens(view);

		} else {

			RelativeLayout item = (RelativeLayout) itemSelectView
					.findViewById(R.id.r_item_log);

			// if (item.getVisibility() == View.VISIBLE) { // para que ele não
			// repita a animação
			// return;
			// }

			item.setVisibility(View.VISIBLE);

			LinearLayout tb_edicao = (LinearLayout) itemSelectView
					.findViewById(R.id.tb_edicao);
			tb_edicao.setVisibility(View.GONE);

		}

		final Handler handler = new Handler();
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						RelativeLayout item = (RelativeLayout) itemSelectView
								.findViewById(R.id.r_item_log);
						// if (item.getVisibility() == View.VISIBLE) { // para
						// que
						// ele não
						// repita a animação
						// return;
						// }

						item.setVisibility(View.VISIBLE);

						LinearLayout tb_edicao = (LinearLayout) itemSelectView
								.findViewById(R.id.tb_edicao);
						tb_edicao.setVisibility(View.GONE);
					}
				});
			}
		}, 3000);

	}

	/**
	 * Esconde todos os outros itens
	 */
	private void hideOtherItens(View currentView) {
		for (int i = 0; i < listview_log.getCount(); i++) {
			View item = (View) listview_log.getChildAt(i);
			if (item != currentView) {
				// item.setVisibility(View.INVISIBLE);
				showBtsEditDelete(item, false);
			}
		}
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

	public void deleteConConfirm(View v) {

		deleteConConfirm();

	}

	public void btBarLeft(View v) {

		finish(); // go next screen

		overridePendingTransition(R.anim.slide_to_right, R.anim.slide_to_right);

	}

	public void organizeBt() {

		// bt left
		ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_back);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

	}

	public void onBackPressed() {
		super.onBackPressed();

		overridePendingTransition(R.anim.slide_to_right, R.anim.slide_to_right);
	}

	public void onItemLongClick(AdapterView<?> parent, View view, int pos,
			long id) {

		ItemLog item = itens.get(pos);
		id_item = item.getId();

		showBtsEditDelete(view, true);

	}

	public void onItemClick(final AdapterView<?> parent, View view,
			final int pos, final long id) {

		Log.i("test", "onItemClick > [ position: " + pos + "]");

		// get the row the clicked button is in
		id_car = itens.get(pos).getId_car();
		id_item = itens.get(pos).getId();

		// open list item log
		openViewItem();

	}

	public void openSettings(View v) {
		startActivity(new Intent(this, SettingsScreen.class));

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

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
