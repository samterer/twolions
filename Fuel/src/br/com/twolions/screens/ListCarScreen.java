package br.com.twolions.screens;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.twolions.R;
import br.com.twolions.base.RepositorioCarro;
import br.com.twolions.base.RepositorioCarroScript;
import br.com.twolions.core.ListCarActivity;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.object.CarListAdapter;
import br.com.twolions.object.Carro;
import br.com.twolions.object.Carro.Carros;

public class ListCarScreen extends ListCarActivity
		implements
			OnItemClickListener,
			InterfaceBar {
	protected static final int INSERIR_EDITAR = 1;

	public static RepositorioCarro repositorio;

	private List<Carro> carros;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		repositorio = new RepositorioCarroScript(this);

		atualizarLista();
	}

	protected void atualizarLista() {
		// Pega a lista de carros e exibe na tela
		carros = repositorio.listarCarros();

		// Adaptador de lista customizado para cada linha de um carro
		// setListAdapter(new CarListAdapter(this, carros));

		setContentView(R.layout.list_car);

		ListView listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new CarListAdapter(this, carros));
		listView.setOnItemClickListener(this);

		// organize bts
		organizeBt();
	}

	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		editarCarro(posicao);
	}

	// Recupera o id do carro, e abre a tela de edição
	protected void editarCarro(int posicao) {
		// Usuário clicou em algum carro da lista
		// Recupera o carro selecionado
		Carro carro = carros.get(posicao);

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormCarScreen.class);

		// Passa o id do carro como parâmetro
		it.putExtra(Carros._ID, carro.id);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
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
