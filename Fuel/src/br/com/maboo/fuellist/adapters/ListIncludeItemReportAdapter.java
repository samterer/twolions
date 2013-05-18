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
import br.com.maboo.fuellist.R;
import br.com.maboo.fuellist.modelobj.ItemLog;
import br.com.maboo.fuellist.modelobj.Settings;
import br.com.maboo.fuellist.util.Constants;

public class ListIncludeItemReportAdapter extends BaseAdapter {

	protected static final String TAG = "appLog";

	private LayoutInflater inflater;

	private List<ItemLog> itens;

	private Typeface tf; // font

	private Settings set;

	public ListIncludeItemReportAdapter(Activity context, List<ItemLog> itens, Settings set) {
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
			int layout = R.layout.item_report;
			view = inflater.inflate(layout, null);
			view.setTag(holder); // seta a tag
			view.setId(position);

			holder.icone = (ImageView) view.findViewById(R.id.imgLeftCenter);

		} else {
			// Ja existe no cache, bingo entao pega!
			try {
				holder = (ViewHolder) view.getTag();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
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

		return view;
	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		BaseAdapter listIncludeItemAdapter;
		ImageView icone;
	}
}