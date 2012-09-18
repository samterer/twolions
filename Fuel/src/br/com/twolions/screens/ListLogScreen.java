package br.com.twolions.screens;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.twolions.R;
import br.com.twolions.core.ListLogActivity;
import br.com.twolions.dao.ItemLogDAO;
import br.com.twolions.daoobjects.Carro.Carros;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.object.ItemListAdapter;

public class ListLogScreen extends ListLogActivity
		implements
			OnItemClickListener,
			InterfaceBar {
	protected static final int INSERIR_EDITAR = 1;

	private String CATEGORIA = "appLog";

	public static ItemLogDAO repositorio;

	private List<ItemLog> itens;

	ListView listView;

	private Long id_item;
	private Long id_car;

	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		if (repositorio == null) {
			repositorio = new ItemLogDAO(this);
		}

		// id do carro da vez
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id_car = extras.getLong(Carros._ID);

			if (id_car != null) {
				Log.i(CATEGORIA, "Atualiza lista de cars");
				atualizarLista();
			}
		}

	}
	private void init() {

	}

	protected void atualizarLista() {
		// Pega a lista de carros e exibe na tela
		itens = getItens();

		setContentView(R.layout.list_log);

		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new ItemListAdapter(this, itens));
		listView.setOnItemClickListener(this);

		// organize bts
		organizeBt();

	}

	private List<ItemLog> getItens() {
		Log.i(CATEGORIA, "getItens() id[" + id_car + "]");

		List<ItemLog> list = repositorio.listarItemLogs(id_car);

		for (int i = 0; i < list.size(); i++) {
			ItemLog item = list.get(i);

			Log.i(CATEGORIA, "Item type[" + item.getType() + "]");
		}

		return list;
	}
	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		final ItemLog i = itens.get(posicao);
		id_item = i.getId();

		registerForContextMenu(view);

	}
	// sub menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// menu.setHeaderTitle(name_car);
		menu.add(0, EDITAR, 0, "Edit");
		menu.add(0, DELETE, 0, "Delete");
		menu.add(0, v.getId(), 0, "Cancel");
	}
	// click in item of sub menu
	@Override
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

	// Recupera o id do carro, e abre a tela de edição
	protected void editItem() {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(ItemLog._ID, id_item);
		// id do item
		it.putExtra(ItemLog.ID_CAR, id_car);

		// Abre a tela de edição
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
		atualizarLista();
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
			atualizarLista();
		}
	}

	public void btBarLeft(View v) {
		setResult(RESULT_CANCELED);
		// Fecha a tela
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

		Log.i(CATEGORIA, "Clicked");

	}

}
