package br.com.twolions.object;

import java.util.List;

import android.app.Activity;
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
	private Activity context;
	private LayoutInflater inflater;
	private List<ItemLog> itens;

	public ListItemAdapter(Activity context, List<ItemLog> itens) {
		this.context = context;
		this.itens = itens;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Recupera o objeto global da aplicação
		// FuelApplication application = (FuelApplication)
		// context.getApplication();
	}

	public int getCount() {
		return itens != null ? itens.size() : 0;
	}

	public Object getItem(int position) {
		return itens != null ? itens.get(position) : null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();
			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_log;
			view = inflater.inflate(layout, null);
			view.setTag(holder);
			holder.value_u = (TextView) view.findViewById(R.id.textRightDown);
			holder.value_p = (TextView) view.findViewById(R.id.textRightUp);
			holder.date = (TextView) view.findViewById(R.id.date);
			holder.subject = (TextView) view.findViewById(R.id.textLeftDown);
			holder.text = (TextView) view.findViewById(R.id.textLeftDown);
		} else {

			holder = (ViewHolder) view.getTag();

		}
		ItemLog item = itens.get(position);
		// TODO
		String moeda = "$";
		String medida = "/L";
		// value u
		if (item.getValue_u() > 0) {
			holder.value_u.setText(moeda + String.valueOf(item.getValue_u()));
		}
		// value p
		if (item.getValue_p() > 0) {
			holder.value_p.setText(moeda + String.valueOf(item.getValue_p()));
		}
		// date
		holder.date.setText(String.valueOf(item.getDate()));
		// subject
		if (item.getSubject() != "") {
			holder.subject.setText(String.valueOf(item.getSubject()));
		}

		// stocked
		if (item.getType() == Constants.FUEL) {
			// calcula qtd de litro abastecido
			Double total = Math.floor(item.getValue_p() / item.getValue_u());
			holder.text.setText(String.valueOf(total.intValue()) + medida);
		}

		return view;
	}
	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		TextView value_u;
		TextView value_p;
		TextView date;
		TextView subject;
		TextView text;
	}
}