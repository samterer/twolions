package br.com.twolions.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.twolions.R;
import br.com.twolions.daoobjects.Carro;

public class CarListAdapter extends BaseAdapter {
	protected static final String TAG = "appLog";
	private LayoutInflater inflater;
	private final List<Carro> carros;

	public CarListAdapter(Activity context, List<Carro> carros) {
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
		if (view == null) {
			// Nao existe a View no cache para esta linha então cria um novo
			holder = new ViewHolder();
			// Busca o layout para cada carro com a foto
			int layout = R.layout.item_car;
			view = inflater.inflate(layout, null);
			view.setTag(holder);
			holder.date = (TextView) view.findViewById(R.id.date);
			holder.nome = (TextView) view.findViewById(R.id.nome);
			holder.placa = (TextView) view.findViewById(R.id.placa);
			holder.tipo = (ImageView) view.findViewById(R.id.tipo);
		} else {
			// Ja existe no cache, bingo entao pega!
			holder = (ViewHolder) view.getTag();
		}

		Carro c = carros.get(position);
		holder.nome.setText(c.getNome());
		holder.placa.setText(c.getPlaca());

		if (c.getTipo().equals("carro")) {
			holder.tipo.setImageResource(R.drawable.type_car_on);
		} else {
			holder.tipo.setImageResource(R.drawable.type_moto_on);
		}

		return view;
	}

	// Design Patter "ViewHolder" para Android
	static class ViewHolder {
		TextView date;
		TextView nome;
		TextView placa;
		ImageView tipo;
	}
}
