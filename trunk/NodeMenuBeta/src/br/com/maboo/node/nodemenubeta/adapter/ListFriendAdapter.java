package br.com.maboo.node.nodemenubeta.adapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.facebook.friend.FriendElement;

public class ListFriendAdapter extends BaseAdapter {

	private String TAG = "ListFriendAdapter";

	private LayoutInflater inflater;

	List<FriendElement> friends = new ArrayList<FriendElement>();

	private Typeface tf; // font

	public ListFriendAdapter(Fragment context, List<FriendElement> friends) {

		this.friends = friends;

		this.inflater = (LayoutInflater) context.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		tf = Typeface.createFromAsset(context.getActivity().getAssets(),
				"fonts/DroidSansFallback.ttf"); // font
	}

	public int getCount() {
		return friends != null ? friends.size() : 0;
	}

	public Object getItem(int position) {
		return friends != null ? friends.get(position) : null;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;

		FriendElement itemRequest = friends.get(position);

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

			holder.nome = (TextView) view.findViewById(R.id.nome);
			holder.nome.setTypeface(tf);

			holder.text = (TextView) view.findViewById(R.id.text);
			holder.text.setTypeface(tf);

		} else {

			// Ja existe no cache, bingo entao pega!
			try {

				holder = (ViewHolder) view.getTag();

			} catch (NullPointerException e) {

				e.printStackTrace();

			}

		}

		// set background no fundo do item
		// holder.bgItem.setBackgroundColor(Color.parseColor("#aa55aa"));

		// profile pic
		holder.icon = itemRequest.getIcon();

		// subject
		holder.id.setText(String.valueOf(itemRequest.getId()));
		holder.nome.setText(String.valueOf(itemRequest.getNome()));
		holder.text.setText(String.valueOf(itemRequest.getText()));

		Log.i(TAG, "exibindo item: " + position);

		return view;
	}

	public Bitmap getBitmap(String bitmapUrl) {

		try {

			URL url = new URL(bitmapUrl);

			return BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());

		}

		catch (Exception ex) {
			return null;
		}

	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		RelativeLayout bgItem;
		ImageView icon;
		TextView id;
		TextView nome;
		TextView text;
		boolean check;
	}
}