package br.com.maboo.node.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.maboo.node.R;
import br.com.maboo.node.application.FriendsApplication;
import br.livroandroid.utils.DownloadImagemUtil;

import com.facebook.friend.FriendElement;

public class ListFriendAdapter extends BaseAdapter implements Filterable {

	private String TAG = "ListMapAddrAdapter";

	private LayoutInflater inflater;

	private List<FriendElement> originalList = new ArrayList<FriendElement>();

	private Typeface tf; // font

	private DownloadImagemUtil downloader;

	private Activity context;

	public ListFriendAdapter(Activity context, List<FriendElement> originalList) {

		// Log.i(TAG, "ListMapAddrAdapter...");
		try {
			// instance
			this.context = context;

			// lists
			this.originalList = originalList;

			// inflate
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// get application (request web)
			FriendsApplication application = (FriendsApplication) context
					.getApplication();

			// Utiliza este objeto para recuperar a classe que faz o download de
			// imagens
			downloader = application.getDownloadImagemUtil();

			// fonte
			tf = Typeface.createFromAsset(context.getAssets(),
					"fonts/DroidSansFallback.ttf"); // font

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public int getCount() {
		return originalList != null ? originalList.size() : 0;
	}

	public Object getItem(int position) {
		return originalList != null ? originalList.get(position) : null;
	}

	public long getItemId(int position) {
		return Long.valueOf(originalList.get(position).getId());
	}

	/*
	 * limpa o adapter e a list
	 */
	public void clearAdapter() {
		originalList.clear();
		notifyDataSetChanged();
	}

	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;

		if (view == null) { // verifica se o holder existe

			// Log.i(TAG,"criando a view...");

			// Nao existe a View no cache para esta linha ent�o cria um novo
			holder = new ViewHolder();

			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_friend;
			view = inflater.inflate(layout, null);
			// seta a tag
			view.setTag(holder);

			// come�a a carregar a view
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

		FriendElement request = originalList.get(position);

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

	// filtro
	public Filter getFilter() {
		Filter filtro = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults filtroResultado = new FilterResults();

				if (constraint == null || constraint.length() == 0) {

					// Se nao tiver nada para filtrar entao etorna a lista
					// completa
					filtroResultado.values = originalList;
					filtroResultado.count = originalList.size();
					return filtroResultado;
				} else {

					List<FriendElement> auxFriends = new ArrayList<FriendElement>();

					for (FriendElement p : originalList) {
						if (p.getNome().toUpperCase()
								.contains(constraint.toString().toUpperCase())) {
							auxFriends.add(p);
						}
					}

					filtroResultado.values = auxFriends;
					filtroResultado.count = auxFriends.size();
				}

				return filtroResultado;
			} // FIm do performFiltering

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults resultado) {

				// Temos que informar a nova lista
				if (resultado.count == 0)

					// Notifica os ouvintes
					notifyDataSetInvalidated();

				else {
					// Preencho a lista(originalList) do adapter com o novo
					// valor
					originalList = (List<FriendElement>) resultado.values;

					// Notifica ovites apos a lista ter novos valores
					notifyDataSetChanged();
				}

			} // Fim do publishResults

		};
		return filtro;
	}

}