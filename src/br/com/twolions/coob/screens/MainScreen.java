package br.com.twolions.coob.screens;

import java.util.Vector;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import br.com.twolions.coob.R;
import br.com.twolions.coob.core.ActivityCircle;
import br.com.twolions.coob.interfaces.InterfaceBar;
import br.com.twolions.coob.util.HexValidator;

public class MainScreen extends ActivityCircle implements InterfaceBar {

	Vector<EditText> vEditText;

	Vector<LinearLayout> vLinearLayout;

	HexValidator hex;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();

		organizeBt();

		listenerText();

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

	private void setBackground(int index, String color) {

		LinearLayout ll = (LinearLayout) vLinearLayout.elementAt(index);

		Log.i("main", "pintando background [" + index + "] com a cor [" + color
				+ "]");

		try {
			ll.setBackgroundColor(Color.parseColor(color));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************************************************************
	 * TOUCH
	 ****************************************************************/

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

							if (s.length() > 0) { // insere o sharp caso ele não
													// existe no campo
								if (s.charAt(0) == '#') {
									//
								} else {
									e.setText("#" + e.getText().toString());
								}
							}

						}

						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {

						}

						public void afterTextChanged(Editable s) {
							try {

								if (hex.validate(s.toString())) {

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
													.intValue(), s.toString());

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
