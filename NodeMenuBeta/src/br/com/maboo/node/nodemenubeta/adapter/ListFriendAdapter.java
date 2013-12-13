package br.com.maboo.node.nodemenubeta.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.maboo.node.nodemenubeta.FriendsApplication;
import br.livroandroid.utils.DownloadImagemUtil;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.friend.FriendElement;

public class ListFriendAdapter extends BaseAdapter {

	private String TAG = "ListFriendAdapter";

	private LayoutInflater inflater;

	List<FriendElement> friends = new ArrayList<FriendElement>();

	private Typeface tf; // font

	private DownloadImagemUtil downloader;

	private Activity context;

	public ListFriendAdapter(Activity context, List<FriendElement> friends) {
		
		//Log.i(TAG, "ListFriendAdapter...");

		this.context = context;
		this.friends = friends;

		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		FriendsApplication application = (FriendsApplication) context.getApplication();
		// Utiliza este objeto para recuperar a classe que faz o download de imagens
		downloader = application.getDownloadImagemUtil();

		tf = Typeface.createFromAsset(context.getAssets(),
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

	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;

		if (view == null) { // verifica se o holder existe
			
			//Log.i(TAG,"criando a view...");

			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();

			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_friend;
			view = inflater.inflate(layout, null);
			// seta a tag
			view.setTag(holder);

			// começa a carregar a view
			view.setId(position);

			holder.icon = (ImageView) view.findViewById(R.id.icon);
			holder.progress = (ProgressBar) view.findViewById(R.id.progressBar);

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

		holder.icon.setImageBitmap(null);

		FriendElement request = friends.get(position);

		// view carrega, vamos atualizar os valores

		// set background no fundo do item
		// holder.bgItem.setBackgroundColor(Color.parseColor("#aa55aa"));

		// id
		holder.id.setText(String.valueOf(request.getId()));
		// nome
		holder.nome.setText(String.valueOf(request.getNome()));
		// text
		holder.text.setText(String.valueOf(request.getText()));
		// profile pic
		downloader.download(context, request.getUrlPic(), holder.icon,
				holder.progress);

		return view;
	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		RelativeLayout bgItem;
		ImageView icon;
		TextView id;
		TextView nome;
		TextView text;
		ProgressBar progress;
		boolean check;
	}
}