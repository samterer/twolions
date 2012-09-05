package br.com.twolions.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.twolions.R;
import br.com.twolions.dao.Carro;

public class CarListAdapter extends BaseAdapter {
	private Context context;
	private List<Carro> lista;

	public CarListAdapter(Context context, List<Carro> lista) {
		this.context = context;
		this.lista = lista;
	}

	public int getCount() {
		return lista.size();
	}

	public Object getItem(int position) {
		return lista.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// Recupera o Carro da posição atual
		Carro c = lista.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.car_item, null);

		// Atualiza o valor do TextView
		TextView nome = (TextView) view.findViewById(R.id.nome);
		nome.setText(c.nome);

		TextView placa = (TextView) view.findViewById(R.id.placa);
		placa.setText(c.placa);

		ImageView tipo = (ImageView) view.findViewById(R.id.tipo);
		if (c.tipo.equals("carro")) {
			tipo.setImageResource(R.drawable.type_car_on);
		} else {
			tipo.setImageResource(R.drawable.type_moto_on);
		}

		return view;
	}
}