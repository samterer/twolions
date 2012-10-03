package br.com.twolions.screens;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
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

public class ListLogScreen extends FuelActivity
		implements
			OnItemClickListener,
			InterfaceBar,
			Transaction {
	protected static final int INSERIR_EDITAR = 1;

	private String TAG = Constants.LOG_APP;

	public static ItemLogDAO repositorio;

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

		repositorio = new ItemLogDAO(this);

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

		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.layout_controller);
		listview_log.setLayoutAnimation(controller);

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

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// abre lista de logs do carro
			final Intent it = new Intent(this, ViewGraphicScreen.class);
			// Abre a tela de edição
			startActivity(it);
		}

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	public void updateList() {
		// Pega a lista de carros e exibe na tela
		itens = getItens();

		listview_log.setAdapter(new ListItemAdapter(this, itens));
		listview_log.setOnItemClickListener(this);
		listview_log
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> av, View v,
							int pos, long id) {

						showBtsEditDelete(v, true);

						return true;
					}
				});
	}

	private List<ItemLog> getItens() {
		List<ItemLog> list = null;

		try {
			list = repositorio.listarItemLogs(id_car);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void showBtsEditDelete(View view, boolean exibe) {
		if (exibe) {
			Toast.makeText(this, "exibindo...", Toast.LENGTH_SHORT).show();

			RelativeLayout item = (RelativeLayout) view
					.findViewById(R.id.item_log);
			item.setVisibility(View.GONE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);
			tb_edicao.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(this, "apagando...", Toast.LENGTH_SHORT).show();

			LinearLayout item = (LinearLayout) view.findViewById(R.id.item_log);
			item.setVisibility(View.VISIBLE);

			LinearLayout tb_edicao = (LinearLayout) view
					.findViewById(R.id.tb_edicao);
			tb_edicao.setVisibility(View.GONE);
		}
	}

	public void deleteConConfirm() {
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage("Are you sure you want to delete this item?");
		// Método executado se escolher Sim
		alerta.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				deleteItem();
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

	// Recupera o id do carro, e abre a tela de edição
	protected void editItem() {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// id do item
		it.putExtra(ItemLog._ID, id_item);
		// passa o tipo do item
		it.putExtra(ItemLog.TYPE, type);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}

	// delete car
	public void deleteItem() {
		if (id_item != null) {
			excluirItem(id_item);

			// OK
			setResult(RESULT_OK);

			// atualiza a lista na tela
			updateList();
		}

	}

	public void deleteConConfirm(View v) {
		deleteConConfirm();
	}

	// Excluir o carro
	protected void excluirItem(long id) {
		repositorio.deletar(id);
	}

	@Override
	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarCarro retornar, seja se foi para adicionar
		// vamos atualizar a lista
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			// atualizarLista();
			updateList();
		}
	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/
	private String oldPosition = null;
	private String getSelectedItemOfList;
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

		getSelectedItemOfList = (parent.getItemAtPosition(pos)).toString();

		Log.i(TAG, "getSelectedItemOfList [" + getSelectedItemOfList + "]");

		// get the row the clicked button is in
		id_car = itens.get(pos).getId_car();
		id_item = itens.get(pos).getId();

		// open list item log
		openViewItem();

	}

	/*
	 * Abre lista de itens
	 */
	private void openViewItem() {

		Log.i(TAG, "OPEN LIST CAR [" + id_car + "]");

		// abre lista de logs do carro
		final Intent it = new Intent(this, ViewItemScreen.class);
		// Abre a tela de edição
		startActivity(it);

	}

	public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos,
			long id) {

		Log.i(TAG, "onItemLongClick...");

		boolean exibe = false;

		if (oldPosition != null) { // desaparece com edit

			view = listview_log.getAdapter().getView(
					Integer.valueOf(oldPosition).intValue(), null, null);

			exibe = false;
			// showBtsEditDelete(v, false);

			oldPosition = null;
		} else { // exibe edit

			exibe = true;
			// showBtsEditDelete(v, true);

			oldPosition = "" + pos;
		}

		showBtsEditDelete(view, exibe);

		return false;
	}

	public void btBarLeft(View v) {

		// startActivityForResult(new Intent(this,
		// FormCarScreen.class),INSERIR_EDITAR);

		// go next screen
		finish();

	}
	public void btBarRight(View v) {

		startActivityForResult(new Intent(this, FormCarScreen.class),
				INSERIR_EDITAR);

	}

	public void organizeBt() {
		// bt left
		ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_cancel);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

		// ImageView title = (ImageView) findViewById(R.id.title);
		// title.setImageResource(R.drawable.t_select_vehicle);

	}
	public void onBackPressed() { // call my backbutton pressed method when
									// boolean==true

		Log.i(TAG, "Clicked back");

	}

}
