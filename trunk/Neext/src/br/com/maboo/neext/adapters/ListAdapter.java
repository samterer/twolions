package br.com.maboo.neext.adapters;

import java.util.List;

import javax.security.auth.Subject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.maboo.neext.R;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.util.AndroidUtils;
import br.com.maboo.neext.util.Constants;

public class ListAdapter extends BaseAdapter implements AnimationListener {

	protected static final String TAG = Constants.LOG_APP;

	private LayoutInflater inflater;

	private List<ItemNote> itens;

	private Typeface tf; // font

	private Context context;

	public ListAdapter(Activity context, List<ItemNote> itens) {

		// Log.i(TAG, "ListAdapter....");

		this.context = context;

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

	@SuppressWarnings("unused")
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;

		ItemNote itemRequest = itens.get(position);

		if (holder == null) { // verifica se o holder existe

			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();

			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_note_test;
			view = inflater.inflate(layout, null);
			view.setTag(holder); // seta a tag
			view.setId(position);

			// check
			holder.check = itemRequest.isCheck();

			holder.bgItem = (RelativeLayout) view.findViewById(R.id.r_item_log);

			holder.imgLeftCenter = (ImageView) view
					.findViewById(R.id.imgLeftCenter);

			holder.type = itemRequest.getType();

			holder.subject = (TextView) view.findViewById(R.id.subject);
			holder.subject.setTypeface(tf);

			//holder.date = (TextView) view.findViewById(R.id.date);
			//holder.date.setTypeface(tf);
			
			/** testando novo layout de data **/
			holder.day = (TextView) view.findViewById(R.id.day);
			holder.day.setTypeface(tf);
			
			holder.month = (TextView) view.findViewById(R.id.month);
			holder.month.setTypeface(tf);
			
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

		holder.imgLeftCenter.setImageResource(R.drawable.boxe_check); // esta
																		// como
																		// box
																		// check
																		// para
																		// alinhar
																		// os
																		// itens
																		// (a
																		// imagem
																		// alinha
																		// os
																		// itens)

		// verifica se o item esta check ou uncheck
		if (holder.check == true) {

			// cria a linha no meio do item
			holder.subject.setPaintFlags(holder.subject.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);

		}

		// color type
		if (holder.type.toString().charAt(0) != '#') {
			holder.type = "#" + holder.type;
		}

		// set background no fundo do item
		holder.bgItem.setBackgroundColor(Color.parseColor(holder.type
				.toString()));

		// subject
		holder.subject.setText(String.valueOf(itemRequest.getSubject()));

		// formata a data
		// formata date
		String dateFromBase = itemRequest.getDate();

		StringBuffer sb = new StringBuffer();
		String date_full = "";
		int day = 0;
		int month = 0;
		int year = 0;
		for (int i = 0; i < dateFromBase.length(); i++) {

			if (dateFromBase.charAt(i) == '-') { // insere valor da
													// data
				date_full = sb.toString();
				sb = new StringBuffer();
				i++;

				// insere valor da hora o numero dessa linha é comparado a 16,
				// pois esse é o tamanho maximo
				// correto de uma data, de acordo com a inserção dela
				// 'dd/mm/aaaa - hh:mm'
			} else if (i == 15) {
				sb.append(dateFromBase.charAt(i));

				holder.hour.setText(sb.toString()); // hora
				break;

			}

			sb.append(dateFromBase.charAt(i));
		}
		
		/** aplicando data por extenso	**/
		//Log.i("appLog","titulo: "+date_full.toString());
		//Log.i("appLog","data completa: "+String.valueOf(itemRequest.getSubject()));
		
		day = Integer.valueOf(date_full.substring(0, 2)).intValue(); // get only day
		month = Integer.valueOf(date_full.substring(3, 5)).intValue(); // get only month
		year = Integer.valueOf(date_full.substring(8, 10)).intValue(); // get only month - 04/12/2013
		
		//Log.i("appLog","2. day: "+day);		
		//Log.i("appLog","2. month: "+month);
		
		holder.day.setText(""+day);	
		
		// seleciona primeiras 3 letras do mes
		holder.month.setText(AndroidUtils.getMonth(month).substring(0, 3).toLowerCase()+"/"+year);

		return view;
	}

	public void onAnimationStart(Animation animation) {
		// Toast.makeText(context, "Animation started",
		// Toast.LENGTH_SHORT).show();
	}

	public void onAnimationEnd(Animation animation) {
		// Toast.makeText(context, "Animation ended",
		// Toast.LENGTH_SHORT).show();
	}

	public void onAnimationRepeat(Animation animation) {
		// Toast.makeText(context, "Animation rep", Toast.LENGTH_SHORT).show();
	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		ImageView imgLeftCenter;
		RelativeLayout bgItem;
		String type;
		//TextView date;
		TextView day;
		TextView month;
		TextView hour;
		TextView subject;
		boolean check;
	}
}