package br.com.maboo.neext.screens;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.FormItemActivity;
import br.com.maboo.neext.interfaces.InterfaceBar;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.util.Constants;
import br.com.maboo.neext.util.EditTextTools;
import br.com.maboo.neext.util.TextViewTools;

public class ViewItemScreen extends FormItemActivity implements InterfaceBar {

	private final String TAG = Constants.LOG_APP;

	// Campos texto
	private TextView date;
	private TextView hour;
	private EditText subject;
	private EditText text;

	private static Long id_item;

	private static int type;

	protected static final int INSERIR_EDITAR = 1;

	// itemRequest na tela
	private ItemNote itemRequest;

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

			id_item = extras.getLong(ItemNote._ID);

			if (id_item != null) { // searching itemRequest

				Log.i(TAG, "searching itemRequest [" + id_item + "]");

				itemRequest = buscarItemNote(id_item);

			} else {

				finish();

			}

			type = itemRequest.getType();

		}

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansFallback.ttf"); // modifica as fontes

		// date
		date = (TextView) findViewById(R.id.date);
		// hour
		hour = (TextView) findViewById(R.id.hour);

		// subject

		tv = (TextView) findViewById(R.id.t_subject);
		vTextView.add(tv);

		subject = (EditText) findViewById(R.id.subject);

		vEditText.add(subject);

		tv = (TextView) findViewById(R.id.t_text);
		vTextView.add(tv);

		text = (EditText) findViewById(R.id.text);

		vEditText.add(text);

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

			subject.setText(String.valueOf((itemRequest.getSubject())));
			subject.setFocusable(false);

			text.setText(String.valueOf((itemRequest.getText())));
			text.setFocusable(false);

		} catch (NullPointerException e) {
			e.printStackTrace();

			Log.e(TAG, itemRequest.toString());
		}

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	// Buscar o itemLog pelo id_item
	protected ItemNote buscarItemNote(final long id) {
		return ListScreen.dao.buscarItemNote(id);
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

		setResult(RESULT_OK);

		// Fecha a tela
		finish();

	}

	public void btBarRight(final View v) {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		it.putExtra("task", "edit");

		// id do itemRequest
		it.putExtra(ItemNote._ID, id_item);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	public void onBackPressed() { // call my backbutton pressed method when

		Log.i("appLog", "Clicked back");

	}

}
