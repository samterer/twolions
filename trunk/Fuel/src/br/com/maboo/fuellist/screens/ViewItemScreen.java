package br.com.maboo.fuellist.screens;

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
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.core.FormItemActivity;
import br.com.maboo.fuellist.interfaces.InterfaceBar;
import br.com.maboo.fuellist.modelobj.Carro;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.util.Constants;
import br.com.maboo.fuellist.util.EditTextTools;
import br.com.maboo.fuellist.util.TextViewTools;

public class ViewItemScreen extends FormItemActivity implements InterfaceBar {

	private final String TAG = Constants.LOG_APP;

	// Campos texto
	private EditText value_u;
	private EditText value_p;
	private EditText odometer;
	private TextView date;
	private TextView hour;
	private EditText subject;
	private EditText text;

	private static Long id_item;
	private static Long id_car;
	private static String name_car;

	private static int type;

	private static final int FUEL = Constants.FUEL;
	private static final int EXPENSE = Constants.EXPENSE;
	private static final int NOTE = Constants.NOTE;
	private static final int REPAIR = Constants.REPAIR;

	protected static final int INSERIR_EDITAR = 1;

	// itemRequest na tela
	private ItemLog itemRequest;

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

		Log.i(TAG, "view itemRequest screen");

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {

			name_car = extras.getString(Carro.NOME);

			id_item = extras.getLong(ItemLog._ID);

			if (id_item != null) { // searching itemRequest

				Log.i(TAG, "searching itemRequest [" + id_item + "]");

				itemRequest = buscarItemLog(id_item);

			} else {

				finish();

			}

			type = itemRequest.getType();

			id_car = itemRequest.getId_car();
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

		title.setVisibility(View.VISIBLE);

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansFallback.ttf"); // modifica as fontes

		// date
		date = (TextView) findViewById(R.id.date);
		// hour
		hour = (TextView) findViewById(R.id.hour);

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

		changeFormatEditText(); // aplica o formato para apenas visualização

		// edit ?
		Log.i(TAG, "Recupera os dados para a visualização do itemRequest...");
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

		if (itemRequest == null) {

			Toast.makeText(this,
					"Dados do itemRequest não encontrados na base.",
					Toast.LENGTH_SHORT).show();

			finish(); // fecha a view em caso de erro

		}

		try {

			// formata date
			String dateFromBase = itemRequest.getDate();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dateFromBase.length(); i++) {

				if (dateFromBase.charAt(i) == '-') { // insere valor da
														// data

					Log.i(TAG, "date [" + sb.toString() + "]");

					date.setText(sb.toString());

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
				subject.setFocusable(false);
			}

			// value u
			if (type == FUEL) {
				value_u.setText(String.valueOf((itemRequest.getValue_u())));
				value_u.setFocusable(false);
			}

			// value p
			if (type == EXPENSE || type == REPAIR || type == FUEL) {
				value_p.setText(String.valueOf((itemRequest.getValue_p())));
				value_p.setFocusable(false);
			}

			// text
			if (type == NOTE) {
				text.setText(String.valueOf((itemRequest.getText())));
				text.setFocusable(false);
			}

			// odemeter
			if (type == FUEL) {
				odometer.setText(String.valueOf((itemRequest.getOdometer())));
				odometer.setFocusable(false);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();

			Log.e(TAG, itemRequest.toString());
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

		// volta para a lista
		// Intent it = new Intent(this, ListItemScreen.class);
		// startActivityForResult(it, RESULT_OK);
		// startActivity(it);

		finish();
	}

	/**
	 * Edita item
	 */
	public void btBarRight(final View v) {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		it.putExtra("task", "edit");

		// id do itemRequest
		it.putExtra(ItemLog._ID, id_item);

		// Passa também o nome do carro para ser usado no titulo
		it.putExtra(Carro.NOME, name_car);

		// Abre a tela de edição
		// startActivityForResult(it, INSERIR_EDITAR);
		startActivity(it);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	public void onBackPressed() {

		setResult(RESULT_OK);

		super.onBackPressed();

	}

}
