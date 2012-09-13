package br.com.twolions.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import br.com.core.ActivityCircle;
import br.com.twolions.R;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.Carro.Carros;
import br.com.twolions.interfaces.InterfaceBar;

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
	private Button campoTipoMoto;
	private Button campoTipoCar;
	private String tipo = "carro";
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
		campoTipoCar = (Button) findViewById(R.id.campoTipoCar);
		campoTipoMoto = (Button) findViewById(R.id.campoTipoMoto);

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
				// tipo
				if (c.tipo.equals("moto")) {
					tipo = "moto";
					campoTipoMoto.setPressed(true);
					campoTipoCar.setPressed(false);
				} else {
					tipo = "carro";
					campoTipoCar.setPressed(true);
					campoTipoMoto.setPressed(false);
				}
			}
		}
	}

	public void actionBt(Context context) {

		// carro
		campoTipoCar.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					return true;
				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;

				if (campoTipoCar.isPressed()) {
					tipo = "";
					campoTipoCar.setPressed(false);
				} else {
					tipo = "carro";
					campoTipoCar.setPressed(true);
					campoTipoMoto.setPressed(false);
				}

				return true;
			}
		});

		// moto
		campoTipoMoto.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					return true;
				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;

				if (campoTipoMoto.isPressed()) {
					tipo = "";
					campoTipoMoto.setPressed(false);
				} else {
					tipo = "moto";
					campoTipoMoto.setPressed(true);
					campoTipoCar.setPressed(false);
				}

				return true;
			}
		});

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
		if (tipo.equals("")) {
			tipo = "carro";
		}
		carro.tipo = tipo.trim();

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
		bt_left.setImageResource(R.drawable.bt_cancel_long);

		// bt rigt
		ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_save);

		// title
		ImageView title = (ImageView) findViewById(R.id.title);
		title.setVisibility(View.INVISIBLE);
	}

	public void btBarLeft(View v) {
		setResult(RESULT_CANCELED);
		// Fecha a tela
		finish();

	}

	public void btBarRight(View v) {
		salvar();
	}
}
