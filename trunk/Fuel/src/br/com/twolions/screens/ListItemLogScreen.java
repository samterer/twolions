package br.com.twolions.screens;

import java.util.List;

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
import br.com.twolions.FuelActivity;
import br.com.twolions.R;
import br.com.twolions.Transaction;
import br.com.twolions.dao.ItemLogDAO;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.daoobjects.ListItemLog;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.object.ListItemAdapter;
import br.com.twolions.util.Constants;

public class ListItemLogScreen extends FuelActivity
		implements
			OnItemClickListener,
			InterfaceBar,
			Transaction {
	protected static final int INSERIR_EDITAR = 1;

	private String TAG = Constants.LOG_APP;

	public static ItemLogDAO repositorio;

	private List<ItemLog> itens;

	ListView listview_log;

	private Long id_item;
	private Long id_car;
	private int type;

	// menu
	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		repositorio = new ItemLogDAO(this);

		// id do carro da vez
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id_car = extras.getLong(Carro._ID);
			if (id_car != null) {
				montaTela(icicle);
			}
		}

		if (itens != null) {
			// Atualiza o ListView diretamente
			listview_log.setAdapter(new ListItemAdapter(this, itens));
		} else {
			startTransaction(this);
		}

	}

	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {
		setContentView(R.layout.list_log);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setOnItemClickListener(this);
		itens = (List<ItemLog>) getLastNonConfigurationInstance();

		Log.i(TAG, "Lendo estado: getLastNonConfigurationInstance()");
		if (itens == null && icicle != null) {
			// Recuperamos a lista de carros salva pelo
			// onSaveInstanceState(bundle)
			ListItemLog lista = (ListItemLog) icicle
					.getSerializable(ListItemLog.KEY);
			Log.i(TAG, "Lendo estado: savedInstanceState(carros)");
			this.itens = lista.itens;
		}

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.i(TAG, "Salvando Estado: onRetainNonConfigurationInstance()");
		return itens;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "Salvando Estado: onSaveInstanceState(bundle)");
		// Salvar o estado da tela
		outState.putSerializable(ListItemLog.KEY, new ListItemLog(itens));
	}

	public void execute() throws Exception {
		// Busca os carros em uma thread
		itens = getItens();
	}
	public void atualizarView() {
		// Atualiza os carros na thread principal
		if (itens != null) {
			listview_log.setAdapter(new ListItemAdapter(this, itens));
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		setContentView(R.layout.list_log);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setOnItemClickListener(this);
		if (itens != null) {
			listview_log.setAdapter(new ListItemAdapter(this, itens));
		}
	}

	public void updateList() {
		// Pega a lista de carros e exibe na tela
		itens = getItens();

		setContentView(R.layout.transaction);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setAdapter(new ListItemAdapter(this, itens));
		listview_log.setOnItemClickListener(this);

		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.layout_controller);
		listview_log.setLayoutAnimation(controller);

		// organize bts
		organizeBt();

	}

	/*
	 * protected void atualizarLista() { // Pega a lista de carros e exibe na
	 * tela if (id_car != null) { itens = getItens(); }
	 * 
	 * setContentView(R.layout.list_log);
	 * 
	 * listview_log = (ListView) findViewById(R.id.listview_log);
	 * listview_log.setAdapter(new ListItemAdapter(this, itens));
	 * listview_log.setOnItemClickListener(this);
	 * 
	 * listview_log.setTextFilterEnabled(true);
	 * 
	 * LayoutAnimationController controller = AnimationUtils
	 * .loadLayoutAnimation(this, R.anim.layout_controller);
	 * listview_log.setLayoutAnimation(controller);
	 * 
	 * // organize bts organizeBt();
	 * 
	 * }
	 */

	private List<ItemLog> getItens() {
		Log.i(TAG, "Atualizando lista de itens...");
		Log.i(TAG, "getItens() id[" + id_car + "]");

		List<ItemLog> list = repositorio.listarItemLogs(id_car);

		// for (int i = 0; i < list.size(); i++) {
		// ItemLog item = list.get(i);
		// Log.i(TAG, "Item type[" + item.getType() + "]");
		// Log.i(TAG, "Item id[" + item.getId() + "]");
		// }

		return list;
	}

	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		final ItemLog i = itens.get(posicao);
		id_item = i.getId();
		type = i.getType();

		// Log.i(TAG, "Open edit... id [" + id_item + "]");
		// Log.i(TAG, i.toString());

		registerForContextMenu(view);
		view.setLongClickable(true); // undo setting of this flag in
										// registerForContextMenu
		this.openContextMenu(view);

	}
	// sub menu
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// menu.setHeaderTitle(name_car);
		menu.add(0, EDITAR, 0, "Edit");
		menu.add(0, DELETE, 0, "Delete");
		menu.add(0, v.getId(), 0, "Cancel");
	}

	// click in item of sub menu
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case EDITAR :
				editItem();
				break;
			case DELETE :
				deleteItemLog();
				break;
			default :
				return false;
		}
		return true;
	}

	// Recupera o id do carro, e abre a tela de edi��o
	protected void editItem() {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// id do item
		it.putExtra(ItemLog._ID, id_item);
		// passa o tipo do item
		it.putExtra(ItemLog.TYPE, type);

		// Abre a tela de edi��o
		startActivityForResult(it, INSERIR_EDITAR);
	}
	// delete car
	public void deleteItemLog() {
		if (id_item != null) {
			excluirItem(id_item);
		}

		// OK
		setResult(RESULT_OK);

		// atualiza a lista na tela
		// atualizarLista();
		updateList();
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

	public void btBarLeft(View v) {
		setResult(RESULT_CANCELED);
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
		bt_left.setImageResource(R.drawable.bt_cancel_long);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

		// ImageView title = (ImageView) findViewById(R.id.title);
		// title.setImageResource(R.drawable.t_select_vehicle);

	}
	public void onBackPressed() { // call my backbutton pressed method when
									// boolean==true

		Log.i(TAG, "Clicked");

	}

}
