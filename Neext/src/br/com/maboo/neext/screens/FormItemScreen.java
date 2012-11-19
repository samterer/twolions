package br.com.maboo.neext.screens;

import java.util.Calendar;
import java.util.Vector;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.FormItemActivity;
import br.com.maboo.neext.interfaces.InterfaceBar;
import br.com.maboo.neext.model.ItemModel;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.util.Constants;
import br.com.maboo.neext.util.EditTextTools;
import br.com.maboo.neext.util.TextViewTools;

/**
 * Activity que utiliza o TableLayout para editar o itemLog
 * 
 * @author rlecheta
 * 
 */
public class FormItemScreen extends FormItemActivity implements InterfaceBar {

	private final String TAG = Constants.LOG_APP;

	// Campos texto
	private TextView date;
	private TextView hour;
	private EditText subject;
	private EditText text;

	private static Long id_item;
	private static String type;

	// itemRequest na tela
	private ItemNote itemRequest;

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

		final Bundle extras = getIntent().getExtras();

		if (extras != null) { // Se for para Editar, recuperar os valores ...

			String task = extras.getString("task");

			if (task.equals("create")) { // cria novo itemRequest

				type = extras.getString(ItemNote.TYPE);
				Log.i(TAG, "searching type [" + type + "]");

			} else if (task.equals("edit")) { // edit itemRequest

				id_item = extras.getLong(ItemNote._ID);

				Log.i(TAG, "searching itemRequest [" + id_item + "]");
				itemRequest = ItemModel.buscarItemNote(id_item); // busca
																	// informações
																	// do
				// itemRequest

				type = itemRequest.getType();

			}

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

		tv = (TextView) findViewById(R.id.t_subject);
		vTextView.add(tv);

		subject = (EditText) findViewById(R.id.subject);
		subject.setTypeface(tf);

		vEditText.add(subject);

		// text

		tv = (TextView) findViewById(R.id.t_text);
		vTextView.add(tv);

		text = (EditText) findViewById(R.id.text);
		text.setTypeface(tf);

		vEditText.add(text);

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

			subject.setText(String.valueOf((itemRequest.getSubject())));

			// text

			text.setText(String.valueOf((itemRequest.getText())));

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

		ItemNote itemLog4Save = new ItemNote();
		if (id_item != null) {
			// É uma atualização
			itemLog4Save.setId(id_item);
		}

		// type of itemRequest
		itemLog4Save.setType(type);

		// hour
		// get date for save
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date.getText().toString() + "-" + hour.getText());

		itemLog4Save.setDate(sbDate.toString());

		// subject

		itemLog4Save.setSubject(subject.getText().toString());

		itemLog4Save.setText(text.getText().toString());

		// OK
		setResult(RESULT_OK, new Intent());

		// Fecha a tela
		finish();

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
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

	public void onBackPressed() { // call my backbutton pressed method when
		// boolean==true

	}

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
