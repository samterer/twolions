package br.com.twolions.screens;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.adapters.CarListAdapter;
import br.com.twolions.core.FuelActivity;
import br.com.twolions.dao.CarroDAO;
import br.com.twolions.dao.ItemLogDAO;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.daoobjects.ListCarros;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.util.Constants;

public class ListCarScreen extends FuelActivity
		implements
			OnItemClickListener,
			InterfaceBar,
			Transaction {

	private final String TAG = Constants.LOG_APP;

	protected static final int INSERIR_EDITAR = 1;

	public static CarroDAO dao;

	private List<Carro> carros;

	ListView listview_car;

	// private String name_car;
	// private Long id_car;

	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	String getSelectedItemOfList;

	Carro carro = null;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		dao = new CarroDAO(this);

		montaTela(icicle);

		organizeBt();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {
		setContentView(R.layout.list_car);

		listview_car = (ListView) findViewById(R.id.listview_car);

		carros = (List<Carro>) getLastNonConfigurationInstance();

		carros = (List<Carro>) getLastNonConfigurationInstance();

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
		listview_car.setOnItemClickListener(this);

	}

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

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	private List<Carro> getCars() {
		List<Carro> list = null;

		try {
			list = dao.listarCarros();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void showBtsEditDelete(View view, boolean exibe) {

		if (exibe) {
			Toast.makeText(this, "exibindo...", Toast.LENGTH_SHORT).show();

			ImageView seta = (ImageView) view.findViewById(R.id.seta);
			seta.setVisibility(View.GONE);

			LinearLayout item = (LinearLayout) view.findViewById(R.id.item);
			item.setVisibility(View.GONE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);
			tb_edicao.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(this, "apagando...", Toast.LENGTH_SHORT).show();

			ImageView seta = (ImageView) view.findViewById(R.id.seta);
			seta.setVisibility(View.VISIBLE);

			LinearLayout item = (LinearLayout) view.findViewById(R.id.item);
			item.setVisibility(View.VISIBLE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);
			tb_edicao.setVisibility(View.GONE);
		}

	}

	/*
	 * Abre lista de itens
	 */
	private void openScreenListItemLog() {

		Log.i(TAG, "OPEN LIST CAR [" + carro.getId() + "]");

		// abre lista de logs do carro
		final Intent it = new Intent(this, ListLogScreen.class);
		// Passa o id do carro como parâmetro
		it.putExtra(Carro._ID, carro.getId());
		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

	}

	/*
	 * Verifica se o carro selecionado possui itens
	 */
	private boolean verificaItensPorCarro() {
		boolean result = false;

		ItemLogDAO itemDAO = new ItemLogDAO(this);
		List<ItemLog> list = itemDAO.listarItemLogs(carro.getId());

		if (list.size() > 0) {
			result = true;
		}

		return result;
	}

	// Recupera o id do carro, e abre a tela de edição
	private void editCar() {
		// Cria a intent para abrir a tela de editar
		final Intent it = new Intent(this, FormCarScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(Carro._ID, carro.getId());

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}

	public void editCar(View v) {
		editCar();
	}

	private void deleteConConfirm() {

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

	public void deleteConConfirm(View v) {
		deleteConConfirm();
	}

	// delete car
	public void deleteCar() {
		if (carro != null) {
			excluirCarro(carro.getId());

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

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/
	String oldPosition = null;
	public void onItemClick(final AdapterView<?> parent, View view,
			final int pos, final long id) {

		getSelectedItemOfList = (parent.getItemAtPosition(pos)).toString();

		Log.i(TAG, "getSelectedItemOfList [" + getSelectedItemOfList + "]");

		// get the row the clicked button is in
		carro = carros.get(pos);

		// long click
		// registerForContextMenu(view);

		// exibe edit
		listview_car.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {

				boolean exibe = false;

				if (oldPosition != null) {
					// desaparece com edit
					v = listview_car.getAdapter()
							.getView(Integer.valueOf(oldPosition).intValue(),
									null, null);

					exibe = false;
					// showBtsEditDelete(v, false);

					oldPosition = null;
				} else {
					// exibe edit

					exibe = true;
					// showBtsEditDelete(v, true);

					oldPosition = "" + pos;
				}

				showBtsEditDelete(v, exibe);

				return true;
			}

		});

		// open list item log
		openScreenListItemLog();

		// view.setOnLongClickListener(new myLongListener());

		// open list item log
		// openScreenListItemLog(carro);
		// Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
	}

	// sub menu
	// public void onCreateContextMenu(final ContextMenu menu, final View v,
	// final ContextMenuInfo menuInfo) {
	// super.onCreateContextMenu(menu, v, menuInfo);
	// Toast.makeText(this, "looong click!", Toast.LENGTH_SHORT).show();
	//
	// showBtsEditDelete(v);
	// // menu.setHeaderTitle(name_car);
	// // menu.add(0, EDITAR, 0, "Edit");
	// // menu.add(0, DELETE, 0, "Delete");
	// // menu.add(0, v.getId(), 0, "Cancel");
	// }

	// click in item of sub menu
	// public boolean onContextItemSelected(final MenuItem item) {
	// switch (item.getItemId()) {
	// case EDITAR :
	// editCar();
	// break;
	// case DELETE : // deleteCar(); deleteConConfirm(); break; default :
	// return false;
	// }
	// return true;
	// }

	public void btBarLeft(final View v) {
		// Fecha a tela
		finish();

	}
	public void btBarRight(final View v) {

		startActivityForResult(new Intent(this, FormCarScreen.class),
				INSERIR_EDITAR);

	}

}
