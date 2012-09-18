package br.com.twolions.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.twolions.R;
import br.com.twolions.core.FormItemActivity;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.interfaces.InterfaceBar;

/**
 * Activity que utiliza o TableLayout para editar o itemLog
 * 
 * @author rlecheta
 * 
 */
public class FormItemScreen extends FormItemActivity implements InterfaceBar {
	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;

	// Campos texto
	private TextView value_u;
	private TextView value_p;
	private TextView odometer;
	private TextView date;
	private String tipo = "";
	private Long id;
	// id car
	private Long id_car;

	@Override
	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.form_fuel);

		// organize bt
		organizeBt();

		init();

		actionBt(this);
	}

	private void init() {
		value_u = (TextView) findViewById(R.id.value_u);
		value_p = (TextView) findViewById(R.id.value_p);
		odometer = (TextView) findViewById(R.id.odometer);
		date = (TextView) findViewById(R.id.date);

		id = null;

		final Bundle extras = getIntent().getExtras();
		// Se for para Editar, recuperar os valores ...
		if (extras != null) {
			id = extras.getLong(ItemLog._ID);
			id_car = extras.getLong(ItemLog.ID_CAR);

			if (id != null) {
				// é uma edição, busca o itemLog...
				final ItemLog i = buscarItemLog(id);
				value_u.setText(String.valueOf((i.getValue_u())));
				value_p.setText(String.valueOf((i.getValue_p())));
				odometer.setText(String.valueOf((i.getOdometer())));
				date.setText(String.valueOf((i.getDate())));
			}
		}
	}
	public void actionBt(final Context context) {

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

		final ItemLog itemLog = new ItemLog();
		if (id != null) {
			// É uma atualização
			itemLog.setId(id);
			itemLog.setId_car(id_car);
		}
		itemLog.setValue_u(Double
				.valueOf((String) value_u.getText().toString()));
		itemLog.setValue_p(Double.valueOf(value_p.getText().toString()));
		itemLog.setOdometer(Long.valueOf(odometer.getText().toString()));
		itemLog.setValue_p(Double.valueOf(value_p.getText().toString()));
		itemLog.setDate(date.getText().toString());

		// Salvar
		salvarItemLog(itemLog);

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	public void excluir() {
		if (id != null) {
			excluirItemLog(id);
		}

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	// Buscar o itemLog pelo id
	protected ItemLog buscarItemLog(final long id) {
		return ListLogScreen.repositorio.buscarItemLog(id);
	}

	// Salvar o itemLog
	protected void salvarItemLog(final ItemLog itemLog) {
		ListLogScreen.repositorio.salvar(itemLog);
	}

	// Excluir o itemLog
	protected void excluirItemLog(final long id) {
		ListLogScreen.repositorio.deletar(id);
	}

	public void organizeBt() {
		// bt left
		final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_cancel_long);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_save);

		// title
		final ImageView title = (ImageView) findViewById(R.id.title);
		title.setVisibility(View.INVISIBLE);
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
