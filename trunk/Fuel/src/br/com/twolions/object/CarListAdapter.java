package br.com.twolions.object;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.twolions.R;
import br.com.twolions.daoobjects.Carro;

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
		// Recupera o Carro da posi��o atual
		Carro c = lista.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_car, null);

		// Atualiza o valor do TextView
		TextView nome = (TextView) view.findViewById(R.id.nome);
		nome.setText(c.getNome());

		TextView placa = (TextView) view.findViewById(R.id.placa);
		placa.setText(c.getPlaca());

		ImageView tipo = (ImageView) view.findViewById(R.id.tipo);
		if (c.getTipo().equals("carro")) {
			tipo.setImageResource(R.drawable.type_car_on);
		} else {
			tipo.setImageResource(R.drawable.type_moto_on);
		}

		return view;
	}
}