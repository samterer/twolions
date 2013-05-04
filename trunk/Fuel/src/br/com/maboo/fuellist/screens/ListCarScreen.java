package br.com.maboo.fuellist.screens;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.adapters.ListCarAdapter;
import br.com.maboo.fuellist.core.FuelListActivity;
import br.com.maboo.fuellist.dao.CarroDAO;
import br.com.maboo.fuellist.dao.ItemLogDAO;
import br.com.maboo.fuellist.interfaces.InterfaceBar;
import br.com.maboo.fuellist.modelobj.Carro;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.modelobj.ListCarros;
import br.com.maboo.fuellist.modelobj.Settings;
import br.com.maboo.fuellist.transaction.Transaction;
import br.com.maboo.fuellist.util.AndroidUtils;
import br.com.maboo.fuellist.util.Constants;

public class ListCarScreen extends FuelListActivity implements
		OnItemClickListener, InterfaceBar, Transaction {

	private final String TAG = Constants.LOG_APP;

	protected static final int INSERIR_EDITAR = 1;

	public static CarroDAO dao;

	private List<Carro> carros;

	private ListView listview_car;

	private static String name_car;
	private static Long id_car;

	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	private Settings set;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		try {

			dao = new CarroDAO(this);

			setContentView(R.layout.list_car); // instancia o layout

		} catch (SQLException e) {
			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

			e.printStackTrace();

		}

		montaTela(icicle);

		organizeBt();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {

		listview_car = (ListView) findViewById(R.id.listview_car);

		carros = (List<Carro>) getLastNonConfigurationInstance();

		// effect(); // efeito alpha

		Log.i(TAG, "Lendo estado: getLastNonConfigurationInstance()");

		if (icicle != null) {

			// Recuperamos a lista de carros salva pelo
			// onSaveInstanceState(bundle)
			ListCarros lista = (ListCarros) icicle
					.getSerializable(ListCarros.KEY);

			Log.i(TAG, "Lendo estado: savedInstanceState(carros)");

			this.carros = lista.carros;
		}

		if (carros != null) {

			// Atualiza o ListView diretamente
			listview_car.setAdapter(new ListCarAdapter(this, carros));

		} else {

			startTransaction(this);

		}

	}

	public Object onRetainNonConfigurationInstance() {

		Log.i(TAG, "Salvando Estado: onRetainNonConfigurationInstance()");

		return carros;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.i(TAG, "Salvando Estado: onSaveInstanceState(bundle)");

		// Salvar o estado da tela
		outState.putSerializable(ListCarros.KEY, new ListCarros(carros));
	}

	public void execute() throws Exception {
		this.carros = getCars();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		listview_car = (ListView) findViewById(R.id.listview_car);

		if (carros != null) {
			listview_car.setAdapter(new ListCarAdapter(this, carros));
		}

		confListForLongClick();
	}

	protected void onResume() {

		super.onResume();
	}

	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
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

		listview_car.setLayoutAnimation(controller);

	}

	public void update() {
		// Pega a lista de carros e exibe na tela
		carros = getCars();

		listview_car.setAdapter(new ListCarAdapter(this, carros));

		// effect(); // efeito alpha

		confListForLongClick();

	}

	private void confListForLongClick() {

		listview_car.setOnItemClickListener(this);

		listview_car
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> av, View v,
							int pos, long id) {

						ListCarScreen.this.onItemLongClick(av, v, pos, id);

						return true;
					}
				});

	}

	private List<Carro> getCars() {
		List<Carro> list = null;

		try {

			list = dao.listarCarros();

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

			LinearLayout item = (LinearLayout) view
					.findViewById(R.id.l_item_car);

			item.setVisibility(View.GONE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);
			tb_edicao.setLayoutAnimation(controller);
			tb_edicao.setVisibility(View.VISIBLE);

			// prepara animação (right to left)
			controller = AnimationUtils.loadLayoutAnimation(this,
					R.anim.anime_slide_to_left);

			item.setLayoutAnimation(controller);

			hideOtherItens(view);

		} else {

			LinearLayout item = (LinearLayout) view
					.findViewById(R.id.l_item_car);
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

						LinearLayout item = (LinearLayout) view
								.findViewById(R.id.l_item_car);
						item.setVisibility(View.VISIBLE);

						LinearLayout tb_edicao = (LinearLayout) view
								.findViewById(R.id.tb_edicao);

						tb_edicao.setVisibility(View.GONE);
					}
				});
			}
		}, 4500);

	}

	/**
	 * Esconde todos os outros itens
	 */
	private void hideOtherItens(View currentView) {
		for (int i = 0; i < listview_car.getCount(); i++) {
			View item = (View) listview_car.getChildAt(i);
			if (item != currentView) {
				// item.setVisibility(View.INVISIBLE);
				showBtsEditDelete(item, false);
			}
		}
	}

	/*
	 * Abre lista de itens
	 */
	private void openScreenListItemLog() {

		Log.i(TAG, "OPEN LIST CAR [" + id_car + "]");

		// abre lista de logs do carro
		final Intent it = new Intent(this, ListItemScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(Carro._ID, id_car);

		// Passa também o nome do carro para ser usado no titulo
		it.putExtra(Carro.NOME, name_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

		overridePendingTransition(R.anim.slide_to_left, R.anim.slide_to_left);

	}

	// Recupera o id do carro, e abre a tela de edição
	private void editCar() {
		// Cria a intent para abrir a tela de editar
		final Intent it = new Intent(this, FormCarScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(Carro._ID, id_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}

	public void editCar(View v) {
		editCar();
	}

	private void deleteConConfirm() {

		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage(R.string.a_dt_car);

		// Método executado se escolher Sim
		alerta.setPositiveButton(R.string.a_y,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						deleteCar();
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

	public void deleteConConfirm(View v) {
		deleteConConfirm();
	}

	// delete car
	public void deleteCar() {
		if (id_car != null) {

			excluirCarro(id_car);

			// deleta todos os itens vinculados a este carro
			ItemLogDAO daoItem = new ItemLogDAO(this);
			List<ItemLog> list = daoItem.listarItemLogs(id_car);

			if (list.size() > 0 && list != null) {

				Log.i(TAG, "Este carro possui itens vinculados a ele.");

				for (int i = 0; i < list.size(); i++) {
					daoItem.deletar(list.get(i).getId());
				}

			}

			// OK
			setResult(RESULT_OK);

			// atualiza a lista na tela
			update();
		}
	}

	// Excluir o carro
	protected void excluirCarro(final long id) {
		dao.deletar(id);
	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/
	public void onItemClick(final AdapterView<?> parent, View view,
			final int pos, final long id) {

		view.setTag(pos);

		// getSelectedItemOfList = (parent.getItemAtPosition(pos)).toString();
		// Log.i(TAG, "getSelectedItemOfList [" + getSelectedItemOfList + "]");

		// get the row the clicked button is in
		Carro carro = carros.get(pos);
		id_car = carro.getId();
		name_car = carro.getNome();

		// open list item log
		openScreenListItemLog();

	}

	public void onItemLongClick(AdapterView<?> parent, View view, int pos,
			long id) {

		Carro carro = carros.get(pos);
		id_car = carro.getId();

		showBtsEditDelete(view, true);

	}

	public void btBarLeft(final View v) {
		//
	}

	public void btBarRight(final View v) {

		startActivityForResult(new Intent(this, FormCarScreen.class),
				INSERIR_EDITAR);

	}

	public void openSettings(View v) {
		startActivity(new Intent(this, SettingsScreen.class));

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

	public void organizeBt() {

		// bt left
		// final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		// bt_left.setImageResource(R.drawable.bt_menu);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

	}

}
