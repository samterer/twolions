package br.com.maboo.fuellist.screens;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import br.com.maboo.fuellist.util.ToastUtil;

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

		// addListenerOnButton();

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
		
	    // reseta valores dos totais
		valorTotal = 0.0;
		totalUnidade = 0.0;

		getSharedPrefs();

		itens = getItens();

		listreport.setAdapter(new ReportAdapter(this, itens, set));

		setValoresReport();

	}

	/*
	 * Atualiza a lista, de acordo com um limite entre datas definidas pelo
	 * usuarios nos campos from: e to:
	 */
	private Date dateItem;
	private SimpleDateFormat sdf;
	// lista definida pela data
	private List<ItemLog> itensNew;

	// date from
	Date dateFrom = null;

	// date to
	Date dateTo = null;
	
	private void updateListWithDate() {

		itensNew = new ArrayList<ItemLog>();

		// salva data temporaria
		String dateTemp;

		try {

			sysout("## date_left: " + date_left);
			dateTemp = date_left.getText().toString().substring(6, 16)
					.toString();
			sysout("## date from: " + dateTemp);
			dateFrom = sdf.parse(dateTemp.trim());

			dateTemp = "";

			dateTemp = date_right.getText().toString().substring(4, 14)
					.toString();
			sysout("## date to: " + dateTemp);
			dateTo = sdf.parse(dateTemp.trim());

			dateTemp = "";

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// organiza a lista de acordo com as datas iniciais e finais
		for (int i = 0; i < itens.size(); i++) {
			ItemLog item = itens.get(i);

			// não contabiliza itens do tipo de note
			if (item.getType() == NOTE) {
				continue;
			}

			try {
				dateTemp = item.getDate().substring(0, 10).toString().trim();
				dateItem = sdf.parse(dateTemp);
				sysout("## verificando se o item [" + item.getId()
						+ "] esta dentro da regra, sua data é [" + dateItem
						+ "]");
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// verifica se a data é igual ao 'from' ou ao 'to'
			if (dateItem.equals(dateFrom) || dateItem.equals(dateTo)) {
				itensNew.add(item);

				sysout("## o item [" + item.getId()
						+ "] esta dentro da regra de 'equal'");

				continue;
			} else {
				sysout("## " + dateItem + " não é igual a [" + dateFrom
						+ "] ou [" + dateTo + "]");
			}

			// verifica se a data é maior que a 'from' e menor que a 'to'
			if (dateItem.after(dateFrom) && dateItem.before(dateTo)) {
				itensNew.add(item);

				sysout("## o item [" + item.getId()
						+ "] esta dentro da regra 'entre'");

				continue;
			} else {
				sysout("## " + dateItem + " não esta entre [" + dateFrom
						+ "] e [" + dateTo + "]");
			}

		}

		// verifica se deve atualizar a tela
		if (itensNew.size() > 0) {

			listreport.setAdapter(new ReportAdapter(this, itensNew, set));

			setValoresReport();
		}

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
		totalvalorcategoria.setText(set.getMoeda() + ""
				+ String.format("%.2f", valorTotal));

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

	// calendar (unico)
	private Calendar c;

	private void initDate() {

		// calendar
		c = Calendar.getInstance();

		// pattern
		sdf = new SimpleDateFormat("dd/mm/yyyy");

		// date
		date_left = (TextView) findViewById(R.id.date_left);

		dl_day_time = c.get(Calendar.DAY_OF_MONTH);
		dl_month_time = c.get(Calendar.MONTH);
		dl_year_time = c.get(Calendar.YEAR);

		// recupera data inicial
		recuperaDataMaisAntiga();

		date_left.setText("from: "
				+ new StringBuilder().append(AndroidUtils.pad(dl_day_time))
						.append("/").append(AndroidUtils.pad(dl_month_time))
						.append("/").append(AndroidUtils.pad(dl_year_time)));

		// date
		date_right = (TextView) findViewById(R.id.date_right);

		dr_day_time = c.get(Calendar.DAY_OF_MONTH);
		dr_month_time = c.get(Calendar.MONTH) + 1;
		dr_year_time = c.get(Calendar.YEAR);

		date_right.setText("to: "
				+ new StringBuilder().append(AndroidUtils.pad(dr_day_time))
						.append("/").append(AndroidUtils.pad(dr_month_time))
						.append("/").append(AndroidUtils.pad(dr_year_time)));

	}

	// recupera data do item mais antigo da lista
	private void recuperaDataMaisAntiga() {

		if (itens == null) {
			itens = getItens();
		}

		// salva data temporaria
		Date valor = new Date();
		Date dateMenor = new Date();

		// começa o processo para inserir a data
		// no campo da esquerda
		dl_day_time = c.get(Calendar.DAY_OF_MONTH);
		dl_month_time = c.get(Calendar.MONTH) + 1;
		dl_year_time = c.get(Calendar.YEAR);

		dateMenor.setDate(dl_day_time);
		dateMenor.setMonth(dl_month_time);
		dateMenor.setYear(dl_year_time);

		for (int i = 0; i < itens.size(); i++) {
			ItemLog item = itens.get(i);

			// não contabiliza itens do tipo de note
			if (item.getType() == NOTE) {
				continue;
			}

			try {
				valor = sdf.parse(item.getDate().substring(0, 10).toString()
						.trim());
				sysout("## valor: " + valor);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (valor.before(dateMenor)) {
				dateMenor = valor;
				sysout("## dateMenor: " + dateMenor);
			}
		}

		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		String s = formatter.format(dateMenor);

		sysout("## dateMenor(final): " + s);

		// formata date
		dl_day_time = Integer.valueOf(s.substring(0, 2)).intValue();
		dl_month_time = Integer.valueOf(s.substring(3, 5)).intValue();
		dl_year_time = Integer.valueOf(s.substring(6, 10)).intValue();

	}

	private int datePickerInput = 0;
	private static final int DIALOG_DATE_PICKER_L = 100;
	private static final int DIALOG_DATE_PICKER_R = 111;

	public void onL(View v) {

		datePickerInput = DIALOG_DATE_PICKER_L;

		showDialog(DIALOG_DATE_PICKER_L);
	}

	public void onR(View v) {

		datePickerInput = DIALOG_DATE_PICKER_R;

		showDialog(DIALOG_DATE_PICKER_R);
	}

	protected Dialog onCreateDialog(int id) {

		switch (datePickerInput) {
		case DIALOG_DATE_PICKER_L:

			return new DatePickerDialog(this, myDateSetListener, dl_year_time,
					dl_month_time - 1, dl_day_time);
		case DIALOG_DATE_PICKER_R:

			return new DatePickerDialog(this, myDateSetListener, dr_year_time,
					dr_month_time - 1, dr_day_time);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			switch (datePickerInput) {
			case DIALOG_DATE_PICKER_L:
				dl_day_time = dayOfMonth;
				dl_month_time = monthOfYear;
				dl_year_time = year;

				// set current date into textview
				date_left.setText("from: "
						+ new StringBuilder()
								.append(AndroidUtils.pad(dl_day_time))
								.append("/")
								.append(AndroidUtils.pad(dl_month_time + 1))
								.append("/")
								.append(AndroidUtils.pad(dl_year_time)));
				break;

			case DIALOG_DATE_PICKER_R:
				dr_day_time = dayOfMonth;
				dr_month_time = monthOfYear;
				dr_year_time = year;

				// set current date into textview
				date_right.setText("to: "
						+ new StringBuilder()
								.append(AndroidUtils.pad(dr_day_time))
								.append("/")
								.append(AndroidUtils.pad(dr_month_time + 1))
								.append("/")
								.append(AndroidUtils.pad(dr_year_time)));
				break;
			}
			
			// a data do 'to' é menor do que a data do 'from'
			try {
				if(dateFrom.after(dateTo)) { 
					ToastUtil toast = new ToastUtil(ReportScreen.this);
					toast.show("'Date to' less than 'Date from'");
					return;
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			// atualiza lista
			updateListWithDate();

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

		List<ItemLog> itensTemp = new ArrayList<ItemLog>();

		// define lista usada para gerar o report
		if (itensNew != null && itensNew.size() > 0) {
			itensTemp = itensNew;
		} else {
			itensTemp = itens;
		}

		// cria o cabeçalho do relatorio
		sb.append(getSpace(45) + "<b>Filter by date<br /><br />");
		sb.append(getSpace(24) + date_left.getText().toString() + getSpace(5)
				+ "-" + getSpace(5) + date_right.getText().toString()
				+ "<br /><br /><br /><br />");
		for (int i = 0; i < itensTemp.size(); i++) {
			// recupera o item da vez
			ItemLog item = itensTemp.get(i);

			// cabeçalho da lista
			if (i == 0) {
				sb.append("Date" + getSpace(28) + "Type" + getSpace(12)
						+ "Unit(" + set.getVolume() + ")" + getSpace(12)
						+ "Detail" + getSpace(14) + "Values</b><br />");
				// line
				sb.append("__________________________________________________________________________");
				sb.append("<br /><br />");
			}

			sb.append("<strong>");
			sb.append("<div>");

			// não imprime itens do tipo de note
			if (item.getType() == NOTE) {
				continue;
			}

			// data
			sb.append(item.getDate() + getSpace());

			// tipo
			sb.append(getType(item.getType()).toLowerCase() + getSpace(16));

			// unidade
			if (item.getType() == FUEL) {
				// calcula qtd de litro abastecido
				Double total = Math
						.floor(item.getValue_p() / item.getValue_u());
				sb.append(AndroidUtils.pad(total.intValue()) + getSpace(18));
			} else {
				sb.append(getSpace(15)); // insere um espaço pra formatação
			}

			// detail
			// regra de espaço do repair
			if (item.getType() == REPAIR) {
				sb.append(getSpace(4));
			}

			if (item.getSubject().toString().length() > limiteSpace) {
				StringBuffer sbSubjec = new StringBuffer();
				for (int j = 0; j < (limiteSpace - 2); j++) {
					sbSubjec.append(item.getSubject().charAt(j));
				}
				sb.append((sbSubjec.toString() + "...").toLowerCase());
			} else {
				sb.append(String.valueOf(item.getSubject()).toLowerCase());
			}

			// regras de espaço para a formatação
			switch (item.getType()) {
			case FUEL:
				if (item.getSubject().length() >= limiteSpace) {
					sb.append(getSpace(8));
				} else {
					sb.append(getSpace()
							+ getDefineSpace(item.getSubject().length()));
				}
				break;
			case EXPENSE:
				if (item.getSubject().length() >= limiteSpace) {
					sb.append(getSpace(8));
				} else {
					sb.append(getSpace()
							+ getDefineSpace(item.getSubject().length()));
				}
				break;
			case REPAIR:
				if (item.getSubject().length() >= limiteSpace) {
					sb.append(getSpace(8));
				} else {
					sb.append(getSpace()
							+ getDefineSpace(item.getSubject().length()));
				}
				break;
			}

			// values
			// formata o espaço antes do item
			sb.append(getSpace(8) + set.getMoeda() + " " + item.getValue_p());

			// line
			sb.append("<br />");
			sb.append("__________________________________________________________________________");
			sb.append("<br /><br />");
			sb.append("</div></strong>");
		}

		// cria o rodape

		sb.append("<b>" + getSpace(54)
				+ AndroidUtils.pad(totalUnidade.intValue()) + getSpace(42) + ""
				+ set.getMoeda() + getSpace(1)
				+ String.format("%.2f", valorTotal) + "</b>");

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

	private void sysout(String text) {
		Log.i(TAG, text);
	}
}
