package br.com.twolions.screens;

import java.util.Calendar;
import java.util.Vector;

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
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.interfaces.InterfaceBar;
import br.com.twolions.settings.Settings;
import br.com.twolions.util.Constants;
import br.com.twolions.util.EditTextTools;
import br.com.twolions.util.TextViewTools;

public class ViewItemScreen extends FormItemActivity implements InterfaceBar {

	private final String CATEGORIA = Constants.LOG_APP;

	// Campos texto
	private EditText value_u;
	private EditText value_p;
	private EditText odometer;
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

	Vector<EditText> vEditText; // vetor de editText
	Vector<TextView> vTextView; // vetor de TextViews

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

		vEditText = new Vector<EditText>();
		vTextView = new Vector<TextView>();

		TextView tv;

		// if (id_item != null) { // retorno da tela de edit de item
		//
		// Log.i(CATEGORIA, "return of edit screen");
		//
		// Log.i(CATEGORIA, "searching item [" + id_item + "]");
		// item = buscarItemLog(id_item);
		//
		// type = item.getType();
		//
		// id_car = item.getId_car();
		//
		// } else { // lista > view item

		Log.i(CATEGORIA, "view item screen");

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {

			id_item = extras.getLong(ItemLog._ID);

			if (id_item != null) { // searching item

				Log.i(CATEGORIA, "searching item [" + id_item + "]");

				item = buscarItemLog(id_item);

			} else {

				finish();

			}

			type = item.getType();

			id_car = item.getId_car();
		}

		// }

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
			vTextView.add(tv);

			subject = (EditText) findViewById(R.id.subject);

			vEditText.add(subject);
		}

		// value u
		if (type == FUEL) {

			tv = (TextView) findViewById(R.id.t_value_u);
			vTextView.add(tv);

			value_u = (EditText) findViewById(R.id.value_u);

			vEditText.add(value_u);
		}

		// value p
		if (type == EXPENSE || type == REPAIR || type == FUEL) {

			tv = (TextView) findViewById(R.id.t_value_p);
			vTextView.add(tv);

			value_p = (EditText) findViewById(R.id.value_p);

			vEditText.add(value_p);
		}

		// text
		if (type == NOTE) {

			tv = (TextView) findViewById(R.id.t_text);
			vTextView.add(tv);

			text = (EditText) findViewById(R.id.text);

			vEditText.add(text);
		}

		// odemeter
		if (type == FUEL) {

			tv = (TextView) findViewById(R.id.t_odometer);
			vTextView.add(tv);

			odometer = (EditText) findViewById(R.id.odometer);

			vEditText.add(odometer);
		}

		EditTextTools.insertFontInAllFields(vEditText, tf); // change font
															// editText

		TextViewTools.insertFontInAllFields(vTextView, tf); // change font
															// textView

		changeFormatEditText(); // aplica o formato para apenas visualiza��o

		// edit ?
		Log.i(CATEGORIA, "Recupera os dados para a visualiza��o do item...");
		loadingDataItem();

	}

	/**
	 * Mantem todos os cursores false e muda o fundo para transparente
	 */
	private void changeFormatEditText() {
		for (int i = 0; i < vEditText.size(); i++) {
			EditText et = (EditText) vEditText.elementAt(i);

			et.setBackgroundColor(0x00000000); // set ao background transparente
			et.setCursorVisible(false); // torna invisivel o cursor
		}

	}

	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// Quando a activity EditarCarro retornar, seja se foi para adicionar
		// vamos atualizar a lista
		if (codigoRetorno == RESULT_OK) {
			// atualiza a lista na tela
			// init();
		}
	}

	// open screen with datas of object
	public void loadingDataItem() {

		if (item == null) {

			Toast.makeText(this, "Dados do item n�o encontrados na base.",
					Toast.LENGTH_SHORT).show();

			finish(); // fecha a view em caso de erro

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
				value_u.setText(Settings.moeda
						+ String.valueOf((item.getValue_u())));
				value_u.setFocusable(false);
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
				value_p.setText(Settings.moeda
						+ String.valueOf((item.getValue_p())));
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
		return ListItemScreen.dao.buscarItemLog(id);
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
		bt_left.setImageResource(R.drawable.bt_back);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_bar_edit);

	}

	public void btBarLeft(final View v) {

		setResult(RESULT_OK);

		// Fecha a tela
		finish();

	}

	public void btBarRight(final View v) {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		it.putExtra("task", "edit");

		// id do item
		it.putExtra(ItemLog._ID, id_item);

		// Abre a tela de edi��o
		startActivityForResult(it, INSERIR_EDITAR);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	public void onBackPressed() { // call my backbutton pressed method when

		Log.i("appLog", "Clicked back");

	}

}
