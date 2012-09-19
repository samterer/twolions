package br.com.twolions.screens;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

	private final String CATEGORIA = "appLog";

	static final int RESULT_SALVAR = 1;
	static final int RESULT_EXCLUIR = 2;

	// Campos texto
	private TextView value_u;
	private TextView value_p;
	private TextView odometer;
	private TextView date;
	private TextView subject;
	private TextView text;

	private int type;
	private static final int FUEL = 0;
	private static final int EXPENSE = 1;
	private static final int REPAIR = 3;
	private static final int NOTE = 2;

	private Long id;

	// id car
	private Long id_car;

	@Override
	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		init();

		// organize bt
		organizeBt();

		actionBt(this);
	}

	private void init() {

		id = null;

		final Bundle extras = getIntent().getExtras();
		// Se for para Editar, recuperar os valores ...
		if (extras != null) {
			id = extras.getLong(ItemLog._ID);
			id_car = extras.getLong(ItemLog.ID_CAR);
			type = extras.getInt(ItemLog.TYPE);
		}

		// instance itens of xml
		switch (type) {
			case FUEL :
				setContentView(R.layout.form_fuel);
				break;

			case EXPENSE :
				setContentView(R.layout.form_expense);

				break;
			case NOTE :
				setContentView(R.layout.form_note);
				break;
			case REPAIR :
				setContentView(R.layout.form_repair);
				break;
		}

		// create Date object
		Date dateCurrent = new Date();

		// formatting hour in h (1-12 in AM/PM) format like 1, 2..12.
		String strDateFormat = "h";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

		Log.i(CATEGORIA, "hour in h format : " + sdf.format(dateCurrent));

		// date
		date = (TextView) findViewById(R.id.date);

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {
			subject = (TextView) findViewById(R.id.subject);
		}

		// value u
		if (type == FUEL) {
			value_u = (TextView) findViewById(R.id.value_u);
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == NOTE) {
			value_p = (TextView) findViewById(R.id.value_p);
		}

		// text
		if (type == NOTE) {
			text = (TextView) findViewById(R.id.text);
		}

		// odemeter
		if (type == FUEL) {
			odometer = (TextView) findViewById(R.id.odometer);
		}

		// edit ?
		if (id != null) {
			loadingEdit();
		}

	}

	// open screen with datas of object
	public void loadingEdit() {

		// searching item
		final ItemLog i = buscarItemLog(id);

		if (i == null) {
			Toast.makeText(this, "Dados do item não encontrados na base.",
					Toast.LENGTH_SHORT).show();
			return;
		}

		try {

			// date
			date.setText(String.valueOf((i.getDate())));

			// subject
			if (type == EXPENSE || type == REPAIR || type == NOTE) {
				subject.setText(String.valueOf((i.getSubject())));
			}

			// value u
			if (type == FUEL) {
				value_u.setText(String.valueOf((i.getValue_u())));
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == NOTE) {
				value_p.setText(String.valueOf((i.getValue_p())));
			}

			// text
			if (type == NOTE) {
				text.setText(String.valueOf((i.getText())));
			}

			// odemeter
			if (type == FUEL) {
				odometer.setText(String.valueOf((i.getOdometer())));
			}

		} catch (NullPointerException e) {
			e.printStackTrace();

			Log.e(CATEGORIA, i.toString());
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
