package br.com.maboo.imageedit.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.model.Mask;

public class GridMaskAdapter extends BaseAdapter {

	protected static final String TAG = "appLog";

	private LayoutInflater inflater;

	private List<Mask> itens;

	private Context context;

	public GridMaskAdapter(Context context, List<Mask> itens) {

		Log.i(TAG, "ListAdapter....");

		this.context = context;

		this.itens = itens;
		
		Log.i(TAG,"itens size: "+itens.size());

		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
		Log.i(TAG,"getView");

		ViewHolder holder = null;

		Mask m = itens.get(position);

		if (holder == null) {
			
			Log.i(TAG,"holder is null");

			holder = new ViewHolder();

			int layout = R.layout.item;
			view = inflater.inflate(layout, null);
			view.setTag(holder);
			view.setId(position);

			// carrega holder
			holder.img = (ImageView) view.findViewById(R.id.img);		
			holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.img.setPadding(8, 8, 8, 8);

		} else {
			// Ja existe no cache, bingo entao pega!
			try {
				Log.i(TAG,"holder existe");
				holder = (ViewHolder) view.getTag();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		
		Log.i(TAG,"create holder");
		
		// imagem da mascara
		Drawable d = context.getResources().getDrawable(m.getDrawableId());
		Log.i(TAG,"d: " +d.getCurrent().toString());
		
		holder.img.setImageDrawable(d);
		
		// nome da imagem
		holder.name = m.getName();

		return view;
	}

	// Design Patter "ViewHolder" para Android
	class ViewHolder {
		ImageView img;
		String name;
	}
}