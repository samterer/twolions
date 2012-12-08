package br.com.maboo.neext.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.maboo.neext.R;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.util.Constants;

public class ListAdapter extends BaseAdapter {

	protected static final String TAG = Constants.LOG_APP;

	private LayoutInflater inflater;

	private List<ItemNote> itens;

	private Typeface tf; // font

	public ListAdapter(Activity context, List<ItemNote> itens) {

		Log.i(TAG, "ListAdapter....");

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

		ItemNote itemRequest = itens.get(position);

		if (view == null) {

			// Nao existe a View no cache para esta linha ent�o cria um novo
			holder = new ViewHolder();

			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_note;
			view = inflater.inflate(layout, null);
			view.setTag(holder); // seta a tag
			view.setId(position);

			holder.rl = (RelativeLayout) view.findViewById(R.id.r_item_log);

			holder.imgLeftCenter = (ImageView) view
					.findViewById(R.id.imgLeftCenter);

			holder.type = itemRequest.getType();

			holder.subject = (TextView) view.findViewById(R.id.subject);
			holder.subject.setTypeface(tf);

			holder.date = (TextView) view.findViewById(R.id.date);
			holder.date.setTypeface(tf);

			holder.hour = (TextView) view.findViewById(R.id.hour);
			holder.hour.setTypeface(tf);

		} else {

			// Ja existe no cache, bingo entao pega!
			try {

				holder = (ViewHolder) view.getTag();

			} catch (NullPointerException e) {

				e.printStackTrace();

			}
		}
		
		// verifica se o item esta check ou uncheck
		if(itemRequest.isCheck()) {
			holder.rl.setBackgroundColor(view.getResources().getColor(R.color.cinza_coob_c));
			// change img
			holder.imgLeftCenter.setImageResource(R.drawable.boxe_check);
		} else {
			holder.rl.setBackgroundColor(view.getResources().getColor(R.color.pastel));
			// change img
			holder.imgLeftCenter.setImageResource(R.drawable.boxe);
		}

		// set background in image
		holder.imgLeftCenter.setBackgroundColor(Color.parseColor("#"
				+ holder.type.toString()));

		// subject
		// verifica se o subject � muito grande, se for, coloca reticencias no
		// final
		if (itemRequest.getSubject().length() > 10) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 10; i++) {
				sb.append(itemRequest.getSubject().toString().charAt(i));
			}
			holder.subject.setText(sb.toString() + "...");
		} else {
			holder.subject.setText(String.valueOf(itemRequest.getSubject()));
		}

		// formata a data
		// formata date
		String dateFromBase = itemRequest.getDate();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < dateFromBase.length(); i++) {

			if (dateFromBase.charAt(i) == '-') { // insere valor da
													// data

				// Log.i(TAG, "date [" + sb.toString() + "]");

				holder.date.setText(sb.toString());

				sb = new StringBuffer();

				i++;

			} else if (i == 15) { // insere
									// valor
									// da hora
									// o numero dessa linha � comparado a
									// 16, pois esse � o tamanho maximo
									// correto de uma data, de acordo com a
									// inser��o dela 'dd/mm/aaaa - hh:mm'
				sb.append(dateFromBase.charAt(i));

				// Log.i(TAG, "hour [" + sb.toString() + "]");

				holder.hour.setText(sb.toString()); // hora

				break;

			}

			// Log.i(TAG, "insert [" + dateFromBase.charAt(i) + "]");

			sb.append(dateFromBase.charAt(i));
		}

		return view;
	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		ImageView imgLeftCenter;
		RelativeLayout rl;
		String type;
		TextView date;
		TextView hour;
		TextView subject;
	}
}