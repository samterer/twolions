package br.com.twolions.screens;

import java.util.Calendar;
import java.util.Vector;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.core.FormItemActivity;
import br.com.twolions.daoobjects.Carro;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.rules.ItemRules;
import br.com.twolions.util.Constants;
import br.com.twolions.util.EditTextTools;
import br.com.twolions.util.TextViewTools;

/**
 * Activity que utiliza o TableLayout para editar o itemLog
 * 
 * @author rlecheta
 * 
 */
public class FormItemScreen extends FormItemActivity implements InterfaceBar {

	private final String CATEGORIA = Constants.LOG_APP;

	// Campos texto
	private EditText value_u;
	private EditText value_p;
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

	// item na tela
	private ItemLog item;

	// date
	private static final int TIME_DIALOG_ID = 999;
	private int hour;
	private int minute;

	// spinner value
	private static final int NUMER_VP_DIALOG_ID = 111;

	Vector<EditText> vEditText; // vetor de editText
	Vector<TextView> vTextView; // vetor de TextViews

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

		vEditText = new Vector<EditText>();
		vTextView = new Vector<TextView>();

		TextView tv;

		id_car = 0L; // sempre tenho um id de carro

		final Bundle extras = getIntent().getExtras();

		if (extras != null) { // Se for para Editar, recuperar os valores ...

			String task = extras.getString("task");

			if (task.equals("create")) { // cria novo item

				id_car = extras.getLong(Carro._ID);
				Log.i(CATEGORIA, "searching type [" + id_car + "]");

				type = extras.getInt(ItemLog.TYPE);
				Log.i(CATEGORIA, "searching type [" + type + "]");

			} else if (task.equals("edit")) { // edit item

				id_item = extras.getLong(ItemLog._ID);

				Log.i(CATEGORIA, "searching item [" + id_item + "]");
				item = buscarItemLog(id_item); // busca informações do item

				id_car = item.getId_car();
				type = item.getType();

			}

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

		date.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)));

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {

			tv = (TextView) findViewById(R.id.t_subject);
			vTextView.add(tv);

			subject = (EditText) findViewById(R.id.subject);
			subject.setTypeface(tf);

			vEditText.add(subject);
		}

		// value u
		if (type == FUEL) {

			tv = (TextView) findViewById(R.id.t_value_u);
			vTextView.add(tv);

			value_u = (EditText) findViewById(R.id.value_u);
			value_u.setTypeface(tf);

			vEditText.add(value_u);
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {

			tv = (TextView) findViewById(R.id.t_value_p);
			vTextView.add(tv);

			value_p = (EditText) findViewById(R.id.value_p);
			value_p.setTypeface(tf);

			vEditText.add(value_p);
		}

		// text
		if (type == NOTE) {

			tv = (TextView) findViewById(R.id.t_text);
			vTextView.add(tv);

			text = (EditText) findViewById(R.id.text);
			text.setTypeface(tf);

			vEditText.add(text);
		}

		// odemeter
		if (type == FUEL) {

			tv = (TextView) findViewById(R.id.t_odometer);
			vTextView.add(tv);

			odometer = (TextView) findViewById(R.id.odometer);
			odometer.setTypeface(tf);

		}

		EditTextTools.insertFontInAllFields(vEditText, tf); // change font
															// editText
		TextViewTools.insertFontInAllFields(vTextView, tf); // change font
															// textView

		if (item != null) { // edit item?

			Log.i(CATEGORIA, "Edição de item...");

			loadingEdit();

		} else {// create a new item?

			Log.i(CATEGORIA, "Criação de item...");

		}

	}

	// open screen with datas of object
	public void loadingEdit() {

		// searching item
		// Log.i(CATEGORIA, "searching item [" + id_item + "]");
		// final ItemLog i = buscarItemLog(id_item);

		// if (item == null) { // retirar depois, dupla verificação
		// Toast.makeText(this, "Dados do item não encontrados na base.",
		// Toast.LENGTH_SHORT).show();
		// onPause(); // fecha o form
		// return;
		// } else {
		// get id car
		// id_car = item.getId_car();
		Log.i(CATEGORIA, "Data for edit");
		Log.i(CATEGORIA, item.toString());
		// }

		try {

			// date

			date.setText(String.valueOf((item.getDate())));

			// subject
			if (type == EXPENSE || type == REPAIR || type == NOTE) {
				subject.setText(String.valueOf((item.getSubject())));
			}

			// value u
			if (type == FUEL) {
				value_u.setText(String.valueOf((item.getValue_u())));
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
				value_p.setText(String.valueOf((item.getValue_p())));
			}

			// text
			if (type == NOTE) {
				text.setText(String.valueOf((item.getText())));
			}

			// odemeter
			if (type == FUEL) {
				odometer.setText(String.valueOf((item.getOdometer())));
			}

		} catch (NullPointerException e) {
			e.printStackTrace();

			Log.e(CATEGORIA, item.toString());
		}

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	// @Override
	// protected void onPause() {
	// super.onPause();
	// // Cancela para não ficar nada na tela pendente
	// setResult(RESULT_CANCELED);
	//
	// // Fecha a tela
	// finish();
	// }

	public void salvar() {

		// if (type == EXPENSE || type == REPAIR || type == NOTE) {
		if (EditTextTools.isEmptyEdit(vEditText, this)) {
			return;
		}

		ItemLog itemLog = new ItemLog();
		if (id_item != null) {
			// É uma atualização
			itemLog.setId(id_item);
		}

		// id_car
		itemLog.setId_car(id_car);

		// type of item
		itemLog.setType(type);

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

		// regra de negocio
		if (type == FUEL) {
			if (ItemRules.ruleManager(item, this)) {

				// Salvar
				salvarItemLog(itemLog);

			} else {
				return;
			}
		} else {

			// Salvar
			salvarItemLog(itemLog);

		}

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

	// Buscar o itemLog pelo id_item
	protected ItemLog buscarItemLog(final long id) {
		return ListItemScreen.dao.buscarItemLog(id);
	}

	// Salvar o itemLog
	protected void salvarItemLog(final ItemLog itemLog) {
		ListItemScreen.dao.salvar(itemLog);
	}

	// Excluir o itemLog
	protected void excluirItemLog(final long id) {
		ListItemScreen.dao.deletar(id);
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
		salvar();
	}

	// public void showTimePickerDialog(final View v) {
	// Log.i(CATEGORIA, "open time picker");
	// showDialog(TIME_DIALOG_ID);
	// }
	Spinner size_spinner;

	public void addListenerOnButton() {

		date.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(TIME_DIALOG_ID);

			}

		});

		// values

		value_p.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				Toast.makeText(FormItemScreen.this,
						"value current[" + v.getText() + "]",
						Toast.LENGTH_SHORT).show();

				return false;
			}

		});

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
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

}
