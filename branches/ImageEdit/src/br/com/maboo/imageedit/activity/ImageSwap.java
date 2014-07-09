package br.com.maboo.imageedit.activity;

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
import br.com.maboo.imageedit.model.Masks;

public class ImageSwap extends Activity {

	private String pageData[]; // Stores the text to swipe.
	private LayoutInflater inflater; // Used to create individual pages
	private ViewPager vp; // Reference to class to swipe views

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_swap);

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
			return Masks.LIST_PHOTO_SWAP.length;
		}

		// Create the given page (indicated by position)
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View page = inflater.inflate(R.layout.page, null);

			// cast img
			ImageView iView = (ImageView) page.findViewById(R.id.img);
			
			iView.setImageDrawable(getResources().getDrawable(
					Masks.LIST_PHOTO_SWAP[position]));

			iView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(position < 1) { // block click in tutorial
						return;
					}
					Toast.makeText(ImageSwap.this, "mask: " + position,
							Toast.LENGTH_SHORT).show();
					
					Intent i = new Intent(ImageSwap.this, ActivityMakePhoto.class);
					i.putExtra("id", position);
					startActivity(i);
					
					// insere animacao das cortinas fechando

				}
			});

			// Add the page to the front of the queue
			((ViewPager) container).addView(page, getInitPhotoPos());

			return page;
		}
		
		private int getInitPhotoPos() {
			int result = 0;
			
			// ignora tutorial na segunda vez que o app é aberto
			if(isFirstOpening()) {
				result = 0;
			} else {
				result = 1;
			}
			
			return result;
		}
		
		private boolean isFirstOpening(){
			boolean result = true;
			
			// veirify properties
			
			return result;
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