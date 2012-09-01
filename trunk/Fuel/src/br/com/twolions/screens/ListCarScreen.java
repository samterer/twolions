package br.com.twolions.screens;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
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

	private TableLayout tb;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		repositorio = new RepositorioCarroScript(this);

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

		ListView listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(new CarListAdapter(this, carros));
		listView.setOnItemClickListener(this);

		// organize bts
		organizeBt();
	}

	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		// editarCarro(posicao);
		// get the row the clicked button is in
		TableLayout tb = (TableLayout) findViewById((int) id);// parent.getChildAt(posicao);

		Log.i("appLog", "tb: " + tb);
		if (tb.getVisibility() == View.VISIBLE) {
			tb.setVisibility(View.GONE);
		} else {
			tb.setVisibility(View.VISIBLE);
		}

	}

	/*
	 * public void myClickHandler(View v) {
	 * 
	 * // get the row the clicked button is in FrameLayout frameLayout =
	 * (FrameLayout) v.getParent();
	 * 
	 * TableLayout tb = (TableLayout) frameLayout.getChildAt(0);
	 * 
	 * if (tb.getVisibility() == View.VISIBLE) { tb.setVisibility(View.GONE); }
	 * else { tb.setVisibility(View.VISIBLE); }
	 * frameLayout.refreshDrawableState(); }
	 */

	// Recupera o id do carro, e abre a tela de edi��o
	protected void editarCarro(int posicao) {
		// Usu�rio clicou em algum carro da lista
		// Recupera o carro selecionado
		Carro carro = carros.get(posicao);

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormCarScreen.class);

		// Passa o id do carro como par�metro
		it.putExtra(Carros._ID, carro.id);

		// Abre a tela de edi��o
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
