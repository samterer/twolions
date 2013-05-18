package br.com.maboo.fuellist.screens;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.adapters.ListItemAdapter;
import br.com.maboo.fuellist.core.FuelListActivity;
import br.com.maboo.fuellist.dao.ItemLogDAO;
import br.com.maboo.fuellist.interfaces.InterfaceBar;
import br.com.maboo.fuellist.modelobj.Carro;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.modelobj.ListItemLog;
import br.com.maboo.fuellist.modelobj.Settings;
import br.com.maboo.fuellist.transaction.Transaction;
import br.com.maboo.fuellist.util.AndroidUtils;
import br.com.maboo.fuellist.util.Constants;

public class ReportScreen extends FuelListActivity implements InterfaceBar,
		Transaction {

	private final String TAG = Constants.LOG_APP;

	private static final int FUEL = Constants.FUEL;
	private static final int EXPENSE = Constants.EXPENSE;
	private static final int NOTE = Constants.NOTE;
	private static final int REPAIR = Constants.REPAIR;

	Vector<EditText> vEditText; // vetor de editText
	Vector<TextView> vTextView; // vetor de TextViews

	public static ItemLogDAO dao;

	private List<ItemLog> itens;

	private ListView listreport;

	private Long id_car;
	private String name_car;

	private Settings set;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		try {

			dao = new ItemLogDAO(this);

			setContentView(R.layout.view_report);

		} catch (SQLException e) {
			// erro caricato
			AndroidUtils.alertDialog(this,
					"Sorry, please... soooorry. And now, re-start the app.");

			e.printStackTrace();

		}

		// id do carro da vez
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			id_car = extras.getLong(Carro._ID);
			if (id_car != null) {

				name_car = extras.getString(Carro.NOME);

				montaTela(icicle);

			}
		}

		organizeBt();

		getSharedPrefs();

	}

	public void montaTela(Bundle icicle) {

		listreport = (ListView) findViewById(R.id.listreport);

		// salva itens na memoria
		itens = (List<ItemLog>) getLastNonConfigurationInstance();

		// Log.i("estado", "Lendo estado: getLastNonConfigurationInstance()");

		if (icicle != null) {

			// Recuperamos a lista de reports salva pelo
			// onSaveInstanceState(bundle)
			ListItemLog lista = (ListItemLog) icicle
					.getSerializable(ListItemLog.KEY);

			// Log.i(TAG, "Lendo estado: savedInstanceState(itens)");

			this.itens = lista.itens;

		}

		if (itens != null) { // Atualiza o ListView diretamente

			listreport.setAdapter(new ListItemAdapter(this, itens, set));

		} else {

			startTransaction(this); // incia tela

		}

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

	public void update() {

		getSharedPrefs();

		itens = getItens();

		listreport.setAdapter(new ListItemAdapter(this, itens, set));

	}

	public Object onRetainNonConfigurationInstance() {
		// Log.i("estado",
		// "Salvando Estado: onRetainNonConfigurationInstance()");

		return itens;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Log.i("estado", "Salvando Estado: onSaveInstanceState(bundle)");

		// Salvar o estado da tela
		outState.putSerializable(ListItemLog.KEY, new ListItemLog(itens));
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		update(); // re-carrega a lista

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
	 * Recupera a lista de itens
	 */
	private List<ItemLog> getItens() {
		List<ItemLog> list = null;

		try {
			list = dao.listarItemLogs(id_car);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/
	public void organizeBt() {

		// bt left
		final ImageView bt_left = (ImageView) findViewById(R.id.bt_left);
		bt_left.setImageResource(R.drawable.bt_back);

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_bar_edit);

		// bt down
		final TextView title_bt_down = (TextView) findViewById(R.id.title_bt_down);
		title_bt_down.setText("ainda nao sei");

	}

	public void btBarUpLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarUpRight(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarDown(View v) {
		// TODO Auto-generated method stub

	}

}
