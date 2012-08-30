package br.com.twolions.screens;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.com.transacao.Transacao;
import br.com.twolions.R;
import br.com.twolions.object.Carro;
import br.com.twolions.object.CarroAdapter;
import br.com.twolions.service.CarroService;

public class ListCarScreen extends ListCarActivity implements
		OnItemClickListener, Transacao {

	private ListView listView;
	private List<Carro> carros;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mountScreen();

	}

	private void mountScreen() {

		setContentView(R.layout.carros);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);

		carros = new ArrayList<Carro>();

		startTransaction(this);
	}

	public void executar() throws Exception {
		// busca os carros em uma thread
		this.carros = CarroService.getCarros();

		for (int i = 0; i < carros.size(); i++) {
			Log.i("appLog", "item: " + carros.get(i).getNome());
		}

	}

	public void atualizarView() {
		Log.i("appLog", "> atualizarView");

		// atualiza os carros na thread principal
		if (carros != null) {
			listView.setAdapter(new CarroAdapter(this, carros));
		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int posicao,
			long id) {
		Carro c = (Carro) parent.getAdapter().getItem(posicao);
		Toast.makeText(this, "Carro: " + c.nome, Toast.LENGTH_SHORT).show();

	}

	// TESTS
	// public void helpAboutReport(View v) {
	//
	// ImageView t_report = (ImageView) findViewById(R.id.t_report);
	// t_report.setVisibility(View.GONE);
	//
	// ImageView t_select_vehicle = (ImageView)
	// findViewById(R.id.t_select_vehicle);
	// t_select_vehicle.setVisibility(View.VISIBLE);
	//
	// Toast.makeText(this, "help about report(single car)",
	// Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// public void helpAboutSelectVehicle(View v) {
	//
	// ImageView t_report = (ImageView) findViewById(R.id.t_report);
	// t_report.setVisibility(View.VISIBLE);
	//
	// ImageView t_select_vehicle = (ImageView)
	// findViewById(R.id.t_select_vehicle);
	// t_select_vehicle.setVisibility(View.GONE);
	//
	// Toast.makeText(this, "help about selection of vehicles",
	// Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// public void help(View v) {
	// ImageView bt_help = (ImageView) findViewById(R.id.bt_help);
	// bt_help.setVisibility(View.GONE);
	//
	// ImageView bt_select_vehicle = (ImageView)
	// findViewById(R.id.bt_select_vehicle);
	// bt_select_vehicle.setVisibility(View.VISIBLE);
	//
	// Toast.makeText(this, "go to help screen", Toast.LENGTH_SHORT).show();
	// }
	//
	// public void backToSelectBehicle(View v) {
	//
	// ImageView bt_help = (ImageView) findViewById(R.id.bt_help);
	// bt_help.setVisibility(View.VISIBLE);
	//
	// ImageView bt_select_vehicle = (ImageView)
	// findViewById(R.id.bt_select_vehicle);
	// bt_select_vehicle.setVisibility(View.GONE);
	//
	// Toast.makeText(this, "back to vehicle list", Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// public void addNewRegister(View v) {
	//
	// Toast.makeText(this, "add new register", Toast.LENGTH_SHORT).show();
	//
	// }
}
