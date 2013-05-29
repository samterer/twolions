package br.com.maboo.fuellist.screens;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
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
		Transaction, OnClickListener {

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

			getSharedPrefs();

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

		initDate();

	}

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/
	@SuppressWarnings("unchecked")
	public void montaTela(Bundle icicle) {

		listreport = (ListView) findViewById(R.id.listreport);

		// cria title da tela(nome do carro)
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
		totalvalorcategoria.setText(set.getMoeda() + "" + String.format("%.2f", valorTotal));

		TextView totalunidadecategoria = (TextView) findViewById(R.id.totalunidadecategoria);
		totalunidadecategoria.setText(totalUnidade.intValue() + " "
				+ set.getVolume());

		// seta o titpo da unidade
		TextView tipoUnidade = (TextView) findViewById(R.id.tipoUnidade);
		tipoUnidade.setText("Unit(" + set.getVolume() + ")");
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
		bt_right.setImageResource(R.drawable.bt_share);

		// bar down
		final TextView title_filter = (TextView) findViewById(R.id.title_filter);
		title_filter.setText("Filter by date");

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
		
		shareReport();
		
	}

	public void btBarDown(View v) {
		// TODO Auto-generated method stub

	}

	/******************************************************************************
	 * DATES
	 ******************************************************************************/
	// spinner value
	private int dl_day_time;
	private int dl_month_time;
	private int dl_year_time;

	private int dr_day_time;
	private int dr_month_time;
	private int dr_year_time;

	private TextView date_left;
	private TextView date_right;

	private void initDate() {

		// calendar
		final Calendar c = Calendar.getInstance();
		// date
		date_left = (TextView) findViewById(R.id.date_left);
		date_left.setOnClickListener(this);
		dl_day_time = c.get(Calendar.DAY_OF_MONTH);
		dl_month_time = c.get(Calendar.MONTH);
		dl_year_time = c.get(Calendar.YEAR);

		date_left.setText("from: "
				+ new StringBuilder().append(AndroidUtils.pad(dl_day_time))
						.append("/").append(AndroidUtils.pad(dl_month_time))
						.append("/").append(AndroidUtils.pad(dl_year_time)));

		// date
		date_right = (TextView) findViewById(R.id.date_right);
		date_right.setOnClickListener(this);
		dr_day_time = c.get(Calendar.DAY_OF_MONTH);
		dr_month_time = c.get(Calendar.MONTH);
		dr_year_time = c.get(Calendar.YEAR);

		date_right.setText("to: "
				+ new StringBuilder().append(AndroidUtils.pad(dr_day_time))
						.append("/").append(AndroidUtils.pad(dr_month_time))
						.append("/").append(AndroidUtils.pad(dr_year_time)));

	}
	
	private int datePickerInput = 0; 
	private static final int        DIALOG_DATE_PICKER_L  = 100;
	private static final int        DIALOG_DATE_PICKER_R  = 111;
	
	
	public void onClick(View v) {
		
		datePickerInput = v.getId();
		
		if(v == findViewById(R.id.date_left)){
			showDialog(DIALOG_DATE_PICKER_L);
        }else if (v == findViewById(R.id.date_right)){
			showDialog(DIALOG_DATE_PICKER_R);
        }
		
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DATE_PICKER_L:
			return new DatePickerDialog(this, myDateSetListener, dl_year_time,
					dl_month_time, dl_day_time);
		case DIALOG_DATE_PICKER_R:
			return new DatePickerDialog(this, myDateSetListener, dr_year_time,
					dr_month_time, dr_day_time);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			
			Log.i("appLog","## id: "+view.getId());

			switch (datePickerInput) {
			case R.id.date_left:
				dl_day_time = dayOfMonth;
				dl_month_time = monthOfYear;
				dl_year_time = year;

				// set current date into textview
				date_left.setText("from: "
						+ new StringBuilder()
								.append(AndroidUtils.pad(dl_day_time))
								.append("/")
								.append(AndroidUtils.pad(dl_month_time))
								.append("/")
								.append(AndroidUtils.pad(dl_year_time)));
				break;

			case R.id.date_right:
				dr_day_time = dayOfMonth;
				dr_month_time = monthOfYear;
				dr_year_time = year;

				// set current date into textview
				date_right.setText("to: "
						+ new StringBuilder()
								.append(AndroidUtils.pad(dr_day_time))
								.append("/")
								.append(AndroidUtils.pad(dr_month_time))
								.append("/")
								.append(AndroidUtils.pad(dr_year_time)));
				break;
			}

		}
	};
	
	/******************************************************************************
	 * REPORT
	 ******************************************************************************/
	private void shareReport() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/html");
		i.putExtra(Intent.EXTRA_SUBJECT,
				"FuelReport (" + name_car.toUpperCase() + ") "
						+ date_left.getText().toString() + " - "
						+ date_right.getText().toString());

		i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(getReport()));

		startActivity(Intent.createChooser(i, "Choose an Email client :"));
	}
	
	private String getReport() {
		StringBuffer sb = new StringBuffer();
		
		// cria o cabeçalho do relatorio
		sb.append(getSpace(45)+"<b>Filter by date<br /><br />");
		sb.append(getSpace(24)+date_left.getText().toString()+getSpace(5)+"-"+getSpace(5)+date_right.getText().toString()+"<br /><br /><br /><br />");
		for (int i = 0; i < itens.size(); i++) {
			// recupera o item da vez
			ItemLog item = itens.get(i);
			
			// cabeçalho da lista
			if(i == 0){ 
				sb.append("Date"+getSpace(28)+"Type"+getSpace(12)+"Unit("+set.getVolume()+")"+getSpace(12)+"Detail"+getSpace(14)+"Values</b><br />");
				// line
				sb.append("__________________________________________________________________________");
				sb.append("<br /><br />");
			}
			
			sb.append("<strong>");
			sb.append("<div>");
			
			// não imprime itens do tipo de note
			if(item.getType() == NOTE) {
				continue;
			}
			
			// data
			sb.append(item.getDate()+getSpace());
			
			// tipo
			sb.append(getType(item.getType()).toLowerCase()+getSpace(16));
			
			// unidade
			if (item.getType() == FUEL) {
				// calcula qtd de litro abastecido
				Double total = Math
						.floor(item.getValue_p() / item.getValue_u());
				sb.append(AndroidUtils.pad(total.intValue())+getSpace(18));
			} else {
				sb.append(getSpace(15)); // insere um espaço pra formatação
			}
			
			// detail
			// regra de espaço do repair
			if(item.getType() == REPAIR) {
				sb.append(getSpace(4));
			}
			
			if (item.getSubject().toString().length() > limiteSpace) {
				StringBuffer sbSubjec = new StringBuffer();
				for (int j = 0; j < (limiteSpace-2); j++) {
					sbSubjec.append(item.getSubject().charAt(j));
				}
				sb.append((sbSubjec.toString() + "...").toLowerCase());
			} else {
				sb.append(String.valueOf(item.getSubject())
						.toLowerCase());
			}
			
			//regras de espaço para a formatação
			switch (item.getType()) {
			case FUEL:
				if(item.getSubject().length() >= limiteSpace) {
					sb.append(getSpace(8));
				} else {
					sb.append(getSpace()+getDefineSpace(item.getSubject().length()));
				}				
				break;
			case EXPENSE:
				if(item.getSubject().length() >= limiteSpace) {
					sb.append(getSpace(8));
				} else {
					sb.append(getSpace()+getDefineSpace(item.getSubject().length()));
				}	
				break;
			case REPAIR:
				if(item.getSubject().length() >= limiteSpace) {
					sb.append(getSpace(8));
				} else {
					sb.append(getSpace()+getDefineSpace(item.getSubject().length()));
				}	
				break;
			}
			
			// values
			//formata o espaço antes do item
			sb.append(getSpace(8)+set.getMoeda() + " " + item.getValue_p());
			
			// line
			sb.append("<br />");
			sb.append("__________________________________________________________________________");
			sb.append("<br /><br />");
			sb.append("</div></strong>");
		}
		
		// cria o rodape
		
		sb.append("<b>"+getSpace(54)+AndroidUtils.pad(totalUnidade.intValue())+getSpace(42)+""+set.getMoeda()+getSpace(1)+String.format("%.2f", valorTotal)+"</b>");
		
		return sb.toString();
	}
	
	private int limiteSpace = 8;
	private String getSpace() {
		return getSpace(10);
	}
	private String getSpace(int tam) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tam; i++) {
			sb.append("&nbsp;");
		}
		return sb.toString();
	}
	
	private String getDefineSpace(int tamCurrent) {
		return getSpace(limiteSpace - tamCurrent); 
	}
	
	

	private String getType(int tipo) {
		switch (tipo) {
		case FUEL:
			return "FUEL";
		case EXPENSE:
			return "EXPENSE";
		case NOTE:
			return "NOTE";
		default:
			return "REPAIR";
		}
	}
}
