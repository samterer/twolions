package br.com.twolions.coob.screens;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.twolions.coob.R;
import br.com.twolions.coob.core.ActivityCircle;
import br.com.twolions.coob.interfaces.InterfaceBar;
import br.com.twolions.coob.util.HexValidator;

public class MainScreen extends ActivityCircle implements InterfaceBar {

	private Vector<EditText> vEditText;

	private Vector<LinearLayout> vLinearLayout;

	private Vector<LinearLayout> vLinearLayoutHorizon;

	private Vector<TextView> vTextView;

	private int[] list_color;

	private String[] list_hex;

	HexValidator hex;

	private boolean isHelpExibido = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		list_color = new int[4]; // inicializa lista de cores por inteiro

		list_hex = new String[4]; // inicializa lista de cores em string

		// cores default
		for (int i = 0; i < list_color.length; i++) {
			list_color[i] = Color.parseColor("#cac8c8");
			// Log.i(TAG, "list_color[i] = " + list_color[i]);
			list_hex[i] = "cac8c8";
		}

		init();

		organizeBt();

		listenerText();

		// inicia a tela de ajuda
		initHelpView();

	}

	/****************************************************************
	 * SERVICES
	 ****************************************************************/

	private void init() {

		setContentView(R.layout.main_layout);

		// vec de linear layout
		vLinearLayout = new Vector<LinearLayout>();

		LinearLayout l_sup_esq = (LinearLayout) findViewById(R.id.l_sup_esq);
		vLinearLayout.add(l_sup_esq);

		LinearLayout l_sup_dir = (LinearLayout) findViewById(R.id.l_sup_dir);
		vLinearLayout.add(l_sup_dir);

		LinearLayout l_inf_esq = (LinearLayout) findViewById(R.id.l_inf_esq);
		vLinearLayout.add(l_inf_esq);

		LinearLayout l_inf_dir = (LinearLayout) findViewById(R.id.l_inf_dir);
		vLinearLayout.add(l_inf_dir);

		// vec de edi text
		vEditText = new Vector<EditText>();

		EditText ed_sup_esq = (EditText) findViewById(R.id.ed_sup_esq);
		vEditText.add(ed_sup_esq);

		EditText ed_sup_dir = (EditText) findViewById(R.id.ed_sup_dir);
		vEditText.add(ed_sup_dir);

		EditText ed_inf_esq = (EditText) findViewById(R.id.ed_inf_esq);
		vEditText.add(ed_inf_esq);

		EditText ed_inf_dir = (EditText) findViewById(R.id.ed_inf_dir);
		vEditText.add(ed_inf_dir);

		// exp para verificar os hex
		hex = new HexValidator();

		// TODO
		// l_inf_dir.setBackgroundColor(Color.parseColor("FFFFFF"));

	}

	// exibe a tela de help sobre a tela do app
	private void initHelpView() {

		if (!isHelpExibido) {
			isHelpExibido = true;

			LinearLayout img = (LinearLayout) findViewById(R.id.help);
			img.setVisibility(View.VISIBLE);

		}

	}

	// pint ao background q o usuario esta utilizando
	private void setBackground(int index, String color) {

		list_hex[index] = color.replace('#', ' ');

		list_color[index] = Color.parseColor(color);

		LinearLayout ll = (LinearLayout) vLinearLayout.elementAt(index);

		Log.i("main", "pintando background [" + index + "] com a cor [" + color
				+ "]");
		
		EditText ed = (EditText) vEditText.elementAt(index);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);

		try {
			ll.setBackgroundColor(Color.parseColor(color));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************************************************************
	 * ESTADO
	 ****************************************************************/

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Log.i("main",
				"O Estado da Tela foi Mudado: onConfigurationChanged(newConfig)");

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// prepara tela horizontal
			initHorizon();

			paintBackgroundHorizon();

		} else { // prepara tela vertical

			init();

			organizeBt();

			listenerText();

			// insere as cores que já existiam
			for (int i = 0; i < vLinearLayout.size(); i++) {
				LinearLayout ll = (LinearLayout) vLinearLayout.elementAt(i);

				ll.setBackgroundColor(list_color[i]);

			}
			// insere os antigos valores nos campos
			for (int i = 0; i < vEditText.size(); i++) {
				EditText e = (EditText) vEditText.elementAt(i);
				e.setText(list_hex[i]);

			}
		}

	}

	// inicializa as variaveis da tela na horizontal
	private void initHorizon() {

		setContentView(R.layout.main_horizon_layout);

		// vec de linear layout horizon
		vLinearLayoutHorizon = new Vector<LinearLayout>();

		LinearLayout l_esq = (LinearLayout) findViewById(R.id.l_esq);
		vLinearLayoutHorizon.add(l_esq);

		LinearLayout l_esq_2 = (LinearLayout) findViewById(R.id.l_esq_2);
		vLinearLayoutHorizon.add(l_esq_2);

		LinearLayout l_dir = (LinearLayout) findViewById(R.id.l_dir);
		vLinearLayoutHorizon.add(l_dir);

		LinearLayout l_dir_2 = (LinearLayout) findViewById(R.id.l_dir_2);
		vLinearLayoutHorizon.add(l_dir_2);

		// vec de text view
		vTextView = new Vector<TextView>();

		TextView t_esq = (TextView) findViewById(R.id.t_esq);
		vTextView.add(t_esq);

		TextView t_esq_2 = (TextView) findViewById(R.id.t_esq_2);
		vTextView.add(t_esq_2);

		TextView t_dir = (TextView) findViewById(R.id.t_dir);
		vTextView.add(t_dir);

		TextView t_dir_2 = (TextView) findViewById(R.id.t_dir_2);
		vTextView.add(t_dir_2);

	}

	// pinta os fundos com a tela na horizontal
	private void paintBackgroundHorizon() {

		for (int i = 0; i < vLinearLayoutHorizon.size(); i++) {

			LinearLayout ll_horizon = (LinearLayout) vLinearLayoutHorizon
					.elementAt(i);

			// Log.i("main", "id of ll [" + ll_horizon.getTag().toString() +
			// "]");

			// Log.i("main", "color q sera aplicada [" + list_color[i] + "]");

			ll_horizon.setBackgroundColor(list_color[i]);

			TextView t = (TextView) vTextView.elementAt(i);
			t.setText('#' + list_hex[i]);
		}

	}

	/****************************************************************
	 * TOUCH
	 ****************************************************************/
	
	public boolean dispatchTouchEvent(MotionEvent event) {
	    View v = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);
	    View w = getCurrentFocus();
	    int scrcoords[] = new int[2];
	    w.getLocationOnScreen(scrcoords);
	    float x = event.getRawX() + w.getLeft() - scrcoords[0];
	    float y = event.getRawY() + w.getTop() - scrcoords[1];

	    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
	    if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 
	    	imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	    }
	    return ret;

	}

	public void organizeBt() {

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_about);

	}

	public void btBarLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarRight(View v) {

		startActivity(new Intent(this, AboutScreen.class));

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

	}

	public void hideHelp(View v) {
		LinearLayout img = (LinearLayout) findViewById(R.id.help);
		img.setVisibility(View.INVISIBLE);
	}

	/****************************************************************
	 * EDIT TEXT
	 ****************************************************************/

	public void listenerText() {

		for (int i = 0; i < vEditText.size(); i++) {
			final EditText e = (EditText) vEditText.elementAt(i);

			if (e != null) { // aplica regra de decimal

				if (e.isFocusable()) {

					e.addTextChangedListener(new TextWatcher() {

						public void onTextChanged(CharSequence s, int start,
								int before, int count) {

							// if (s.length() > 0) { // insere o sharp caso ele
							// não
							// // existe no campo
							// if (s.charAt(0) == '#') {
							// //
							// } else {
							//
							// e.toString().replace('#', ' ');
							//
							// e.setText("#" + e.getText().toString());
							//
							// }
							//
							// }
						}

						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {

						}

						public void afterTextChanged(Editable s) {

							StringBuffer text = new StringBuffer(s.length());
							text.append(s.toString());

							if (text.length() > 5) {
								Log.i("main", "insert #");
								// insere o #
								text.insert(0, "#");
								Log.i("main", "s [" + text.toString() + "]");
							}

							Log.i("main", "s [" + text.toString() + "]");

							try {

								if (hex.validate(text.toString())) {

									Log.i("main",
											"o text no item ["
													+ Integer
															.valueOf(
																	e.getTag()
																			.toString())
															.intValue()
													+ "] é hex.");

									setBackground(
											Integer.valueOf(
													e.getTag().toString())
													.intValue(), text
													.toString());

									return;

								} else {

									Log.i("main",
											"o text no item ["
													+ Integer
															.valueOf(
																	e.getTag()
																			.toString())
															.intValue()
													+ "] NÃO é hex.");

									return;
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
				}
			}

		}

	}
}
