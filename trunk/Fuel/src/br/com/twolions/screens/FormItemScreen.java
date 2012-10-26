package br.com.twolions.screens;

import java.util.Calendar;
import java.util.Vector;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.twolions.R;
import br.com.twolions.core.FormItemActivity;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.model.Carro;
import br.com.twolions.model.ItemLog;
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

	private final String TAG = Constants.LOG_APP;

	// Campos texto
	private EditText value_u;
	private EditText value_p;
	private TextView odometer;
	private TextView date;
	private TextView hour;
	private EditText subject;
	private EditText text;

	private static Long id_item;
	private static Long id_car;
	private static int type;
	private static final int FUEL = Constants.FUEL;
	private static final int EXPENSE = Constants.EXPENSE;
	private static final int NOTE = Constants.NOTE;
	private static final int REPAIR = Constants.REPAIR;

	// itemRequest na tela
	private ItemLog itemRequest;

	// hour
	private static final int TIME_DIALOG_ID = 999;
	private int hour_time;
	private int min_time;

	private int day_time;
	private int month_time;
	private int year_time;

	// spinner value
	private static final int DATE_DIALOG_ID = 998;

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

			if (task.equals("create")) { // cria novo itemRequest

				id_car = extras.getLong(Carro._ID);
				Log.i(TAG, "searching type [" + id_car + "]");

				type = extras.getInt(ItemLog.TYPE);
				Log.i(TAG, "searching type [" + type + "]");

			} else if (task.equals("edit")) { // edit itemRequest

				id_item = extras.getLong(ItemLog._ID);

				Log.i(TAG, "searching itemRequest [" + id_item + "]");
				itemRequest = buscarItemLog(id_item); // busca informações do
														// itemRequest

				id_car = itemRequest.getId_car();
				type = itemRequest.getType();

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

		// calendar
		final Calendar c = Calendar.getInstance();

		// date
		date = (TextView) findViewById(R.id.date);
		day_time = c.get(Calendar.DAY_OF_MONTH);
		month_time = c.get(Calendar.MONTH);
		year_time = c.get(Calendar.YEAR);

		date.setText(new StringBuilder().append(pad(day_time)).append("/")
				.append(pad(month_time)).append("/").append(pad(year_time)));

		// hour
		hour = (TextView) findViewById(R.id.hour);
		hour_time = c.get(Calendar.HOUR_OF_DAY);
		min_time = c.get(Calendar.MINUTE);

		hour.setText(new StringBuilder().append(pad(hour_time)).append(":")
				.append(pad(min_time)));

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

		if (itemRequest != null) { // edit itemRequest?

			Log.i(TAG, "Edição de itemRequest...");

			loadingEdit();

		} else {// create a new itemRequest?

			Log.i(TAG, "Criação de itemRequest...");

		}

	}

	// open screen with datas of object
	public void loadingEdit() {

		Log.i(TAG, "Data for edit");
		Log.i(TAG, itemRequest.toString());
		// }

		try {

			// formata date
			String dateFromBase = itemRequest.getDate();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dateFromBase.length(); i++) {

				if (dateFromBase.charAt(i) == '-') { // insere valor da
														// data

					Log.i(TAG, "date [" + sb.toString() + "]");

					date.setText(sb.toString());

					// insere os valores nas variaveis de classe
					day_time = Integer.valueOf(
							String.valueOf(sb.toString().subSequence(0, 1)))
							.intValue();

					sb = new StringBuffer();

					i++;

				} else if (i == 15) { // insere
										// valor
										// da hora
										// o numero dessa linha é comparado a
										// 16, pois esse é o tamanho maximo
										// correto de uma data, de acordo com a
										// inserção dela 'dd/mm/aaaa - hh:mm'
					sb.append(dateFromBase.charAt(i));

					Log.i(TAG, "hour [" + sb.toString() + "]");

					hour.setText(sb.toString()); // hora

					break;

				}

				Log.i(TAG, "insert [" + dateFromBase.charAt(i) + "]");

				sb.append(dateFromBase.charAt(i));
			}

			// subject
			if (type == EXPENSE || type == REPAIR || type == NOTE) {
				subject.setText(String.valueOf((itemRequest.getSubject())));
			}

			// value u
			if (type == FUEL) {
				value_u.setText(String.valueOf((itemRequest.getValue_u())));
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
				value_p.setText(String.valueOf((itemRequest.getValue_p())));
			}

			// text
			if (type == NOTE) {
				text.setText(String.valueOf((itemRequest.getText())));
			}

			// odemeter
			if (type == FUEL) {

				// recupera ultimo valor registrado de odometer(por data) deste
				// veiculo

				odometer.setText(String.valueOf((itemRequest.getOdometer())));
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.e(TAG, itemRequest.toString());
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

		ItemLog itemLog4Save = new ItemLog();
		if (id_item != null) {
			// É uma atualização
			itemLog4Save.setId(id_item);
		}

		// id_car
		itemLog4Save.setId_car(id_car);

		// type of itemRequest
		itemLog4Save.setType(type);

		// hour
		// get date for save
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date.getText().toString() + "-" + hour.getText());

		itemLog4Save.setDate(sbDate.toString());

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {
			itemLog4Save.setSubject(subject.getText().toString());
		}

		// value u
		if (type == FUEL) {

			StringBuffer sb = new StringBuffer(value_u.getText().length());

			sb.append(value_u.getText().toString());

			sb.deleteCharAt(0);

			itemLog4Save
					.setValue_u(Double.valueOf(sb.toString()).doubleValue());
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {
			// format number

			StringBuffer sb = new StringBuffer(value_p.getText().length());

			sb.append(value_p.getText().toString());

			sb.deleteCharAt(0);

			itemLog4Save
					.setValue_p(Double.valueOf(sb.toString()).doubleValue());
		}

		// text
		if (type == NOTE) {
			itemLog4Save.setText(text.getText().toString());
		}

		// odemeter
		if (type == FUEL) {
			itemLog4Save.setOdometer(Long
					.valueOf(odometer.getText().toString()));
		}

		// regra de negocio
		if (type == FUEL) { // regras de fuel
			if (ItemRules.ruleManager(itemLog4Save, this)) {

				// Salvar
				Log.i(TAG, "save [" + itemLog4Save.toString() + "]");
				salvarItemLog(itemLog4Save);

			} else {

				// pinta de vermelho os dados nos campos de value_p e value_u
				value_p.setTextColor(R.color.vermelho);
				value_u.setTextColor(R.color.vermelho);

				return;

			}
		} else {

			// Salvar
			salvarItemLog(itemLog4Save);

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

	// String test;

	public void addListenerOnButton() {

		hour.setOnClickListener(new OnClickListener() { // change hour

			public void onClick(View v) {

				showDialog(TIME_DIALOG_ID);

			}

		});

		date.setOnClickListener(new OnClickListener() { // change date

			public void onClick(View v) {

				showDialog(DATE_DIALOG_ID);

			}

		});

		if (value_p != null) { // aplica regra de decimal

			value_p.addTextChangedListener(new TextWatcher() {

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				public void afterTextChanged(Editable s) {

					if (!s.toString().matches(
							"^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {

						String userInput = ""
								+ s.toString().replaceAll("[^\\d]", "");

						Float in = Float.parseFloat(userInput);
						float percen = in / 100;

						value_p.setText("$" + percen);
					} else {
						// invalid number
						return;
					}

				}
			});
		}

		if (value_u != null) { // aplica regra de decimal

			value_u.addTextChangedListener(new TextWatcher() {

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void afterTextChanged(Editable s) {

					if (!value_p.getText().equals("")
							|| value_p.getText().equals("$00,00")) {

						if (!s.toString()
								.matches(
										"^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {

							String userInput = ""
									+ s.toString().replaceAll("[^\\d]", "");

							Float in = Float.parseFloat(userInput);
							float percen = in / 100;

							value_u.setText("$" + percen);
						} else {
							// invalid number
							return;
						}

					}

				}
			});
		}

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour_time,
					min_time, false);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, myDateSetListener, year_time,
					month_time, day_time);
		}

		return null;

	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour_time = selectedHour;
			min_time = selectedMinute;

			// set current time into textview
			hour.setText(new StringBuilder().append(pad(hour_time)).append(":")
					.append(pad(min_time)));

		}
	};

	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			day_time = dayOfMonth;
			month_time = monthOfYear;
			year_time = year;

			// set current time into textview
			date.setText(new StringBuilder().append(pad(day_time)).append("/")
					.append(pad(month_time)).append("/").append(pad(year_time)));

		}
	};

}
