package br.com.maboo.fuellist.screens;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.adapters.ReportAdapter;
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
	Vector<TextView> vTextView; // vetor de TextViewsMulher Maravilha - A
								// Paródia

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

			setContentView(R.layout.list_report);

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

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/
	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {

		listreport = (ListView) findViewById(R.id.listreport);

		// cria title
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(name_car);

		// tamanho da fonte, para não estourar o espaço
		if (title.getText().length() > 10) {
			title.setTextSize(15);
		}

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

			listreport.setAdapter(new ReportAdapter(this, itens, set));

		} else {

			startTransaction(this); // incia tela

		}

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

	public void execute() throws Exception {
		this.itens = getItens();
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

	public void update() {

		getSharedPrefs();

		itens = getItens();

		// footer
	//	View footer = (View) getLayoutInflater().inflate(R.layout.item_footer_report, null);
	//	listreport.addFooterView(footer);

		listreport.setAdapter(new ReportAdapter(this, itens, set));

		setValoresReport();

	}

	/**
	 * Recupera a lista de itens
	 */
	private List<ItemLog> getItens() {
		List<ItemLog> list = null;

		try {
			list = dao.listarItemLogsOrderByTipo(id_car);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	private Double valorTotal = 0.0;
	private Double totalUnidade = 0.0;

	/**
	 * Recupera o valor total e de unidade da lista (por coluna ou tipo)
	 */
	private void setValoresReport() {
		// recupera valor de unidade e valor total
		for (int i = 0; i < itens.size(); i++) {
			ItemLog item = itens.get(i);

			// verifica se não é uma nota
			if (item.getType() != NOTE) {
				// incrementa valor
				valorTotal += item.getValue_p();

				// incrementa valor da unidade
				if (item.getType() == FUEL) {
					// calcula qtd de litro abastecido
					Double total = Math.floor(item.getValue_p()
							/ item.getValue_u());

					totalUnidade += total;
					Log.d("appLog", "## incrementa: (totalUnidade): "
							+ totalUnidade);
				}
			}
		}

		TextView totalvalorcategoria = (TextView) findViewById(R.id.totalvalorcategoria);
		totalvalorcategoria.setText(set.getMoeda() + "" + valorTotal);

		TextView totalunidadecategoria = (TextView) findViewById(R.id.totalunidadecategoria);
		totalunidadecategoria.setText(totalUnidade.intValue() + " "
				+ set.getVolume());
		
		// seta o titpo da unidade
		TextView tipoUnidade = (TextView) findViewById(R.id.tipoUnidade);
		tipoUnidade.setText("Unit("
				+ set.getVolume()+")");
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
		bt_right.setVisibility(View.INVISIBLE);

		// bar down
		// final TextView title_bt_down = (TextView)
		// findViewById(R.id.title_bt_down);
		// title_bt_down.setText(R.string.t_bar_down_fil);

		/*
		 * // titulo do report final TextView title_report = (TextView)
		 * findViewById(R.id.title_report);
		 * 
		 * // calendar final Calendar c = Calendar.getInstance(); // date int
		 * day_time = c.get(Calendar.DAY_OF_MONTH); int month_time =
		 * c.get(Calendar.MONTH); int year_time = c.get(Calendar.YEAR); String
		 * dateCurrent = new StringBuilder().append(pad(day_time))
		 * .append("/").append(pad(month_time)).append("/")
		 * .append(pad(year_time)).toString();
		 * 
		 * String titulo = "Report"; title_report.setText(titulo + " " +
		 * dateCurrent);
		 */

	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public void btBarUpLeft(View v) {
		// Fecha a tela
		finish();
	}

	public void btBarUpRight(View v) {
		//
	}

	public void btBarDown(View v) {
		//
	}

}
