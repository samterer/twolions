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

public class ListCarAdapter extends BaseAdapter {
	private final Context context;
	private final List<Carro> lista;

	public ListCarAdapter(final Context context, final List<Carro> lista) {
		this.context = context;
		this.lista = lista;
	}

	public int getCount() {
		return lista.size();
	}

	public Object getItem(final int position) {
		return lista.get(position);
	}

	public long getItemId(final int position) {
		return position;
	}

	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		// Recupera o Carro da posição atual
		final Carro c = lista.get(position);

		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.item_car, null);

		// Atualiza o valor do TextView
		final TextView nome = (TextView) view.findViewById(R.id.nome);
		nome.setText(c.getNome());

		final TextView placa = (TextView) view.findViewById(R.id.placa);
		placa.setText(c.getPlaca());

		final ImageView tipo = (ImageView) view.findViewById(R.id.tipo);
		if (c.getTipo().equals("carro")) {
			tipo.setImageResource(R.drawable.type_car_on);
		} else {
			tipo.setImageResource(R.drawable.type_moto_on);
		}

		return view;
	}
}