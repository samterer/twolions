package br.com.maboo.node.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.maboo.node.R;
import br.livroandroid.utils.RoundedShape;

import com.facebook.scrumptious.auxiliar.FaceUserVO;

public class MenuListAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	String[] mTitle;
	String[] mSubTitle;
	int[] mIcon;
	LayoutInflater inflater;

	public MenuListAdapter(Context context, String[] title, String[] subtitle,
			int[] icon) {
		this.context = context;
		this.mTitle = title;
		this.mSubTitle = subtitle;
		this.mIcon = icon;
	}

	@Override
	public int getCount() {
		return mTitle.length;
	}

	@Override
	public Object getItem(int position) {
		return mTitle[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView txtTitle;
		TextView txtSubTitle;
		ImageView imgIcon;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int id_layout = 0;
		// muda o layout do menu se for para o "profile" do usuario
		if (position == 0) {
			id_layout = R.layout.drawer_list_item_face;
		} else { // item comum do menu
			id_layout = R.layout.drawer_list_item;
		}

		View itemView = inflater.inflate(id_layout, parent, false);

		// Locate the TextViews in drawer_list_item.xml
		txtTitle = (TextView) itemView.findViewById(R.id.title);
		txtSubTitle = (TextView) itemView.findViewById(R.id.subtitle);

		// Locate the ImageView in drawer_list_item.xml
		imgIcon = (ImageView) itemView.findViewById(R.id.icon);

		// Set the results into TextViews
		txtTitle.setText(mTitle[position]);
		txtSubTitle.setText(mSubTitle[position]);

		// o primeiro item é sempre a foto do profile
		// padrao do layout do app
		if (position == 0) {
			// recupera profila do usuario
			imgIcon = (ImageView) itemView.findViewById(R.id.icon);

			RoundedShape rs = new RoundedShape(FaceUserVO.profilePicture);
			imgIcon.setImageBitmap(rs.getTargetBitmap());

		} else {
			// Set the results into ImageView
			imgIcon.setImageResource(mIcon[position]);
		}

		return itemView;
	}

}
