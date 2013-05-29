package br.com.maboo.fuellist.adapters;

import java.util.ArrayList;
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
import br.com.maboo.fuellist.util.AndroidUtils;
import br.com.maboo.fuellist.util.Constants;

public class ReportAdapter extends BaseAdapter {

	protected static final String TAG = "appLog";

	private static final int FUEL = Constants.FUEL;
	private static final int EXPENSE = Constants.EXPENSE;
	private static final int NOTE = Constants.NOTE;
	private static final int REPAIR = Constants.REPAIR;

	private LayoutInflater inflater;

	private List<ItemLog> itens;

	private Typeface tf; // font

	private Settings set;

	public ReportAdapter(Activity context, List<ItemLog> itensForAdapter,
			Settings set) {
		// Log.i(TAG, "## charge ListItemAdapter ##");

		try {

			this.itens = filter(itensForAdapter);

			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			tf = Typeface.createFromAsset(context.getAssets(),
					"fonts/DroidSansFallback.ttf"); // font

			this.set = set;

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Filtra a lista 
	 * No caso, retira todos os itens do tipo NOTE da lista (apenas para exibição do adapter)
	 * 
	 * @param itens
	 * @return
	 */
	@SuppressWarnings("null")
	public List<ItemLog> filter(List<ItemLog> itensForAdapter) {
		List<ItemLog> newList = new ArrayList<ItemLog>();

		for (int i = 0; i < itensForAdapter.size(); i++) {

			ItemLog item = itensForAdapter.get(i);

			if (item.getType() != NOTE) {
				// Log.i("appLog","inserindo na lista o item >> "+item.getId());
				newList.add(item);
			}

		}

		return newList;
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

	private ItemLog itemRequest;

	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;

		itemRequest = itens.get(position);

		if (view == null) {
			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();

			// Busca o layout para cada item
			view = inflater.inflate(R.layout.item_report, null);
			view.setTag(holder); // seta a tag
			view.setId(position);

			holder.icone = (ImageView) view.findViewById(R.id.icone);

			holder.det = (TextView) view.findViewById(R.id.det);
			holder.val = (TextView) view.findViewById(R.id.val);
			holder.uni = (TextView) view.findViewById(R.id.uni);

			holder.line = (View) view.findViewById(R.id.line);

		} else {
			// Ja existe no cache, bingo entao pega!
			try {
				holder = (ViewHolder) view.getTag();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		// calcula qtd de litro abastecido
		
		if (itemRequest.getType() == Constants.FUEL) {
			Double totalUnid = null;
			totalUnid = Math.floor(itemRequest.getValue_p()
					/ itemRequest.getValue_u());

			// holder.uni.setText(String.valueOf(totalUnid.intValue()) + " " +
			// set.getVolume());
			holder.uni.setText(AndroidUtils.pad(totalUnid.intValue()));
			holder.uni.setTypeface(tf);

		}

		// detail
		// if (itemRequest.getType() == Constants.EXPENSE||
		// itemRequest.getType() == Constants.REPAIR|| itemRequest.getType() ==
		// Constants.NOTE) {

		int LIMITE = 8;
		if (itemRequest.getSubject().toString().length() > LIMITE) {
			StringBuffer sbSubjec = new StringBuffer();
			for (int i = 0; i < LIMITE-2; i++) {
				sbSubjec.append(itemRequest.getSubject().charAt(i));
			}
			holder.det.setText((sbSubjec.toString() + "...").toLowerCase());
		} else {
			holder.det.setText(String.valueOf(itemRequest.getSubject())
					.toLowerCase());
		}

		holder.det.setTypeface(tf);
		// }

		// value p (valor total que pagou)
		if (itemRequest.getType() == Constants.EXPENSE
				|| itemRequest.getType() == Constants.REPAIR
				|| itemRequest.getType() == Constants.FUEL) {

			holder.val.setText(set.getMoeda() + " "
					+ String.valueOf(itemRequest.getValue_p()));
			holder.val.setTypeface(tf);

		}

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

		// verifica se deve inserir a linha
		if (position > 0) {
			ItemLog itemOld = itens.get(position - 1);
			if (itemOld.getType() != itemRequest.getType()) {
				holder.line.setVisibility(View.VISIBLE);
			}
		}

		return view;
	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		ImageView icone; // icone do tipo
		TextView det; // detalhe do campo (subject)
		TextView val; // valor do item
		TextView uni; // unidade de medida (litros)
		View line;
	}
}