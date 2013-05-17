package br.com.maboo.fuellist.screens;

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
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.core.FormCarActivity;
import br.com.maboo.fuellist.interfaces.InterfaceBar;
import br.com.maboo.fuellist.modelobj.Carro;
import br.com.maboo.fuellist.util.EditTextTools;
import br.com.maboo.fuellist.util.TextViewTools;

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

	// titulo dos campos
	private TextView tName;
	private TextView tPlate;
	private TextView tType;

	Vector<EditText> vEditText; // vetor de editText
	Vector<TextView> vTextView; // vetor de TextViews

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
		vTextView = new Vector<TextView>();

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

		TextView tv1 = (TextView) findViewById(R.id.tName);
		vTextView.add(tv1);

		TextView tv2 = (TextView) findViewById(R.id.tPlate);
		vTextView.add(tv2);

		TextView tv3 = (TextView) findViewById(R.id.tType);
		vTextView.add(tv3);

		EditTextTools.insertFontInAllFields(vEditText, tf); // change font
															// editText
		TextViewTools.insertFontInAllFields(vTextView, tf); // change font
															// textView

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	private void renomeiaCampos() {
		// verifica o idiota da vez

		// passa o idiota da vez e o vector de vTextView
	}

	public void salvar() {

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

		Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
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

	}

	public void btBarLeft(final View v) {
		setResult(RESULT_CANCELED);

		// Fecha a tela
		finish();

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	public void btBarRight(final View v) {

		if (EditTextTools.isEmptyEdit(vEditText, this, "alert")) {
			return;
		}

		salvar();
	}

	public void onBackPressed() { // call my backbutton pressed method when

		if (EditTextTools.isEmptyEdit(vEditText, this, "noalert")) {
			//
		} else {
			salvar();
		}

		super.onBackPressed();
	}

}
