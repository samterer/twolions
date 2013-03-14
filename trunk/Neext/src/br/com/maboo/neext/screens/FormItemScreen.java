package br.com.maboo.neext.screens;

import java.util.Calendar;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.maboo.neext.R;
import br.com.maboo.neext.core.FormItemActivity;
import br.com.maboo.neext.interfaces.InterfaceBar;
import br.com.maboo.neext.model.ItemModel;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.util.AndroidUtils;
import br.com.maboo.neext.util.Constants;
import br.com.maboo.neext.util.EditTextTools;

/**
 * Activity que utiliza o TableLayout para editar o itemLog
 * 
 * 
 */
public class FormItemScreen extends FormItemActivity implements InterfaceBar {

	private final String TAG = Constants.LOG_APP;

	// Campos texto
	private LinearLayout bg_title;
	private TextView date;
	private TextView hour;
	private TextView title_edit;
	private EditText subject;
	private EditText text;
	private String typeColor = "";
	private boolean check = false;
	private ImageView bt_right_up;

	private static Long id_item;

	// itemRequest na tela
	private ItemNote itemRequest;
	
	// color default para texto
	private int defaultColor = Constants.defaultColor;

	// hour
	private int hour_time;
	private int min_time;
	// date
	private int day_time;
	private int month_time;
	private int year_time;

	Vector<EditText> vEditText; // vetor de editText

	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		init();

		organizeBt();

		actionBt(this);

		listenerText();

		//addListenerOnButton();
	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	private void init() {

		setContentView(R.layout.form_note);

		// subtitulo já prédefinido
		String subj = "";

		vEditText = new Vector<EditText>();

		final Bundle extras = getIntent().getExtras();

		if (extras != null) { // Se for para Editar, recuperar os valores ...

			int task = extras.getInt("T_KEY");

			if (task == Constants.INSERIR) { // cria novo itemRequest

				// verifica se o usuario já passou um subject
				id_item = null;
				if (extras.getString("subj") != null) {
					subj = extras.getString("subj");
				}

				// recebe a color definida pelo usuario
				if (extras.getString("color") != null) {
					typeColor = extras.getString("color");
				}

			} else if (task == Constants.EDITAR) { // edit itemRequest

				id_item = extras.getLong(ItemNote._ID);

				//Log.i(TAG, "searching itemRequest [" + id_item + "]");
				try {					
					itemRequest = ItemModel.buscarItemNote(id_item); // busca
					// informações
					// do
				} catch (SQLException e) {
					
					// erro caricato
					AndroidUtils.alertDialog(this,
							"Sorry, please... soooorry. And now, re-start the app.");
					
					e.printStackTrace();
				}
				
			//	try { // se retornar nulo é pq foi o ultimo valor inserido
					
			//	}
				typeColor = itemRequest.getType(); // coloco a cor do item (pq ela já existe no banco)
				
				
				check = itemRequest.isCheck(); // verifica se o item esta check ou uncheck
				
			}

		}

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/DroidSansFallback.ttf"); // modifica as fontes

		// calendar
		final Calendar c = Calendar.getInstance();
		
		
		//color type
		if(typeColor.toString().charAt(0) != '#') {
			typeColor = "#" + typeColor;
		}

		// color of item
		int color = Color.parseColor(typeColor);

		// title
		bg_title = (LinearLayout) findViewById(R.id.bg_title);
		bg_title.setBackgroundColor(color);

		// bt edit color
		bt_right_up = (ImageView) findViewById(R.id.bt_right_up);
		bt_right_up.setTag(typeColor);
		
		//type edit
		title_edit = (TextView) findViewById(R.id.type_edit);

		// date
		date = (TextView) findViewById(R.id.date);
		day_time = c.get(Calendar.DAY_OF_MONTH);
		month_time = c.get(Calendar.MONTH) + 1; // para que não aja o mês 00, muito obrigado java
		year_time = c.get(Calendar.YEAR);

		date.setText(new StringBuilder().append(pad(day_time)).append("/")
				.append(pad(month_time)).append("/").append(pad(year_time)));
		date.setTextColor(defaultColor);

		// hour
		hour = (TextView) findViewById(R.id.hour);
		hour_time = c.get(Calendar.HOUR_OF_DAY);
		min_time = c.get(Calendar.MINUTE);

		hour.setText(new StringBuilder().append(pad(hour_time)).append(":")
				.append(pad(min_time)));
		hour.setTextColor(defaultColor);

		// subject
		subject = (EditText) findViewById(R.id.subject);

		// insert subject if id_item = null;
		if (id_item == null) {
			subject.append(subj);
		}

		vEditText.add(subject);

		// text
		text = (EditText) findViewById(R.id.text);

		vEditText.add(text);
		
		// muda o button de edit por um bt de cor
		bt_right_up.setImageResource(R.drawable.paint);
		bt_right_up.setVisibility(View.VISIBLE);

		EditTextTools.insertFontInAllFields(vEditText, tf); // change font
															// editText
		if (itemRequest != null) { // edit itemRequest?

			//Log.i(TAG, "Edição de itemRequest...");

			loadingEdit();

		} else {// create a new itemRequest?

			//Log.i(TAG, "Criação de itemRequest...");

		}

	}

	// open screen with datas of object
	public void loadingEdit() {

		Log.i(TAG, "Data for edit");
		Log.i(TAG, itemRequest.toString());
		// }

		try {
			
			//color type
			if(itemRequest.getType().toString().charAt(0) != '#') {
				itemRequest.setType("#" + itemRequest.getType());
			}

			// color of item
			int color = Color
					.parseColor(itemRequest.getType().toString());
			
			typeColor = itemRequest.getType().toString();
			
			// escreve no cabeçalho que esta em mode editing
			title_edit.setText(R.string.m_edit);
			title_edit.setTextColor(defaultColor);

			// change background title (barra superior)
			bg_title.setBackgroundColor(color);
			
			/*// formata date
			String dateFromBase = itemRequest.getDate();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < dateFromBase.length(); i++) {

				if (dateFromBase.charAt(i) == '-') { // insere valor da
														// data

					// Log.i(TAG, "date [" + sb.toString() + "]");

					date.setText(sb.toString());
					date.setTextColor(defaultColor);

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

					// Log.i(TAG, "hour [" + sb.toString() + "]");

					hour.setText(sb.toString()); // hora
					hour.setTextColor(defaultColor);

					break;

				}

				//Log.i(TAG, "insert [" + dateFromBase.charAt(i) + "]");

				sb.append(dateFromBase.charAt(i));
			}*/

			// subject
			subject.setText(String.valueOf((itemRequest.getSubject())));

			// text
			text.setText(String.valueOf((itemRequest.getText())));
			
			//screen check ou uncheck
			if(check) { // checka o item
				// cria a linha no meio do item
				subject.setPaintFlags(subject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			} else { // tira o check do item
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

	public void salvar() {

		if (subject.getText().toString().length() < 1) { // se o campo subjet estiver fazio nao salva
			
			Intent it = new Intent(this, ListScreen.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // mata a pilha de activitys 
			// OK
			startActivity(it);

			overridePendingTransition(R.anim.scale_in, R.anim.anime_slide_to_right);
			
			return;
		}
		
		if (id_item != null) { // atualização			
			updateItem(); // atualiza item			
		} else {			
			saveNewItem(); // cria novo item no banco		
		}

	}
	
	/**
	 * Salva item recem criado
	 */
	private void saveNewItem() {
		ItemNote i4s = new ItemNote();

		// typeColor
		i4s.setType(typeColor.toString());

		// hour and date
		// get date for save
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date.getText().toString() + "-" + hour.getText());

		i4s.setDate(sbDate.toString());

		// subject
		i4s.setSubject(subject.getText().toString());

		// text
		i4s.setText(text.getText().toString());

		// check ou uncheck
		i4s.setCheck(check);
		
		try {
			id_item = ItemModel.salvarItemNote(i4s);			
		} catch (SQLException e) {
			
			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");
			
			e.printStackTrace();
		}
		
		Toast.makeText(this, R.string.m_save, Toast.LENGTH_SHORT).show();

		backToViewItemScreen();
	}

	/**
	 * Atualiza item
	 */
	private void updateItem() {
		int cont = 0; // se esse valor for maior que zero o item sera
						// atualizado\criado
		ItemNote i4s = new ItemNote();

		// É uma atualização
		i4s.setId(id_item);

		// recupera o i4s do dado

		// color
		if (!itemRequest.getType().equals(typeColor.toString())) {
			cont++;
		}
		// subject
		if (!itemRequest.getSubject().equals(subject.getText().toString())) {
			cont++;
		}
		// text
		if (!itemRequest.getText().equals(text.getText().toString())) {
			cont++;
		}
		// check
		if (!itemRequest.isCheck() == check) {
			cont++;
		}

		// typeColor
		i4s.setType(typeColor.toString());

		// hour and date
		// get date for save
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date.getText().toString() + "-" + hour.getText());
		
		i4s.setDate(sbDate.toString());

		// subject
		i4s.setSubject(subject.getText().toString());

		// text
		i4s.setText(text.getText().toString());

		// check ou uncheck
		i4s.setCheck(check);
		
		// Salvar		
		if(cont > 0) { // confirma se o item vai realmente ser atualizado (significa que o user mudou parametros na tela)
			//Log.i(TAG, "save [" + itemLog4Save.toString() + "]");
			try {
				id_item = ItemModel.salvarItemNote(i4s);	// no caso de ser a primeira inserção já devolve o id do novo item
			} catch (SQLException e) {

				// erro caricato
				AndroidUtils
						.alertDialog(this,
								"Sorry, please... soooorry. And now, re-start the app.");

				e.printStackTrace();
			}
			
			Toast.makeText(this, R.string.m_save, Toast.LENGTH_SHORT).show();
			
		}

		backToViewItemScreen();
	}
	
	/**
	 * Retorna pra tela de view item
	 * @param c
	 * @return
	 */
	private void backToViewItemScreen() {
		Intent it = new Intent(this, ViewItemScreen.class);

		// id do item
		it.putExtra(ItemNote._ID, id_item);

		// OK
		startActivity(it);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	/******************************************************************************
	 * BUTTONS
	 ******************************************************************************/
	public void actionBt(final Context context) {

	}
	
	public void organizeBt() {
		
		// insere a imagem no bt central
		ImageView bt = (ImageView) findViewById(R.id.bt_center_down);
		bt.setImageDrawable(getResources().getDrawable(R.drawable.bt_save_off));
		
		// insere a imagem no bt right
		ImageView bt1 = (ImageView) findViewById(R.id.bt_right_down);
		bt1.setImageDrawable(getResources().getDrawable(R.drawable.bt_cancel_off));

	}

	public void onBackPressed() { // call my backbutton pressed method when

		salvar();

		super.onBackPressed(); // boolean==true

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_HOME)) {
	        //do nothing
	        return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void btBarUpLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarUpRight(View v) {
		
		if (customMenuDialog == null) { // instancia o menu apenas uma vez
			customMenuDialog = new MenuDialog(this);
		}

		if (!customMenuDialog.isShowing()) {
			customMenuDialog.show();
		}

	}

	public void btBarDownLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarDownRight(View v) {
		
		finish();
		

	}
	
	public void btBarDownCenter(View v) {

		onBackPressed();
		
	}

	/****************************************************************
	 * EDIT ITEM_TEXT
	 ****************************************************************/

	public void listenerText() {

		// verifica se existe um subject, se não existir ele insere o texto do
		// text no subject
		// para que o subject nunca fique vazio

		EditText e = text;

		if (e != null) {

			if (e.isFocusable()) {

				e.addTextChangedListener(new TextWatcher() {

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					public void afterTextChanged(Editable s) {

						if (subject.toString() == "" || subject.length() == 0) {

							StringBuffer text = new StringBuffer();
							text.append(s.toString());

							subject.append(text);
						}

					}
				});
			}
		}

	}
	
	/****************************************************************
	 * CHANGE COLOR
	 ****************************************************************/
	
	public void changeToColor(String color) {
		
		//Toast.makeText(this, "Change to color [#"+color+"]", Toast.LENGTH_SHORT).show();
		
		// muda cor do item
		typeColor = color;
		
		// muda cor da tela
		// color of item
		int newcolor = Color
				.parseColor("#" + typeColor);
		
		title_edit.setTextColor(defaultColor);
		
		bg_title.setBackgroundColor(newcolor);
		
		date.setTextColor(defaultColor);
		
		hour.setTextColor(defaultColor);
		
		// closed menu for select item
		customMenuDialog.dismiss();
		
	}
	private MenuDialog customMenuDialog;

	private class MenuDialog extends AlertDialog {
		public MenuDialog(Context context) {
			super(context);

			View cus_menu = getLayoutInflater().inflate(R.layout.custom_menu,
					null);

			setView(cus_menu);

		}
		
		// lista de cores
		// 1 - laranja FFA500
		// 2 - azul claro 45c4ff
		// 3 - verde claro 39bf2b
		// 4 - roxo a6a6ed
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			LinearLayout layout_menu = (LinearLayout) findViewById(R.id.layout_menu);

			for (int i = 0; i < 4; i++) {
				final ImageView imgV = (ImageView) layout_menu.getChildAt(i);
				imgV.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

							changeToColor(imgV.getTag().toString().substring(1, imgV.getTag().toString().length()));	

					}

				});

			}

		}

		/**
		 * Verifica onde foi o clique do usuario, se foi no menu de item (ok),
		 * se não (fecha o menu)
		 */
		public boolean onTouchEvent(MotionEvent event) {

			// I only care if the event is an UP action
			if (event.getAction() == MotionEvent.ACTION_UP) {

				// create a rect for storing the window rect
				Rect r = new Rect(0, 0, 0, 0);

				// retrieve the windows rect
				this.getWindow().getDecorView().getHitRect(r);

				// check if the event position is inside the window rect
				boolean intersects = r.contains((int) event.getX(),
						(int) event.getY());

				// if the event is not inside then we can close the activity
				if (!intersects) {

					// close the activity
					customMenuDialog.dismiss();
					
					InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

					// notify that we consumed this event
					return true;
				}
			}
			// let the system handle the event
			return super.onTouchEvent(event);
		}
		
		

	}
}
