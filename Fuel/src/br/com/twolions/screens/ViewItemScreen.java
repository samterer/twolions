package br.com.twolions.screens;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.core.FormItemActivity;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.util.Constants;

public class ViewItemScreen extends FormItemActivity implements InterfaceBar {

	private final String CATEGORIA = Constants.LOG_APP;

	// Campos texto
	private TextView value_u;
	private TextView value_p;
	private TextView odometer;
	private TextView date;
	private EditText subject;
	private EditText text;

	private static Long id_item;
	private static Long id_car;
	private static int type;
	private static final int FUEL = Constants.FUEL;
	private static final int EXPENSE = Constants.EXPENSE;
	private static final int NOTE = Constants.NOTE;
	private static final int REPAIR = Constants.REPAIR;

	protected static final int INSERIR_EDITAR = 1;

	// item na tela
	private ItemLog item;

	// date
	static final int TIME_DIALOG_ID = 999;
	private int hour;
	private int minute;

	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		init();

		organizeBt();

		actionBt(this);

		// addListenerOnButton();
	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	private void init() {

		TextView tv;

		final Bundle extras = getIntent().getExtras();

		if (extras != null) { // Se for para visualizar, recuperar os valores
								// ...
			id_item = extras.getLong(ItemLog._ID);
			Log.i(CATEGORIA, "searching item [" + id_item + "]");
			if (id_item != null) { // searching item
				Log.i(CATEGORIA, "searching item [" + id_item + "]");
				item = buscarItemLog(id_item);
			} else {
				finish();
			}
			type = item.getType();
			id_car = item.getId_car();
		}

		// instance itens of xml
		switch (type) {
		case FUEL:
			setContentView(R.layout.form_fuel);
			break;
		case EXPENSE:
			setContentView(R.layout.form_expense);
			break;
		case NOTE:
			setContentView(R.layout.form_note);
			break;
		case REPAIR:
			setContentView(R.layout.form_repair);
			break;
		}

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansFallback.ttf"); // modifica as fontes

		// date
		date = (TextView) findViewById(R.id.date);
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		date.setText(new StringBuilder().append(pad(hour)).append("")
				.append(pad(minute)));

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {

			tv = (TextView) findViewById(R.id.t_subject);
			tv.setTypeface(tf);

			subject = (EditText) findViewById(R.id.subject);
			subject.setTypeface(tf);
		}

		// value u
		if (type == FUEL) {

			tv = (TextView) findViewById(R.id.t_value_u);
			tv.setTypeface(tf);

			value_u = (TextView) findViewById(R.id.value_u);
			value_u.setTypeface(tf);
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {

			tv = (TextView) findViewById(R.id.t_value_p);
			tv.setTypeface(tf);

			value_p = (TextView) findViewById(R.id.value_p);
			value_p.setTypeface(tf);
		}

		// text
		if (type == NOTE) {

			tv = (TextView) findViewById(R.id.t_text);
			tv.setTypeface(tf);

			text = (EditText) findViewById(R.id.text);
			text.setTypeface(tf);
		}

		// odemeter
		if (type == FUEL) {

			tv = (TextView) findViewById(R.id.t_odometer);
			tv.setTypeface(tf);

			odometer = (TextView) findViewById(R.id.odometer);
			odometer.setTypeface(tf);
		}

		// edit ?
		Log.i(CATEGORIA, "Visualização de item...");
		loadingDataItem();

	}

	// open screen with datas of object
	public void loadingDataItem() {

		if (item == null) {
			Toast.makeText(this, "Dados do item não encontrados na base.",
					Toast.LENGTH_SHORT).show();
			finish(); // fecha a view em caso de erro

		} else {
			// get id car
			// id_car = i.getId_car();
			Log.i(CATEGORIA, "Data for edit");
			Log.i(CATEGORIA, item.toString());
		}

		try {

			// date
			date.setText(String.valueOf((item.getDate())));

			// subject
			if (type == EXPENSE || type == REPAIR || type == NOTE) {
				subject.setText(String.valueOf((item.getSubject())));
				subject.setFocusable(false);
			}

			// value u
			if (type == FUEL) {
				value_u.setText(String.valueOf((item.getValue_u())));
				value_u.setFocusable(false);
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
				value_p.setText(String.valueOf((item.getValue_p())));
				value_p.setFocusable(false);
			}

			// text
			if (type == NOTE) {
				text.setText(String.valueOf((item.getText())));
				text.setFocusable(false);
			}

			// odemeter
			if (type == FUEL) {
				odometer.setText(String.valueOf((item.getOdometer())));
				odometer.setFocusable(false);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();

			Log.e(CATEGORIA, item.toString());
		}

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	// Buscar o itemLog pelo id_item
	protected ItemLog buscarItemLog(final long id) {
		return ListLogScreen.dao.buscarItemLog(id);
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/

	public void actionBt(final Context context) {

	}

	public void organizeBt() {
		// bt left
		final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_cancel);

		// bt rigt
		// final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		// bt_right.setImageResource(R.drawable.bt_menu);

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
		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// id do item
		it.putExtra(ItemLog._ID, id_item);
		// passa o tipo do item
		it.putExtra(ItemLog.TYPE, type);
		// passa o id do carro do item
		it.putExtra(Carro._ID, id_car);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}

}
