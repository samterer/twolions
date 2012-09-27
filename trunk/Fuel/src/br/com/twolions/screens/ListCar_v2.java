package br.com.twolions.screens;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.twolions.R;
import br.com.twolions.adapters.CarListAdapter;
import br.com.twolions.dao.CarroDAO;
import br.com.twolions.dao.ItemLogDAO;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.daoobjects.ListCarros;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.util.Constants;

public class ListCar_v2 extends ListActivity
		implements
			OnItemClickListener,
			InterfaceBar,
			Transaction {

	private final String TAG = Constants.LOG_APP;

	protected static ProgressDialog dialog;

	protected static final int INSERIR_EDITAR = 1;

	public static CarroDAO dao;

	private List<Carro> carros;

	ListView listview_car;

	private String name_car;
	private Long id_car;

	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// if (dao == null) {
		// Log.i(TAG, "dao null, opening");
		// dao = new SqlScript(this);
		// }

		dao = new CarroDAO(this);

		montaTela(icicle);

		organizeBt();

	}
	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {
		setContentView(R.layout.list_car);

		listview_car = (ListView) findViewById(R.id.listview_car);

		carros = (List<Carro>) getLastNonConfigurationInstance();

		carros = (List<Carro>) getLastNonConfigurationInstance();

		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.layout_controller);

		listview_car.setLayoutAnimation(controller);

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
			listview_car.setAdapter(new CarListAdapter(this, carros));

			listview_car.setOnItemClickListener(this);

		} else {
			// FuelActivity fa = new FuelActivity();
			// fa.startTransaction(this);
			// startTransaction(this);
			try {
				execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// habilita o click nos items da list
		// onClickInList();

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

		setContentView(R.layout.list_car);

		listview_car = (ListView) findViewById(R.id.listview_car);
		listview_car.setOnItemClickListener(this);
		if (carros != null) {
			listview_car.setAdapter(new CarListAdapter(this, carros));
		}
	}

	public void updateList() {
		// Pega a lista de carros e exibe na tela
		carros = getCars();

		listview_car.setAdapter(new CarListAdapter(this, carros));

	}
	private List<Carro> getCars() {
		// Log.i(TAG, "Atualizando lista de itens...");
		// Log.i(TAG, "getItens() id[" + id_car + "]");

		List<Carro> list = dao.listarCarros();

		return list;
	}

	public void onItemClick(final AdapterView<?> parent, final View view,
			final int posicao, final long id) {

		Log.i(TAG, "Click! Click! Click! Click! Click!Click!Click!Click!");

		// get the row the clicked button is in
		final Carro c = carros.get(posicao);
		name_car = c.getNome();
		id_car = c.getId();

		// long click
		registerForContextMenu(view);
		view.setClickable(false);

		Log.i(TAG, "click");

		// open list item log
		openScreenListItemLog(c);
	}

	/*
	 * Abre lista de itens
	 */
	private void openScreenListItemLog(Carro c) {

		if (verificaItensPorCarro()) {

			dialog = ProgressDialog.show(this, "Pesquisando itens",
					"Loading...", true);

			Log.i(TAG, "OPEN LIST CAR [" + id_car + "]");

			// abre lista de logs do carro
			final Intent it = new Intent(this, ListLogScreen.class);

			// Passa o id do carro como parâmetro
			it.putExtra(Carro._ID, id_car);

			// Abre a tela de edição
			startActivityForResult(it, INSERIR_EDITAR);
		}
	}

	/*
	 * Verifica se o carro selecionado possui itens
	 */
	private boolean verificaItensPorCarro() {
		boolean result = false;

		ItemLogDAO itemDAO = new ItemLogDAO(this);
		List<ItemLog> list = itemDAO.listarItemLogs(id_car);

		if (list.size() > 0) {
			result = true;
		}

		return result;
	}

	// sub menu
	public void onCreateContextMenu(final ContextMenu menu, final View v,
			final ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(name_car);
		menu.add(0, EDITAR, 0, "Edit");
		menu.add(0, DELETE, 0, "Delete");
		menu.add(0, v.getId(), 0, "Cancel");
	}
	// click in item of sub menu
	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case EDITAR :
				editCar();
				break;
			case DELETE :
				// deleteCar();
				deleteConConfirm();
				break;
			default :
				return false;
		}
		return true;
	}

	// Recupera o id do carro, e abre a tela de edição
	protected void editCar() {
		// Cria a intent para abrir a tela de editar
		final Intent it = new Intent(this, FormCarScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(Carro._ID, id_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}

	public void deleteConConfirm() {

		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage("Are you sure you want to delete this car?");
		// Método executado se escolher Sim
		alerta.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteCar();
			}
		});
		// Método executado se escolher Não
		alerta.setNegativeButton("Not", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//
			}
		});
		// Exibe o alerta de confirmação
		alerta.show();
	}

	// delete car
	public void deleteCar() {
		if (id_car != null) {
			excluirCarro(id_car);

			// OK
			setResult(RESULT_OK);

			// atualiza a lista na tela
			updateList();
		}
	}
	// Excluir o carro
	protected void excluirCarro(final long id) {
		dao.deletar(id);
	}

	@Override
	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarCarro retornar, seja se foi para adicionar
		// vamos atualizar a lista
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			updateList();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (dialog != null) {
			dialog.dismiss();
		}
	}
	protected void onDestroy() {
		super.onDestroy();

		// Fecha o banco
		dao.fechar();

		finish();
	}

	public void btBarLeft(final View v) {
		// Fecha a tela
		finish();

	}

	public void btBarRight(final View v) {

		Log.i(TAG, "FILHO DA PUUUUUUUUUUUUUUUUUUUUUUUTA!");

		startActivityForResult(new Intent(this, FormCarScreen.class),
				INSERIR_EDITAR);

	}

	public void organizeBt() {
		// bt left
		final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_cancel_long);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

		final ImageView title = (ImageView) findViewById(R.id.title);
		title.setImageResource(R.drawable.t_select_vehicle);

	}
	public ListView getListview_car() {
		return listview_car;
	}
	public void setListview_car(ListView listview_car) {
		this.listview_car = listview_car;
	}

}
