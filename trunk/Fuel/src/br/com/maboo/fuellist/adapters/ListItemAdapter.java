package br.com.maboo.fuellist.adapters;

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
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.modelobj.Settings;
import br.com.maboo.fuellist.util.Constants;

public class ListItemAdapter extends BaseAdapter {

	protected static final String TAG = "appLog";

	private LayoutInflater inflater;

	private List<ItemLog> itens;

	private Typeface tf; // font

	private Settings set;

	public ListItemAdapter(Activity context, List<ItemLog> itens, Settings set) {
		// Log.i(TAG, "## charge ListItemAdapter ##");

		try {
			this.itens = itens;

			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			tf = Typeface.createFromAsset(context.getAssets(),
					"fonts/DroidSansFallback.ttf"); // font

			this.set = set;

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

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

		ItemLog itemRequest = itens.get(position);

		if (view == null) {
			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();

			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_log;
			view = inflater.inflate(layout, null);
			view.setTag(holder); // seta a tag
			view.setId(position);

			if (itemRequest.getType() == Constants.FUEL) {
				holder.textRightDown = (TextView) view
						.findViewById(R.id.textRightDown);

				holder.textRightDown.setTypeface(tf);
			}

			if (itemRequest.getType() == Constants.EXPENSE
					|| itemRequest.getType() == Constants.REPAIR
					|| itemRequest.getType() == Constants.FUEL) {

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

			} catch (ClassCastException e) {

				e.printStackTrace();

			}

		}
		// Log.i(TAG, "************************************************");
		// Log.i(TAG, "type [" + itemRequest.getType() + "]");
		// value u (valor unitario. Ex: litros)
		if (itemRequest.getType() == Constants.FUEL) {
			// Log.i(TAG, "valor getValue_u[" + itemRequest.getValue_u() + "]");

			holder.textRightDown.setText(set.getMoeda() + " "
					+ String.valueOf(itemRequest.getValue_u()));

			holder.textRightDown.setVisibility(View.VISIBLE);
		}

		// value p (valor total que pagou)
		if (itemRequest.getType() == Constants.EXPENSE
				|| itemRequest.getType() == Constants.REPAIR
				|| itemRequest.getType() == Constants.FUEL) {

			// Log.i(TAG, "valor getValue_p[" + itemRequest.getValue_p() + "]");

			holder.textRightUp.setText(set.getMoeda() + " "
					+ String.valueOf(itemRequest.getValue_p()));

			holder.textRightUp.setVisibility(View.VISIBLE);
		}

		// formata a data
		StringBuffer sb = new StringBuffer(itemRequest.getDate().length());
		for (int i = 0; i < itemRequest.getDate().length(); i++) {
			if (itemRequest.getDate().charAt(i) == '-') {
				sb.append(" ");
			}
			if (i > 0) {
				if (itemRequest.getDate().charAt(i - 1) == '-') {
					sb.append(" ");
				}
			}

			sb.append(itemRequest.getDate().charAt(i));
		}

		holder.date.setText(String.valueOf(sb.toString()));

		// subject
		if (itemRequest.getType() == Constants.EXPENSE
				|| itemRequest.getType() == Constants.REPAIR
				|| itemRequest.getType() == Constants.NOTE) {
			// Log.i(TAG, "valor getSubject[" + itemRequest.getSubject() + "]");

			holder.textLeftDown
					.setText(String.valueOf(itemRequest.getSubject()));

			holder.textLeftDown.setVisibility(View.VISIBLE);
		}

		// stocked (qtd de litro abastecido)
		if (itemRequest.getType() == Constants.FUEL) {

			// calcula qtd de litro abastecido
			Double total = Math.floor(itemRequest.getValue_p()
					/ itemRequest.getValue_u());

			// Log.i(TAG,"valor total.intValue()["+
			// String.valueOf(total.intValue()) + "]");

			holder.textLeftDown.setText(String.valueOf(total.intValue()) + " "
					+ set.getVolume());

			holder.textLeftDown.setVisibility(View.VISIBLE);
		}
		// Log.i(TAG, "************************************************");

		// icon
		switch (itemRequest.getType()) {
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