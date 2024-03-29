package br.com.maboo.neext.screens;

import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.FormItemActivity;
import br.com.maboo.neext.interfaces.InterfaceBar;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.util.AndroidUtils;
import br.com.maboo.neext.util.Constants;
import br.com.maboo.neext.util.EditTextTools;

public class ViewItemScreen extends FormItemActivity implements InterfaceBar {

	private final String TAG = Constants.LOG_APP;

	// Campos texto
	private LinearLayout bg_title;
	private TextView type_edit;
	private TextView day_date;
	private TextView month_date;
	private TextView hour;
	private EditText subject;
	private EditText text;
	private String typeColor = "";
	private boolean check = false;

	private static Long id_item = null;

	// itemRequest na tela
	private ItemNote itemRequest;

	Vector<EditText> vEditText; // vetor de editText
	
	// color default para texto
	private int defaultColor = Constants.defaultColor;

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

		// Log.i(TAG, "view itemRequest screen");

		if (itemRequest == null) { //se n�o estiver null, significa que � um refresh

			final Bundle extras = getIntent().getExtras();

			if (extras != null) { 

				id_item = extras.getLong(ItemNote._ID);

				if (id_item != null) { // searching itemRequest

					if (id_item == -999) { // verifica se � pra fazer o
											// searching
											// pelo ultimo registro inserido
						// Log.i(TAG, "searching itemRequest [" + -999 + "]");

						itemRequest = buscarLastItemNote();

						typeColor = itemRequest.getType();

						// Log.i(TAG, "Color of item [" +
						// itemRequest.getSubject()+
						// "] � [" + typeColor + "]");

					} else {

					//	Log.i(TAG, "searching itemRequest [" + id_item + "]");

						itemRequest = buscarItemNote(id_item);

						typeColor = itemRequest.getType(); // coloco a cor
															// default

						check = itemRequest.isCheck(); // verifica se o item
														// esta
														// check ou uncheck

					//	Log.i(TAG, "Color of item [" + itemRequest.getSubject() + "] � [" + typeColor + "]");

					}

				} else {

					finish();

				}

			}

			setContentView(R.layout.form_note);

			Typeface tf = Typeface.createFromAsset(getAssets(),
					"fonts/DroidSansFallback.ttf"); // modifica as fontes

			// change background of item	
			String parseColor = "";
			if(typeColor.toString().charAt(0) != '#') { // verifica se j� possui o #
				parseColor = "#" + typeColor.toString();
			} else {
				parseColor = typeColor.toString();
			}
			
			int color = Color.parseColor(parseColor);

			// change background title (barra superior)
			bg_title = (LinearLayout) findViewById(R.id.bg_title);
			bg_title.setBackgroundColor(color);
			
			//type edit 
			type_edit = (TextView) findViewById(R.id.type_edit);
			type_edit.setTextColor(defaultColor);
			
			// date
			day_date = (TextView) findViewById(R.id.day_date);
			//day_date.setTextColor(defaultColor);
			
			// month
			month_date = (TextView) findViewById(R.id.month_date);
			//month_date.setTextColor(defaultColor);

			// hour
			hour = (TextView) findViewById(R.id.hour);
			//hour.setTextColor(defaultColor);

			// subject
			subject = (EditText) findViewById(R.id.subject);
			vEditText.add(subject);

			// text
			text = (EditText) findViewById(R.id.text);
			vEditText.add(text);
			
			// tela check ou uncheck
			//bg_check = (LinearLayout) findViewById(R.id.bg_check);

			EditTextTools.insertFontInAllFields(vEditText, tf); // change font
																// editText

			changeFormatEditText(); // aplica o formato para apenas visualiza��o

		} else {
			
			itemRequest = buscarItemNote(itemRequest.getId()); // test
			
			id_item = itemRequest.getId();
			
			typeColor = itemRequest.getType(); // coloco a cor
			// default
			
			check = itemRequest.isCheck(); // verifica se o item
			// esta
			// check ou uncheck
		}

		// edit ?
		// Log.i(TAG,
		// "Recupera os dados para a visualiza��o do itemRequest...");
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
		// Log.i(TAG, "refresh view...");

		// atualiza a view
		init();
	}

	// open screen with datas of object
	public void loadingDataItem() {

		if (itemRequest == null) {

			Toast.makeText(this,
					"Dados do itemRequest n�o encontrados na base.",
					Toast.LENGTH_SHORT).show();

			finish(); // fecha a view em caso de erro

		}

		try {

			// formata date
			// formata a data
			// formata date
			String dateFromBase = itemRequest.getDate();

			StringBuffer sb = new StringBuffer();
			String date_full = "";
			int day = 0;
			int month = 0;
			int year = 0;
			for (int i = 0; i < dateFromBase.length(); i++) {

				if (dateFromBase.charAt(i) == '-') { // insere valor da
														// data
					date_full = sb.toString();
					sb = new StringBuffer();
					i++;

					// insere valor da hora o numero dessa linha � comparado a 16,
					// pois esse � o tamanho maximo
					// correto de uma data, de acordo com a inser��o dela
					// 'dd/mm/aaaa - hh:mm'
				} else if (i == 15) {
					sb.append(dateFromBase.charAt(i));

					hour.setText(sb.toString()); // hora
					break;

				}

				sb.append(dateFromBase.charAt(i));
			}
			
			/** aplicando data por extenso	**/			
			day = Integer.valueOf(date_full.substring(0, 2)).intValue(); // get only day
			month = Integer.valueOf(date_full.substring(3, 5)).intValue(); // get only month - 04/12/2013
			year = Integer.valueOf(date_full.substring(6, 10)).intValue(); // get only month - 04/12/2013
			
			day_date.setText(""+day);	
			
			// seleciona primeiras 3 letras do mes
			//month_date.setText(AndroidUtils.getMonth(month).substring(0, 3).toLowerCase()+"/"+year);
			month_date.setText(AndroidUtils.getMonth(month)+"/"+year);
			
			//TODO cabecalho retirado at� acertas das datas
			// escreve no cabe�alho qnd foi a ultima edi��o
			//type_edit.setText(TextViewTools.getLastEdit(hour.getText().toString(), date.getText().toString(), this));
			type_edit.setText(R.string.view);

			// subject
			subject.setText(String.valueOf((itemRequest.getSubject())));
			subject.setTextColor(Color.parseColor("#"
					+ "ffffff"));
			subject.setFocusable(false);

			// text
			text.setText(String.valueOf((itemRequest.getText())));
			text.setFocusable(false);

			// screen check ou uncheck
			if (check) {
				subject.setPaintFlags(subject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				subject.setPaintFlags(subject.getPaintFlags()
		                & ~Paint.STRIKE_THRU_TEXT_FLAG);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();

		//	Log.e(TAG, itemRequest.toString());
		}

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	// Buscar o itemLog pelo id_item
	protected ItemNote buscarItemNote(final long id) {
		try {
			return ListScreen.dao.buscarItemNote(id);
		} catch (SQLException e) {

			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

			e.printStackTrace();

			return null;
		}
	}

	protected ItemNote buscarLastItemNote() {
		try {
			return ListScreen.dao.buscarLastItemNote();
		} catch (SQLException e) {

			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

			e.printStackTrace();

			return null;
		}
	}

	// pede pro usuario a confirma��o para deletar realmente o item
	public void deleteConConfirm() {
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage(R.string.a_dt_item);
		// M�todo executado se escolher Sim
		alerta.setPositiveButton(R.string.a_y,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						deleteItem();
					}
				});
		// M�todo executado se escolher N�o
		alerta.setNegativeButton(R.string.a_n,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//
					}
				});
		// Exibe o alerta de confirma��o
		alerta.show();
	}

	// delete car
	public void deleteItem() {
		if (id_item != null) {
			excluirItem(id_item);

			// OK
			setResult(RESULT_OK);

			// Cria a intent para abrir a tela de editar
			Intent it = new Intent(this, ListScreen.class);

			// Abre a tela de edi��o
			startActivityForResult(it, RESULT_OK);
		}

	}

	// passe o id do item que ser� excluido
	protected void excluirItem(long id) {
		try {
			ListScreen.dao.deletar(id);
		} catch (SQLException e) {

			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

			e.printStackTrace();
		}
	}
	
	
	// compartilhar a nota
	private void shareIt(ItemNote itemNote) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

		sharingIntent.setType("text/plain");

		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				itemNote.getSubject());
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				itemNote.getText());

		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/

	public void actionBt(final Context context) {

	}

	public void organizeBt() {
		
		// insere a imagem no bt central
		ImageView btLeft = (ImageView) findViewById(R.id.bt_left_down);
		btLeft.setImageDrawable(getResources().getDrawable(R.drawable.bt_delete_off));
		
		// insere a imagem no bt central
		ImageView btCenter = (ImageView) findViewById(R.id.bt_center_down);
		btCenter.setImageDrawable(getResources().getDrawable(R.drawable.bt_check_off));
		
		// insere a imagem no bt central
		ImageView btRight = (ImageView) findViewById(R.id.bt_right_down);
		btRight.setImageDrawable(getResources().getDrawable(R.drawable.bt_share_off));

	}

	public void onBackPressed() {
		super.onBackPressed();
		
		overridePendingTransition(R.anim.scale_in, R.anim.anime_slide_to_right);

	}
	
	/******************************************************************************
	 * BUTTONS
	 ******************************************************************************/
	public void btBarUpLeft(View v) {
		// TODO Auto-generated method stub
		
	}

	public void btBarUpRight(View v) {
		
		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// id do itemRequest
		it.putExtra(ItemNote._ID, id_item);

		// typeColor os task
		int T_KEY = Constants.EDITAR;
		it.putExtra("T_KEY", T_KEY);

		// Abre a tela de edi��o
		startActivityForResult(it, T_KEY);

		finish();

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
		
	}

	public void btBarDownLeft(View v) {
		
		// delet item e retorna para a lista de itens
		deleteConConfirm();
		
	}

	public void btBarDownRight(View v) {
		
		// compartilha o item
		shareIt(itemRequest);
		
	}
	
	public void btBarDownCenter(View v) {
		
		// check item and update page
		checkOrUncheckItem();
		
	}

	/******************************************************************************
	 * CHECKED AND UNCHECKED
	 ******************************************************************************/
	private void checkOrUncheckItem() {

		itemRequest.setCheck(!itemRequest.isCheck());

		if (itemRequest != null) { // � uma atualiza��o (pra n�o ter erro)
			itemRequest.setId(itemRequest.getId());
			
			try {
				ListScreen.dao.atualizar(itemRequest); // atualiza item
				
				if(itemRequest.isCheck()) {					
					Toast.makeText(this, "check!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "uncheck!", Toast.LENGTH_SHORT).show();
				}
				
			} catch (SQLException e) {

				// erro caricato
				AndroidUtils
						.alertDialog(this,
								"Sorry, please... soooorry. And now, re-start the app.");

				e.printStackTrace();
			}
		}

		init(); // atualiza a view

	}
}
