package br.com.twolions.object;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.twolions.R;

public class CarroAdapter extends BaseAdapter {
	protected static final String TAG = "appLog";
	private LayoutInflater inflater;
	private final List<Carro> carros;
	private final Activity context;

	public CarroAdapter(Activity context, List<Carro> carros) {
		this.context = context;
		this.carros = carros;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return carros != null ? carros.size() : 0;
	}

	public Object getItem(int position) {
		return carros != null ? carros.get(position) : null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;

		Log.i("appLog", "getView position: " + position);

		if (view == null) {
			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();
			// Busca o layout para cada carro com a foto
			int layout = R.layout.carro_item;
			view = inflater.inflate(layout, null);
			view.setTag(holder);
			holder.tNome = (TextView) view.findViewById(R.id.tNome);
			holder.progress = (ProgressBar) view.findViewById(R.id.progress);
		} else {
			// Ja existe no cache, bingo entao pega!
			holder = (ViewHolder) view.getTag();
		}
		Carro c = carros.get(position);
		// Agora que temos a view atualizada os valores
		holder.tNome.setText(c.nome);
		return view;
	}

	// Design Patter "ViewHolder" para Android
	static class ViewHolder {
		TextView tNome;
		ProgressBar progress;
	}
}
