package br.com.twolions.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import br.com.core.ActivityCircle;
import br.com.twolions.R;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.object.Carro;
import br.com.twolions.object.Carro.Carros;

/**
 * Activity que utiliza o TableLayout para editar o carro
 * 
 * @author rlecheta
 * 
 */
public class FormCarScreen extends ActivityCircle implements InterfaceBar {
	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;

	// Campos texto
	private EditText campoNome;
	private EditText campoPlaca;
	private Long id;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.form_car);

		// organize bt
		organizeBt();

		init();

		actionBt(this);
	}

	private void init() {
		campoNome = (EditText) findViewById(R.id.campoNome);
		campoPlaca = (EditText) findViewById(R.id.campoPlaca);

		id = null;

		Bundle extras = getIntent().getExtras();
		// Se for para Editar, recuperar os valores ...
		if (extras != null) {
			id = extras.getLong(Carros._ID);

			if (id != null) {
				// é uma edição, busca o carro...
				Carro c = buscarCarro(id);
				campoNome.setText(c.nome);
				campoPlaca.setText(c.placa);
			}
		}
	}

	public void actionBt(Context context) {
		ImageButton btCancelar = (ImageButton) findViewById(R.id.btCancelar);
		btCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				// Fecha a tela
				finish();
			}
		});

		// Listener para salvar o carro
		ImageButton btSalvar = (ImageButton) findViewById(R.id.btSalvar);
		btSalvar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				salvar();
			}
		});

		ImageButton btExcluir = (ImageButton) findViewById(R.id.btExcluir);

		if (id == null) {
			// Se id está nulo, não pode excluir
			btExcluir.setVisibility(View.INVISIBLE);
		} else {
			// Listener para excluir o carro
			btExcluir.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					excluir();
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Cancela para não ficar nada na tela pendente
		setResult(RESULT_CANCELED);

		// Fecha a tela
		finish();
	}

	public void salvar() {

		Carro carro = new Carro();
		if (id != null) {
			// É uma atualização
			carro.id = id;
		}
		carro.nome = campoNome.getText().toString();
		carro.placa = campoPlaca.getText().toString();
		// Salvar
		salvarCarro(carro);

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	public void excluir() {
		if (id != null) {
			excluirCarro(id);
		}

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	// Buscar o carro pelo id
	protected Carro buscarCarro(long id) {
		return ListCarScreen.repositorio.buscarCarro(id);
	}

	// Salvar o carro
	protected void salvarCarro(Carro carro) {
		ListCarScreen.repositorio.salvar(carro);
	}

	// Excluir o carro
	protected void excluirCarro(long id) {
		ListCarScreen.repositorio.deletar(id);
	}

	public void organizeBt() {
		// bt left
		ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setBackgroundResource(R.drawable.bt_cancel);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setBackgroundResource(R.drawable.bt_save);
	}
	public void btBarLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarRight(View v) {
		// TODO Auto-generated method stub

	}
}
