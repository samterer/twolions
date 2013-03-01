package br.com.maboo.neext.screens;

import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import br.com.maboo.neext.util.Constants;
import br.com.maboo.neext.util.EditTextTools;
import br.com.maboo.neext.util.TextViewTools;

public class ViewItemScreen extends FormItemActivity implements InterfaceBar {

	private final String TAG = Constants.LOG_APP;

	// Campos texto
	private LinearLayout bg_title;
	private TextView type_edit;
	private TextView date;
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

		if (itemRequest == null) { //se não estiver null, significa que é um refresh

			final Bundle extras = getIntent().getExtras();

			if (extras != null) { 

				id_item = extras.getLong(ItemNote._ID);

				if (id_item != null) { // searching itemRequest

					if (id_item == -999) { // verifica se é pra fazer o
											// searching
											// pelo ultimo registro inserido
						// Log.i(TAG, "searching itemRequest [" + -999 + "]");

						itemRequest = buscarLastItemNote();

						typeColor = itemRequest.getType();

						// Log.i(TAG, "Color of item [" +
						// itemRequest.getSubject()+
						// "] é [" + typeColor + "]");

					} else {

						Log.i(TAG, "searching itemRequest [" + id_item + "]");

						itemRequest = buscarItemNote(id_item);

						typeColor = itemRequest.getType(); // coloco a cor
															// default

						check = itemRequest.isCheck(); // verifica se o item
														// esta
														// check ou uncheck

						Log.i(TAG, "Color of item [" + itemRequest.getSubject()
								+ "] é [" + typeColor + "]");

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
			if(typeColor.toString().charAt(0) != '#') { // verifica se já possui o #
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
			date = (TextView) findViewById(R.id.date);
			date.setTextColor(defaultColor);

			// hour
			hour = (TextView) findViewById(R.id.hour);
			hour.setTextColor(defaultColor);

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

			changeFormatEditText(); // aplica o formato para apenas visualização

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
		// "Recupera os dados para a visualização do itemRequest...");
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

					// Log.i(TAG, "date [" + sb.toString() + "]");

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

					// Log.i(TAG, "hour [" + sb.toString() + "]");

					hour.setText(sb.toString()); // hora

					break;

				}

				//Log.i(TAG, "insert [" + dateFromBase.charAt(i) + "]");

				sb.append(dateFromBase.charAt(i));
			}
			
			// escreve no cabeçalho qnd foi a ultima edição
			type_edit.setText(TextViewTools.getLastEdit(hour.getText()
					.toString(), date.getText().toString(), this));
			//type_edit.setText("");

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

	protected ItemNote buscarLastItemNote() {
		return ListScreen.dao.buscarLastItemNote();
	}

	// pede pro usuario a confirmação para deletar realmente o item
	public void deleteConConfirm() {
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage(R.string.a_dt_item);
		// Método executado se escolher Sim
		alerta.setPositiveButton(R.string.a_y,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						deleteItem();
					}
				});
		// Método executado se escolher Não
		alerta.setNegativeButton(R.string.a_n,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//
					}
				});
		// Exibe o alerta de confirmação
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

			// Abre a tela de edição
			startActivityForResult(it, RESULT_OK);
		}

	}

	// passe o id do item que será excluido
	protected void excluirItem(long id) {
		ListScreen.dao.deletar(id);
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

		// Abre a tela de edição
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
		
		// if(itemRequest.isCheck() == true) {		
		//	StringBuffer text = new StringBuffer(); // corpo do note
		//	for (int i = 0; i < itemRequest.getText().length(); i++) { // sinopse do corpo da notificação
		//		text.append(itemRequest.getText().charAt(i));	
		//	}

			// cria notificação
		//	NotificationCreate nc = new NotificationCreate(
		//			getApplicationContext(), "New note was marked.",
		//			itemRequest.getSubject(), text);
		//	nc.criarNotificacao(NotificationViewer.class);
		// }

		if (itemRequest != null) { // É uma atualização (pra não ter erro)
			itemRequest.setId(itemRequest.getId());
			
			ListScreen.dao.atualizar(itemRequest); // atualiza item
		}

		

		init(); // atualiza a view

	}
}
