package br.com.maboo.node.nodemenubeta.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.friend.BaseListElement;

public class ListFriendAdapter extends BaseAdapter {

	private String TAG = "ListFriendAdapter";

	private LayoutInflater inflater;

	private List<BaseListElement> itens;

	private Typeface tf; // font

	public ListFriendAdapter(Fragment context, List<BaseListElement> itens) {

		this.itens = itens;

		Log.i(TAG, "verificando a lista...");
		for (final BaseListElement b : itens) {

			Log.i(TAG, "graphUser: " + b.getText1());

			b.setAdapter(this);

		}

		this.inflater = (LayoutInflater) context.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		tf = Typeface.createFromAsset(context.getActivity().getAssets(),
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

		BaseListElement itemRequest = itens.get(position);

		if (holder == null) { // verifica se o holder existe

			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();

			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_friend;
			view = inflater.inflate(layout, null);
			view.setTag(holder); // seta a tag
			view.setId(position);

			holder.icon = (ImageView) view.findViewById(R.id.icon);

			holder.id = (TextView) view.findViewById(R.id.id);
			holder.id.setTypeface(tf);

			holder.text1 = (TextView) view.findViewById(R.id.text1);
			holder.text1.setTypeface(tf);

			holder.text2 = (TextView) view.findViewById(R.id.text2);
			holder.text2.setTypeface(tf);

		} else {

			// Ja existe no cache, bingo entao pega!
			try {

				holder = (ViewHolder) view.getTag();

			} catch (NullPointerException e) {

				e.printStackTrace();

			}

		}

		// set background no fundo do item
		//holder.bgItem.setBackgroundColor(Color.parseColor("#aa55aa"));

		// profile pic
		//holder.icon.setImageDrawable(itemRequest.getIcon());

		// subject
		holder.id.setText(String.valueOf(itemRequest.getId()));
		holder.text1.setText(String.valueOf(itemRequest.getText1()));
		holder.text2.setText(String.valueOf(itemRequest.getText2()));

		Log.i(TAG, "exibindo item: " + position);

		return view;
	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		RelativeLayout bgItem;
		ImageView icon;
		TextView id;
		TextView text1;
		TextView text2;
		boolean check;
	}
}