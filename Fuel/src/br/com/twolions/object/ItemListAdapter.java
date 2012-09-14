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
import br.com.twolions.daoobjects.ItemLog;

public class ItemListAdapter extends BaseAdapter {
	private Context context;
	private List<ItemLog> lista;

	public ItemListAdapter(Context context, List<ItemLog> lista) {
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
		ItemLog item = lista.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_log, null);

		// fuel
		if (item.type == 0) {
			// value p
			// value u
			// value total abastecido
			// date

		}
		// expense
		if (item.type == 1) {
			// value p
			// subject
			// date
		}
		// note
		if (item.type == 2) {
			// text
			// subject
			// date
		}
		// repair
		if (item.type == 3) {
			// value p
			// subject
			// date
		}

		TextView nome = (TextView) view.findViewById(R.id.nome);
		// nome.setText(c.nome);

		TextView placa = (TextView) view.findViewById(R.id.placa);
		// placa.setText(c.placa);

		ImageView tipo = (ImageView) view.findViewById(R.id.tipo);
		/*
		 * if (c.tipo.equals("carro")) {
		 * tipo.setImageResource(R.drawable.type_car_on); } else {
		 * tipo.setImageResource(R.drawable.type_moto_on); }
		 */

		return view;
	}
}