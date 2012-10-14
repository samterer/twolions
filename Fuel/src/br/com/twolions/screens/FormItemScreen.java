package br.com.twolions.screens;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.core.FormItemActivity;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.util.Constants;

/**
 * Activity que utiliza o TableLayout para editar o itemLog
 * 
 * @author rlecheta
 * 
 */
public class FormItemScreen extends FormItemActivity implements InterfaceBar {

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

	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		init();

		organizeBt();

		actionBt(this);

		addListenerOnButton();
	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	private void init() {

		final Bundle extras = getIntent().getExtras();
		// Se for para Editar, recuperar os valores ...
		if (extras != null) {
			id_item = extras.getLong(ItemLog._ID);
			//Log.i(CATEGORIA, "searching item [" + id_item + "]");
			id_car = extras.getLong(Carro._ID);
			// Log.i(CATEGORIA, "searching type [" + id_car + "]");
			type = extras.getInt(ItemLog.TYPE);
			// Log.i(CATEGORIA, "searching type [" + type + "]");
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
		// Date dateCurrent = new Date();

		// formatting hour in h (1-12 in AM/PM) format like 1, 2..12.
		// String strDateFormat = "h";
		// SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

		// Log.i(CATEGORIA, "hour in h format : " + sdf.format(dateCurrent));

		// date
		date = (TextView) findViewById(R.id.date);
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		date.setText(new StringBuilder().append(pad(hour)).append("")
				.append(pad(minute)));

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {
			subject = (EditText) findViewById(R.id.subject);
		}

		// value u
		if (type == FUEL) {
			value_u = (TextView) findViewById(R.id.value_u);
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == NOTE || type == FUEL) {
			value_p = (TextView) findViewById(R.id.value_p);
		}

		// text
		if (type == NOTE) {
			text = (EditText) findViewById(R.id.text);
		}

		// odemeter
		if (type == FUEL) {
			odometer = (TextView) findViewById(R.id.odometer);
		}

		// edit ?
		if (id_item != null) {
			Log.i(CATEGORIA, "Edi��o de item...");
			loadingEdit();
		} else {
			Log.i(CATEGORIA, "Cria��o de item...");
		}

	}

	// open screen with datas of object
	public void loadingEdit() {

		// searching item
		Log.i(CATEGORIA, "searching item [" + id_item + "]");
		final ItemLog i = buscarItemLog(id_item);

		if (i == null) {
			Toast.makeText(this, "Dados do item n�o encontrados na base.",
					Toast.LENGTH_SHORT).show();
			onPause(); // fecha o form
			return;
		} else {
			// get id car
			id_car = i.getId_car();
			Log.i(CATEGORIA, "Data for edit");
			Log.i(CATEGORIA, i.toString());
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
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
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

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	// @Override
	// protected void onPause() {
	// super.onPause();
	// // Cancela para n�o ficar nada na tela pendente
	// setResult(RESULT_CANCELED);
	//
	// // Fecha a tela
	// finish();
	// }

	public void salvar() {

		ItemLog itemLog = new ItemLog();
		if (id_item != null) {
			// � uma atualiza��o
			itemLog.setId(id_item);
			itemLog.setId_car(id_car);
			itemLog.setType(type);
		}

		// date
		itemLog.setDate(date.getText().toString());

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {
			itemLog.setSubject(subject.getText().toString());
		}

		// value u
		if (type == FUEL) {
			itemLog.setValue_u(Double.valueOf(value_u.getText().toString()));
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {
			itemLog.setValue_p(Double.valueOf(value_p.getText().toString()));
		}
		
		// text
		if (type == NOTE) {
			itemLog.setText(text.getText().toString());
		}

		// odemeter
		if (type == FUEL) {
			itemLog.setOdometer(Long.valueOf(odometer.getText().toString()));
		}

		// Salvar
		salvarItemLog(itemLog);

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();
	}

	// Buscar o itemLog pelo id_item
	protected ItemLog buscarItemLog(final long id) {
		return ListLogScreen.dao.buscarItemLog(id);
	}

	// Salvar o itemLog
	protected void salvarItemLog(final ItemLog itemLog) {
		ListLogScreen.dao.salvar(itemLog);
	}

	// Excluir o itemLog
	protected void excluirItemLog(final long id) {
		ListLogScreen.dao.deletar(id);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case TIME_DIALOG_ID :
				// set time picker as current time
				return new TimePickerDialog(this, timePickerListener, hour,
						minute, false);
		}
		return null;
	}
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			// set current time into textview
			date.setText(new StringBuilder().append(pad(hour)).append(":")
					.append(pad(minute)));

		}
	};
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

	// date
	static final int TIME_DIALOG_ID = 999;
	private int hour;
	private int minute;

	// public void showTimePickerDialog(final View v) {
	// Log.i(CATEGORIA, "open time picker");
	// showDialog(TIME_DIALOG_ID);
	// }

	public void addListenerOnButton() {

		date.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(TIME_DIALOG_ID);

			}

		});

	}

}