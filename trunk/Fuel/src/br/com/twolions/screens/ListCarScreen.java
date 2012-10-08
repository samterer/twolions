package br.com.twolions.screens;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import br.com.twolions.R;
import br.com.twolions.adapters.ListCarAdapter;
import br.com.twolions.core.FuelActivity;
import br.com.twolions.dao.CarroDAO;
import br.com.twolions.dao.ItemLogDAO;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.daoobjects.ListCarros;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.util.Constants;

public class ListCarScreen extends FuelActivity implements OnItemClickListener,
		InterfaceBar, Transaction {

	private final String TAG = Constants.LOG_APP;

	protected static final int INSERIR_EDITAR = 1;

	public static CarroDAO dao;

	private List<Carro> carros;

	ListView listview_car;

	private static String name_car;
	private static Long id_car;

	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	private String getSelectedItemOfList;

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
		// listview_car.setSelector(R.drawable.bt_item_car);

		carros = (List<Carro>) getLastNonConfigurationInstance();

		effect(); // efeito alpha

		// carros = (List<Carro>) getLastNonConfigurationInstance();
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

		// setContentView(R.layout.list_car);

		listview_car = (ListView) findViewById(R.id.listview_car);

		if (carros != null) {
			listview_car.setAdapter(new ListCarAdapter(this, carros));
		}

		confListForLongClick();
	}

	//
	// protected void onPause() {
	// // TODO Auto-generated method stub
	// super.onPause();
	//
	// if (dialog != null) {
	// dialog.dismiss();
	// }
	// }
	// protected void onDestroy() {
	// super.onDestroy();
	//
	// // Fecha o banco
	// dao.fechar();
	//
	// finish();
	// }

	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
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
				.loadLayoutAnimation(this, R.anim.layout_controller);
		listview_car.setLayoutAnimation(controller);
	}

	public void update() {
		// Pega a lista de carros e exibe na tela
		carros = getCars();

		listview_car.setAdapter(new ListCarAdapter(this, carros));

		effect(); // efeito alpha

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

	View view_itens;

	public void showBtsEditDelete(final View view, boolean exibe) {

		view_itens = view;

		if (exibe) {
			// Toast.makeText(this, "exibindo...[" + id_car + "]",
			// Toast.LENGTH_SHORT).show();

			LinearLayout seta = (LinearLayout) view.findViewById(R.id.seta);
			seta.setVisibility(View.GONE);

			LinearLayout item = (LinearLayout) view
					.findViewById(R.id.l_item_car);
			item.setVisibility(View.GONE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);

			LayoutAnimationController controller = AnimationUtils
					.loadLayoutAnimation(this, R.anim.layout_controller);
			tb_edicao.setLayoutAnimation(controller);

			tb_edicao.setVisibility(View.VISIBLE);

		} else {
			// Toast.makeText(this, "apagando...[" + id_car +
			// "]",Toast.LENGTH_SHORT).show();

			ImageView seta = (ImageView) view.findViewById(R.id.seta);
			seta.setVisibility(View.VISIBLE);

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
						LinearLayout seta = (LinearLayout) view
								.findViewById(R.id.seta);
						seta.setVisibility(View.VISIBLE);

						LinearLayout item = (LinearLayout) view
								.findViewById(R.id.l_item_car);
						item.setVisibility(View.VISIBLE);

						LinearLayout tb_edicao = (LinearLayout) view
								.findViewById(R.id.tb_edicao);
						tb_edicao.setVisibility(View.GONE);
					}
				});
			}
		}, 3000);

	}

	/*
	 * Abre lista de itens
	 */
	private void openScreenListItemLog() {

		Log.i(TAG, "OPEN LIST CAR [" + id_car + "]");

		// abre lista de logs do carro
		final Intent it = new Intent(this, ListLogScreen.class);
		// Passa o id do carro como par�metro
		it.putExtra(Carro._ID, id_car);
		// Abre a tela de edi��o
		startActivityForResult(it, INSERIR_EDITAR);

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

	// Recupera o id do carro, e abre a tela de edi��o
	private void editCar() {
		// Cria a intent para abrir a tela de editar
		final Intent it = new Intent(this, FormCarScreen.class);

		// Passa o id do carro como par�metro
		it.putExtra(Carro._ID, id_car);

		// Abre a tela de edi��o
		startActivityForResult(it, INSERIR_EDITAR);
	}

	public void editCar(View v) {
		editCar();
	}

	private void deleteConConfirm() {

		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage("Are you sure you want to delete this car?");

		// M�todo executado se escolher Sim
		alerta.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteCar();
			}
		});

		// M�todo executado se escolher N�o
		alerta.setNegativeButton("Not", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//
			}
		});
		// Exibe o alerta de confirma��o
		alerta.show();
	}

	public void deleteConConfirm(View v) {
		deleteConConfirm();
	}

	// delete car
	public void deleteCar() {
		if (id_car != null) {
			excluirCarro(id_car);

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
	// String oldPosition = null;
	public void onItemClick(final AdapterView<?> parent, View view,
			final int pos, final long id) {

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
		// Fecha a tela
		finish();

	}

	public void btBarRight(final View v) {

		startActivityForResult(new Intent(this, FormCarScreen.class),
				INSERIR_EDITAR);

	}

	public void organizeBt() {
		// bt left
		final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_menu);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

		// final TextView title = (TextView) findViewById(R.id.title);
		// title.setText("VEHICLE");

	}

}