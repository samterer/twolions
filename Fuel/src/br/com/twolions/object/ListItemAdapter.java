package br.com.twolions.object;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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

	Typeface tf; // font

	public ListItemAdapter(Activity context, List<ItemLog> itens) {

		this.itens = itens;

		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		tf = Typeface.createFromAsset(context.getAssets(),
				"fonts/DroidSansFallback.ttf"); // font
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

		ItemLog item = itens.get(position);

		if (view == null) {
			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();
			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_log;
			view = inflater.inflate(layout, null);
			view.setTag(holder);

			if (item.getType() == Constants.FUEL) {
				holder.textRightDown = (TextView) view
						.findViewById(R.id.textRightDown);
				holder.textRightDown.setTypeface(tf);
			}

			if (item.getType() == Constants.EXPENSE
					|| item.getType() == Constants.REPAIR
					|| item.getType() == Constants.FUEL) {
				holder.textRightUp = (TextView) view
						.findViewById(R.id.textRightUp);
				holder.textRightUp.setTypeface(tf);
			}

			holder.date = (TextView) view.findViewById(R.id.date);
			holder.date.setTypeface(tf);

			holder.textLeftDown = (TextView) view
					.findViewById(R.id.textLeftDown);
			holder.textLeftDown.setTypeface(tf);

			holder.icone = (ImageView) view.findViewById(R.id.imgLeftCenter);
		} else {
			// Ja existe no cache, bingo entao pega!
			try {
				holder = (ViewHolder) view.getTag();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		// TODO
		String moeda = "$";
		String medida = "/L";

		// value u
		if (item.getType() == Constants.FUEL) {
			holder.textRightDown.setText(moeda
					+ String.valueOf(item.getValue_u()));
			holder.textRightDown.setVisibility(View.VISIBLE);
		}

		// value p
		if (item.getType() == Constants.EXPENSE
				|| item.getType() == Constants.REPAIR
				|| item.getType() == Constants.FUEL) {
			holder.textRightUp.setText(moeda
					+ String.valueOf(item.getValue_p()));
			holder.textRightUp.setVisibility(View.VISIBLE);
		}

		// date
		holder.date.setText(String.valueOf(item.getDate()));

		// subject
		if (item.getType() == Constants.EXPENSE
				|| item.getType() == Constants.REPAIR
				|| item.getType() == Constants.NOTE) {
			holder.textLeftDown.setText(String.valueOf(item.getSubject()));
			holder.textLeftDown.setVisibility(View.VISIBLE);
		}

		// stocked
		if (item.getType() == Constants.FUEL) {
			// calcula qtd de litro abastecido
			Double total = Math.floor(item.getValue_p() / item.getValue_u());
			holder.textLeftDown.setText(String.valueOf(total.intValue())
					+ medida);
			holder.textLeftDown.setVisibility(View.VISIBLE);
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
		TextView textRightDown;
		TextView textRightUp;
		TextView date;
		TextView textLeftDown;
		ImageView icone;
	}
}