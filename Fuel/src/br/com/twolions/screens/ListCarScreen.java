package br.com.twolions.screens;

import java.util.List;

import android.app.ProgressDialog;
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
import br.com.twolions.adapters.CarListAdapter;
import br.com.twolions.core.ListCarActivity;
import br.com.twolions.dao.CarroDAO;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.Carro.Carros;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.sql.SqlScript;

public class ListCarScreen extends ListCarActivity
		implements
			OnItemClickListener,
			InterfaceBar {

	private final String CATEGORIA = "appLog";

	protected static final int INSERIR_EDITAR = 1;

	public static CarroDAO repositorio;

	private List<Carro> carros;

	ListView listView;

	private String name_car;
	private Long id_car;

	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	@Override
	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		if (repositorio == null) {
			repositorio = new SqlScript(this);
		}

		atualizarLista();

	}

	private void init() {

	}

	protected void atualizarLista() {
		// Pega a lista de carros e exibe na tela
		carros = repositorio.listarCarros();

		setContentView(R.layout.transaction);

		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new CarListAdapter(this, carros));
		listView.setOnItemClickListener(this);

		// organize bts
		organizeBt();

	}

	public void onItemClick(final AdapterView<?> parent, final View view,
			final int posicao, final long id) {
		// get the row the clicked button is in
		final Carro c = carros.get(posicao);
		name_car = c.getNome();
		id_car = c.getId();

		// long click
		registerForContextMenu(view);

		Log.i(CATEGORIA, "click");

		// open list item log
		openScreenListItemLog();
	}

	/*
	 * Abre lista de itens
	 */
	private void openScreenListItemLog() {

		Log.i(CATEGORIA, "OPEN LIST CAR [" + id_car + "]");

		// ImageView v = (ImageView) findViewById(R.id.view);
		// v.setVisibility(View.VISIBLE);
		// v.setAlpha(0);

		dialog = ProgressDialog.show(this, "Pesquisando itens", "Loading...",
				true);

		// abre lista de logs do carro
		final Intent it = new Intent(this, ListLogScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(Carros._ID, id_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

		// go next screen
		/*
		 * flipper = (ViewFlipper) findViewById(R.id.flipper);
		 * 
		 * flipper.setInAnimation(inFromRightAnimation());
		 * flipper.setOutAnimation(outToLeftAnimation()); flipper.showNext();
		 */

		// ImageView img = (ImageView) findViewById(R.id.tipo);
		// img.setVisibility(View.INVISIBLE);

		// ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
		// progress.setVisibility(View.VISIBLE);
	}
	// sub menu
	@Override
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
				deleteCar();
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
		it.putExtra(Carros._ID, id_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}
	// delete car
	public void deleteCar() {
		if (id_car != null) {
			excluirCarro(id_car);
		}

		// OK
		setResult(RESULT_OK);

		// atualiza a lista na tela
		atualizarLista();
	}
	// Excluir o carro
	protected void excluirCarro(final long id) {
		repositorio.deletar(id);
	}

	@Override
	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarCarro retornar, seja se foi para adicionar
		// vamos atualizar a lista
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			atualizarLista();
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
		repositorio.fechar();
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
		bt_left.setImageResource(R.drawable.bt_cancel_long);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

		final ImageView title = (ImageView) findViewById(R.id.title);
		title.setImageResource(R.drawable.t_select_vehicle);

	}
}
