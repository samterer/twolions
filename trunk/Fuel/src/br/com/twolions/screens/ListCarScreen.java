package br.com.twolions.screens;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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
	protected static final int INSERIR_EDITAR = 1;

	public static CarroDAO repositorio;

	private List<Carro> carros;

	ListView listView;

	private String name_car;
	private Long id_car;

	private static final int EDITAR = 0;
	private static final int DELETE = 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		SqlScript sql = new SqlScript(this);

		repositorio = new CarroDAO(this);

		atualizarLista();

	}

	private void init() {

	}

	protected void atualizarLista() {
		// Pega a lista de carros e exibe na tela
		carros = repositorio.listarCarros();

		// Adaptador de lista customizado para cada linha de um carro
		// setListAdapter(new CarListAdapter(this, carros));

		setContentView(R.layout.list_car);

		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new CarListAdapter(this, carros));
		listView.setOnItemClickListener(this);

		// organize bts
		organizeBt();

	}

	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		// get the row the clicked button is in
		Carro c = carros.get(posicao);
		name_car = c.nome;
		id_car = c.id;

		registerForContextMenu(view);

	}
	// sub menu
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(name_car);
		menu.add(0, EDITAR, 0, "Edit");
		menu.add(0, DELETE, 0, "Delete");
		menu.add(0, v.getId(), 0, "Cancel");
	}
	// click in item of sub menu
	public boolean onContextItemSelected(MenuItem item) {
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
		// Usuário clicou em algum carro da lista
		// Recupera o carro selecionado
		// Carro carro = carros.get(posicao);

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormCarScreen.class);

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
	protected void excluirCarro(long id) {
		ListCarScreen.repositorio.deletar(id);
	}

	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarCarro retornar, seja se foi para adicionar
		// vamos atualizar a lista
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			atualizarLista();
		}
	}

	protected void onDestroy() {
		super.onDestroy();

		// Fecha o banco
		repositorio.fechar();
	}

	public void btBarLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarRight(View v) {

		startActivityForResult(new Intent(this, FormCarScreen.class),
				INSERIR_EDITAR);

	}

	public void organizeBt() {
		// bt left
		ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_help);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_add);

		ImageView title = (ImageView) findViewById(R.id.title);
		title.setImageResource(R.drawable.t_select_vehicle);

	}
}
