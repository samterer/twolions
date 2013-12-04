package br.com.maboo.node.nodemenubeta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.scrumptious.auxiliar.FaceUserVO;
import com.facebook.widget.ProfilePictureView;

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
		ProfilePictureView profilePictureView;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.drawer_list_item, parent,
				false);

		// Locate the TextViews in drawer_list_item.xml
		txtTitle = (TextView) itemView.findViewById(R.id.title);
		txtSubTitle = (TextView) itemView.findViewById(R.id.subtitle);

		// Locate the ImageView in drawer_list_item.xml
		imgIcon = (ImageView) itemView.findViewById(R.id.icon);
		
		// Find the user's profile picture custom view
		profilePictureView = (ProfilePictureView) itemView
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);

		// Set the results into TextViews
		txtTitle.setText(mTitle[position]);
		txtSubTitle.setText(mSubTitle[position]);

		
		// recupera profila do usuario
		float a = mIcon[position];
		float b = Float.valueOf(FaceUserVO.id_user);
		if(a == b) {
			profilePictureView.setProfileId(FaceUserVO.id_user);
			profilePictureView.setVisibility(View.VISIBLE);
			imgIcon.setVisibility(View.GONE);
		} else {
			// Set the results into ImageView
			imgIcon.setImageResource(mIcon[position]);
		}

		return itemView;
	}

}
