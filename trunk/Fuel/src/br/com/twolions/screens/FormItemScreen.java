package br.com.twolions.screens;

import java.util.Calendar;
import java.util.Vector;

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
	private EditText subject;
	private EditText text;

	private static Long id_item;
	private static Long id_car;
	private static int type;
	private static final int FUEL = Constants.FUEL;
	private static final int EXPENSE = Constants.EXPENSE;
	private static final int NOTE = Constants.NOTE;
	private static final int REPAIR = Constants.REPAIR;

	// itemResquest na tela
	private ItemLog itemResquest;

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

			if (task.equals("create")) { // cria novo itemResquest

				id_car = extras.getLong(Carro._ID);
				Log.i(TAG, "searching type [" + id_car + "]");

				type = extras.getInt(ItemLog.TYPE);
				Log.i(TAG, "searching type [" + type + "]");

			} else if (task.equals("edit")) { // edit itemResquest

				id_item = extras.getLong(ItemLog._ID);

				Log.i(TAG, "searching itemResquest [" + id_item + "]");
				itemResquest = buscarItemLog(id_item); // busca informações do
														// itemResquest

				id_car = itemResquest.getId_car();
				type = itemResquest.getType();

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

		if (itemResquest != null) { // edit itemResquest?

			Log.i(TAG, "Edição de itemResquest...");

			loadingEdit();

		} else {// create a new itemResquest?

			Log.i(TAG, "Criação de itemResquest...");

		}

	}

	// open screen with datas of object
	public void loadingEdit() {

		Log.i(TAG, "Data for edit");
		Log.i(TAG, itemResquest.toString());
		// }

		try {

			// date
			date.setText(String.valueOf((itemResquest.getDate())));

			// subject
			if (type == EXPENSE || type == REPAIR || type == NOTE) {
				subject.setText(String.valueOf((itemResquest.getSubject())));
			}

			// value u
			if (type == FUEL) {
				value_u.setText(String.valueOf((itemResquest.getValue_u())));
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
				value_p.setText(String.valueOf((itemResquest.getValue_p())));
			}

			// text
			if (type == NOTE) {
				text.setText(String.valueOf((itemResquest.getText())));
			}

			// odemeter
			if (type == FUEL) {

				// recupera ultimo valor registrado de odometer(por data) deste
				// veiculo

				odometer.setText(String.valueOf((itemResquest.getOdometer())));
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.e(TAG, itemResquest.toString());
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

		// type of itemResquest
		itemLog4Save.setType(type);

		// date
		itemLog4Save.setDate(date.getText().toString());

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

		date.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				showDialog(TIME_DIALOG_ID);

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

					if (!s.toString().matches(
							"^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {

						String userInput = ""
								+ s.toString().replaceAll("[^\\d]", "");

						Float in = Float.parseFloat(userInput);
						float percen = in / 100;

						value_u.setText("$" + percen);
					}

				}
			});
		}

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
