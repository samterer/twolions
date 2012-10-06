package br.com.twolions.object;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.twolions.R;
import br.com.twolions.daoobjects.ItemLog;
import br.com.twolions.util.Constants;

public class ListItemAdapter extends BaseAdapter {
	protected static final String TAG = "appLog";
	private LayoutInflater inflater;
	private List<ItemLog> itens;

	public ListItemAdapter(Activity context, List<ItemLog> itens) {
		this.itens = itens;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			holder.icone = (ImageView) view.findViewById(R.id.imgLeftCenter);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ItemLog item = itens.get(position);
		// TODO
		String moeda = "$";
		String medida = "/L";
		
		// value u
		if (item.getType() == Constants.FUEL) {
			holder.value_u.setText(moeda + String.valueOf(item.getValue_u()));
			holder.value_u.setVisibility(View.VISIBLE);
		}
		
		// value p
		if (item.getType() == Constants.EXPENSE || item.getType()  == Constants.REPAIR || item.getType() == Constants.FUEL) {
			holder.value_p.setText(moeda + String.valueOf(item.getValue_p()));
			holder.value_p.setVisibility(View.VISIBLE);
		}
		
		// date
		holder.date.setText(String.valueOf(item.getDate()));
		
		// subject
		if (item.getType() == Constants.EXPENSE || item.getType() == Constants.REPAIR  || item.getType() == Constants.NOTE) {
			holder.subject.setText(String.valueOf(item.getSubject()));
			holder.subject.setVisibility(View.VISIBLE);
		}

		// stocked
		if (item.getType() == Constants.FUEL) {
			// calcula qtd de litro abastecido
			Double total = Math.floor(item.getValue_p() / item.getValue_u());
			holder.text.setText(String.valueOf(total.intValue()) + medida);
			holder.text.setVisibility(View.VISIBLE);
		}
		
		// icon
		switch (item.getType()) {
		case Constants.FUEL:
			holder.icone.setImageResource(R.drawable.fuel);
			break;

		case Constants.EXPENSE:
			holder.icone.setImageResource(R.drawable.expense);
			break;
		case Constants.NOTE:
			holder.icone.setImageResource(R.drawable.note);
			break;
		case Constants.REPAIR:
			holder.icone.setImageResource(R.drawable.repair);
			break;
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
		ImageView icone;
	}
}