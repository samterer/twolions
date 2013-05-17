package br.com.maboo.fuellist.screens;

import java.util.Calendar;
import java.util.Vector;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.core.FormItemActivity;
import br.com.maboo.fuellist.interfaces.InterfaceBar;
import br.com.maboo.fuellist.model.ItemModel;
import br.com.maboo.fuellist.modelobj.Carro;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.modelobj.Settings;
import br.com.maboo.fuellist.rules.Rules;
import br.com.maboo.fuellist.util.AndroidUtils;
import br.com.maboo.fuellist.util.Constants;
import br.com.maboo.fuellist.util.EditTextTools;
import br.com.maboo.fuellist.util.TextViewTools;

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
	private TextView t_value_u_desc;

	private EditText value_p;
	private TextView t_value_p_desc;

	private EditText odometer;
	private TextView t_odometer_desc;

	private TextView date;
	private TextView hour;
	private EditText subject;
	private EditText text;

	// titulo dos campos
	private TextView tName;
	private TextView tPlate;
	private TextView tType;

	private static Long id_item = null;
	private static Long id_car = null;
	private static String name_car;

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

	private Settings set;

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

			name_car = extras.getString(Carro.NOME);

			String task = extras.getString("task");

			if (task.equals("create")) { // cria novo itemRequest

				id_item = null; // mantem o id do item nulo

				id_car = extras.getLong(Carro._ID);
				// Log.i(TAG, "searching type [" + id_car + "]");

				type = extras.getInt(ItemLog.TYPE);
				// Log.i(TAG, "searching type [" + type + "]");

			} else if (task.equals("edit")) { // edit itemRequest

				id_item = extras.getLong(ItemLog._ID);

				// Log.i(TAG, "searching itemRequest [" + id_item + "]");
				try {
					itemRequest = ItemModel.buscarItemLog(id_item); // busca
																	// informações
																	// do
					// itemRequest
				} catch (SQLException e) {

					// erro caricato
					AndroidUtils
							.alertDialog(this,
									"Sorry, please... soooorry. And now, re-start the app.");

					e.printStackTrace();
				}

				id_car = itemRequest.getId_car(); // identificador do automovel
				type = itemRequest.getType(); // identificador do tipo do item

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

		// cria title
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(name_car);

		// tamanho da fonte, para não estourar o espaço
		if (title.getText().length() > 10) {
			title.setTextSize(15);
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

			t_value_u_desc = (TextView) findViewById(R.id.t_value_u_desc);
			vTextView.add(t_value_u_desc);

			tv = (TextView) findViewById(R.id.t_value_u);
			vTextView.add(tv);

			value_u = (EditText) findViewById(R.id.value_u);
			value_u.setTypeface(tf);

			vEditText.add(value_u);
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {

			t_value_p_desc = (TextView) findViewById(R.id.t_value_p_desc);
			vTextView.add(t_value_p_desc);

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

			t_odometer_desc = (TextView) findViewById(R.id.t_odometer_desc);
			vTextView.add(t_odometer_desc);

			tv = (TextView) findViewById(R.id.t_odometer);
			vTextView.add(tv);

			odometer = (EditText) findViewById(R.id.odometer);
			odometer.setTypeface(tf);

			if (id_car != null) {

				odometer.setText(String.valueOf(ItemModel
						.buscarUltOdometroPorItem(id_car))); // recupera o
																// ultimo
																// odometer
																// desse
																// veiculo
				if (odometer.getText().toString() == "0") { // se o odometro for
															// ficar com
					// o
					// valor zero, ele ira ficar
					// vazio
					odometer.setText("");
				}

			}

			vEditText.add(odometer);
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

		getSharedPrefs(); // carrega preferencias

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

				// Log.i(TAG, "insert [" + dateFromBase.charAt(i) + "]");

				sb.append(dateFromBase.charAt(i));
			}

			// subject
			if (type == EXPENSE || type == REPAIR || type == NOTE) {
				subject.setText(String.valueOf((itemRequest.getSubject())));
			}

			// value u
			if (type == FUEL) {
				t_value_u_desc.setText(set.getMoeda() + "/" + set.getVolume());
				value_u.setText(String.valueOf((itemRequest.getValue_u())));
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
				t_value_p_desc.setText(set.getMoeda());
				value_p.setText(String.valueOf((itemRequest.getValue_p())));
			}

			// text
			if (type == NOTE) {
				text.setText(String.valueOf((itemRequest.getText())));
			}

			// odemeter
			if (type == FUEL) {
				t_odometer_desc.setText(set.getDist());

				// recupera ultimo valor registrado de odometer(por data) deste
				// veiculo
				odometer.setText(String.valueOf((itemRequest.getOdometer())));
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.e(TAG, itemRequest.toString());
		}

	}

	private void getSharedPrefs() {

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		this.set = new Settings(sharedPrefs);

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/
	/**
	 * Regra de save do item - verifica se é um update - verifica se é um novo
	 * item que esta sendo salvo
	 */
	public void ruleSave() {
		if (id_item != null) {// atualização
			updateItem(); // atualiza item
		} else {
			saveNewItem(); // cria novo item no banco
		}
	}

	public void saveNewItem() {
		ItemLog itemLog4Save = new ItemLog();

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

			itemLog4Save.setValue_u(Double
					.valueOf(value_u.getText().toString()));
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {

			itemLog4Save.setValue_p(Double
					.valueOf(value_p.getText().toString()));
		}

		// text
		if (type == NOTE) {
			itemLog4Save.setText(text.getText().toString());
		}

		// odemeter
		if (type == FUEL) {

			// get last odometer

			itemLog4Save.setOdometer(Long
					.valueOf(odometer.getText().toString()));
		}

		// regra de negocio
		if (type == FUEL) { // regras de fuel
			if (!Rules.ruleFuel(itemLog4Save, this)) {
				return;
			}
		}

		try {
			id_item = ItemModel.salvarItemLog(itemLog4Save); // no caso de
																// ser a

			showToast("Saved!"); // mensagem para o usuario de que o item foi
									// salvo
			// primeira inserção
			// já devolve o id
			// do novo item
		} catch (SQLException e) {

			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

			e.printStackTrace();
		}

		backToViewItemScreen();
	}

	public void updateItem() {
		int cont = 0; // se esse valor for maior que zero o item sera
		// atualizado\criado
		ItemLog itemLog4Save = new ItemLog();

		// id_car
		itemLog4Save.setId_car(id_car);

		// id_item
		itemLog4Save.setId(id_item);

		// type of itemRequest
		itemLog4Save.setType(type);

		// date/hour
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date.getText().toString() + "-" + hour.getText());

		if (!itemRequest.getDate().equals(sbDate.toString())) {
			cont++;
		}

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {
			if (!itemRequest.getSubject().equals(subject.getText().toString())) {
				cont++;
			}
		}

		// value u
		if (type == FUEL) {
			if (!String.valueOf(itemRequest.getValue_u()).equals(
					value_u.getText().toString())) {
				cont++;
			}
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {
			if (!String.valueOf(itemRequest.getValue_p()).equals(
					value_p.getText().toString())) {
				cont++;
			}
		}

		// text
		if (type == NOTE) {
			if (!itemRequest.getText().equals(text.getText().toString())) {
				cont++;
			}
		}

		// odemeter
		if (type == FUEL) {
			if (!String.valueOf(itemRequest.getOdometer()).equals(
					odometer.getText().toString())) {
				cont++;
			}
		}

		// regra de negocio
		if (type == FUEL) { // regras de fuel
			if (!Rules.ruleFuel(itemLog4Save, this)) {
				return;
			}
		}

		// date/hour
		itemLog4Save.setDate(sbDate.toString());

		// subject
		if (type == EXPENSE || type == REPAIR || type == NOTE) {
			itemLog4Save.setSubject(subject.getText().toString());
		}

		// value u
		if (type == FUEL) {

			itemLog4Save.setValue_u(Double
					.valueOf(value_u.getText().toString()));
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {

			itemLog4Save.setValue_p(Double
					.valueOf(value_p.getText().toString()));
		}

		// text
		if (type == NOTE) {
			itemLog4Save.setText(text.getText().toString());
		}

		// odemeter
		if (type == FUEL) {

			// get last odometer

			itemLog4Save.setOdometer(Long
					.valueOf(odometer.getText().toString()));
		}

		// regra de negocio
		if (type == FUEL) { // regras de fuel
			if (!Rules.ruleFuel(itemLog4Save, this)) {
				return; // não passou na regra
			}
		}

		// Salvar
		if (cont > 0) { // confirma se o item vai realmente ser atualizado
						// (significa que o user mudou parametros na tela)
			// Log.i(TAG, "save [" + itemLog4Save.toString() + "]");
			try {
				id_item = ItemModel.salvarItemLog(itemLog4Save); // no caso de
																	// ser a
				// primeira inserção
				// já devolve o id
				// do novo item
			} catch (SQLException e) {

				// erro caricato
				AndroidUtils
						.alertDialog(this,
								"Sorry, please... soooorry. And now, re-start the app.");

				e.printStackTrace();
			}

			showToast("Updated!");

		}

		backToViewItemScreen();
	}

	/**
	 * Retorna pra tela de view item
	 * 
	 * @param c
	 * @return
	 */
	private void backToViewItemScreen() {

		// retorna para a tela de view
		Intent it = new Intent(this, ViewItemScreen.class);

		// Passa o id do item
		it.putExtra(ItemLog._ID, id_item);

		// OK
		setResult(RESULT_OK, it);

		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // mata a pilha de
														// activitys
		// Abre a tela de edição
		startActivity(it);

		finish();

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

	/**
	 * Criada mensagem dizendo que o arquivo foi salvo
	 * 
	 * @param c
	 * @return
	 */
	private void showToast(String msg) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) findViewById(R.id.custom_toast_layout_id));

		TextView text = (TextView) layout.findViewById(R.id.text_toast);
		text.setText(msg);

		ImageView image = (ImageView) layout.findViewById(R.id.image_toast);

		switch (type) {
		case Constants.FUEL:
			image.setImageResource(R.drawable.fuel);
			break;
		case Constants.EXPENSE:
			image.setImageResource(R.drawable.expense);
			break;
		case Constants.NOTE:
			image.setImageResource(R.drawable.note);
			break;
		case Constants.REPAIR:
			image.setImageResource(R.drawable.repair);
			break;
		}

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
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

	public void btBarUpLeft(final View v) {

		// Fecha a tela
		finish();

	}

	public void btBarUpRight(final View v) {

		if (EditTextTools.isEmptyEdit(vEditText, this, "alert")) {
			return;
		}

		ruleSave();
	}

	public void onBackPressed() { // do call my back button method when

		if (!EditTextTools.isEmptyEdit(vEditText, this, "noalert")) {
			ruleSave();
		}

		super.onBackPressed();
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

			// set current hour into textview
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

			// set current date into textview
			date.setText(new StringBuilder().append(pad(day_time)).append("/")
					.append(pad(month_time)).append("/").append(pad(year_time)));
		}
	};

	public void btBarDown(View v) {
		// TODO Auto-generated method stub

	}

}
