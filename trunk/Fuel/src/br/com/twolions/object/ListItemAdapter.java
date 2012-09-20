package br.com.twolions.object;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.twolions.R;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.util.Constants;

public class ListItemAdapter extends BaseAdapter {
	private Context context;
	private List<ItemLog> lista;

	public ListItemAdapter(Context context, List<ItemLog> lista) {
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

		// date
		TextView date = (TextView) view.findViewById(R.id.date);
		date.setText(String.valueOf(item.getDate()));

		// icone

		// TODO
		String moeda = "$";
		String medida = "/L";

		// value u
		if (item.getValue_u() > 0) {
			TextView value_u = (TextView) view.findViewById(R.id.textRightDown);
			value_u.setText(moeda + String.valueOf(item.getValue_u()));
		}

		// value p
		if (item.getValue_p() > 0) {

			TextView value_p = (TextView) view.findViewById(R.id.textRightUp);
			value_p.setText(moeda + String.valueOf(item.getValue_p()));

		}
		// subject
		if (item.getSubject() != "") {
			TextView subject = (TextView) view.findViewById(R.id.textLeftDown);
			subject.setText(String.valueOf(item.getSubject()));

		}

		// stocked
		if (item.getType() == Constants.FUEL) {
			// value total abastecido
			TextView value_t = (TextView) view.findViewById(R.id.textLeftDown);
			// calcula qtd de litro abastecido
			Double total = Math.floor(item.getValue_p() / item.getValue_u());
			value_t.setText(String.valueOf(total.intValue()) + medida);
		}

		/*
		 * // fuel if (item.getType() == 0 || ) { // value p TextView value_p =
		 * (TextView) view.findViewById(R.id.textRightUp);
		 * value_p.setText(String.valueOf(item.getValue_p())); // value u
		 * TextView value_u = (TextView) view.findViewById(R.id.textRightDown);
		 * value_u.setText(String.valueOf(item.getValue_u())); // value total
		 * abastecido TextView value_t = (TextView)
		 * view.findViewById(R.id.textLeftDown); // calcula qtd de litro
		 * abastecido Double total = Math.floor(item.getValue_p() /
		 * item.getValue_u()); value_t.setText(String.valueOf(total)); } //
		 * expense if (item.getType() == 1) { // value p TextView value_p =
		 * (TextView) view.findViewById(R.id.textRightUp);
		 * value_p.setText(String.valueOf(item.getValue_p())); // subject
		 * TextView subject = (TextView) view.findViewById(R.id.textLeftDown);
		 * subject.setText(String.valueOf(item.getSubject())); } // note if
		 * (item.getType() == 2) { // text // subject TextView subject =
		 * (TextView) view.findViewById(R.id.textLeftDown);
		 * subject.setText(String.valueOf(item.getSubject())); } // repair if
		 * (item.getType() == 3) { // value p TextView value_p = (TextView)
		 * view.findViewById(R.id.textRightUp);
		 * value_p.setText(String.valueOf(item.getValue_p())); // subject
		 * TextView subject = (TextView) view.findViewById(R.id.textLeftDown);
		 * subject.setText(String.valueOf(item.getSubject())); }
		 */

		return view;
	}
}