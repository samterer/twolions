package br.com.twolions.screens;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.core.FormCarActivity;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.util.EditTextTools;

/**
 * Activity que utiliza o TableLayout para editar o carro
 * 
 * @author rlecheta
 * 
 */
public class FormCarScreen extends FormCarActivity implements InterfaceBar {
	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;

	// Campos texto
	private EditText campoNome;
	private EditText campoPlaca;
	private Button campoTipoMoto;
	private Button campoTipoCar;
	private String tipo = "carro";
	private Long id;
	
	Vector<EditText> vEditText; //vetor de editText

	@Override
	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.form_car);

		organizeBt(); // listener dos bts da barra sup

		init();

		changeFont(); // modifica as fontes

		actionBt(this);
	}
	
	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/
	private void init() {
		
		
		vEditText = new Vector<EditText>();

		campoNome = (EditText) findViewById(R.id.campoNome);
		vEditText.add(campoNome);
		campoPlaca = (EditText) findViewById(R.id.campoPlaca);
		vEditText.add(campoPlaca);
		campoTipoCar = (Button) findViewById(R.id.campoTipoCar);
		campoTipoMoto = (Button) findViewById(R.id.campoTipoMoto);

		id = null;

		final Bundle extras = getIntent().getExtras();
		// Se for para Editar, recuperar os valores ...
		if (extras != null) {
			id = extras.getLong(Carro._ID);

			if (id != null) {
				// é uma edição, busca o carro...
				final Carro c = buscarCarro(id);
				campoNome.setText(c.getNome());
				campoPlaca.setText(c.getPlaca());
				// tipo
				if (c.getTipo().equals("moto")) {
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
	
	private void changeFont() {

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansFallback.ttf");

		campoNome.setTypeface(tf);
		campoNome.setHint("insert a name"); 		// implement hint

		campoPlaca.setTypeface(tf);
		campoPlaca.setHint("insert a place"); 		// implement hint

		TextView tv1 = (TextView) findViewById(R.id.text1);
		tv1.setTypeface(tf);

		TextView tv2 = (TextView) findViewById(R.id.text2);
		tv2.setTypeface(tf);

		TextView tv3 = (TextView) findViewById(R.id.text3);
		tv3.setTypeface(tf);

	}

	

	// public void onPause() {
	// super.onPause();
	// // Cancela para não ficar nada na tela pendente
	// // setResult(RESULT_CANCELED);
	//
	// // Fecha a tela
	// finish();
	// }
	
	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/


	public void salvar() {

		
		if(EditTextTools.isEmptyEdit(vEditText,this)) {

			return;
		}

		final Carro carro = new Carro();
		if (id != null) {
			// É uma atualização
			carro.setId(id);
		}
		
		carro.setNome(campoNome.getText().toString());
		
		carro.setPlaca(campoPlaca.getText().toString());
		
		if (tipo.equals("")) {
			tipo = "carro";
		}
		carro.setTipo(tipo.trim());

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
	protected Carro buscarCarro(final long id) {
		return ListCarScreen.dao.buscarCarro(id);
	}

	// Salvar o carro
	protected void salvarCarro(final Carro carro) {
		ListCarScreen.dao.salvar(carro);
	}

	// Excluir o carro
	protected void excluirCarro(final long id) {
		ListCarScreen.dao.deletar(id);
	}
	
	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/

	public void actionBt(final Context context) {

		// carro
		campoTipoCar.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(final View v, final MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					return true;
				}
				if (event.getAction() != MotionEvent.ACTION_UP) {
					return false;
				}

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
			public boolean onTouch(final View v, final MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					return true;
				}
				if (event.getAction() != MotionEvent.ACTION_UP) {
					return false;
				}

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
	
	public void organizeBt() {
		// bt left
		final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_cancel);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_save);

		// title
		// final ImageView title = (ImageView) findViewById(R.id.title);
		// title.setVisibility(View.INVISIBLE);
	}

	public void btBarLeft(final View v) {
		setResult(RESULT_CANCELED);
		// Fecha a tela
		finish();

	}

	public void btBarRight(final View v) {
		salvar();
	}
}
