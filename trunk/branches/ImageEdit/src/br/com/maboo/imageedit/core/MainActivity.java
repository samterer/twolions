package br.com.maboo.imageedit.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.camera.MakePhoto;
import br.com.maboo.imageedit.model.DrawableId;

public class MainActivity extends Activity {

	String pageData[]; // Stores the text to swipe.
	LayoutInflater inflater; // Used to create individual pages
	ViewPager vp; // Reference to class to swipe views

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		// Get the data to be swiped through
		pageData = getResources().getStringArray(R.array.masks);

		// get an inflater to be used to create single pages
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Reference ViewPager defined in activity
		vp = (ViewPager) findViewById(R.id.viewPager);

		// set the adapter that will create the individual pages
		vp.setAdapter(new MyPagesAdapter());

	}

	// Implement PagerAdapter Class to handle individual page creation
	class MyPagesAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			// Return total pages, here one for each data item
			return pageData.length;
		}

		// Create the given page (indicated by position)
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View page = inflater.inflate(R.layout.page, null);

			// cast img
			ImageView iView = (ImageView) page.findViewById(R.id.img);
			iView.setImageDrawable(getResources().getDrawable(
					DrawableId.LIST[position]));

			iView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(MainActivity.this, "mask: " + position,
							Toast.LENGTH_SHORT).show();
					Intent i = new Intent(MainActivity.this, MakePhoto.class);
					i.putExtra("id", position);
					startActivity(i);

				}
			});

			// Add the page to the front of the queue
			((ViewPager) container).addView(page, 0);

			return page;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// See if object from instantiateItem is related to the given view
			// required by API
			return arg0 == (View) arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
			object = null;
		}

	}
}