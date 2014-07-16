package br.com.maboo.imageedit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.model.Masks;
import br.com.maboo.imageedit.util.AnimUtil;

public class ImageSwapActivity extends Activity {

	private LayoutInflater inflater; // Used to create individual pages

	private ViewPager vp; // Reference to class to swipe views
	
	private ImageView mCurtainLeft, mCurtainRight, mLogoBig, mLogoBit, mFooterHosp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_swap);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vp = (ViewPager) findViewById(R.id.viewPager);
		vp.setAdapter(new CustomPagerAdapter());

		RelativeLayout layoutLoad = (RelativeLayout) findViewById(R.id.layout_stage);
		layoutLoad.setVisibility(View.VISIBLE);

		mLogoBig = (ImageView) findViewById(R.id.logo_big);
		mLogoBig.setVisibility(View.VISIBLE);

		hideStage(); // hide the stage in screen
	}

	private void hideStage() {
		mLogoBig = (ImageView) findViewById(R.id.logo_big);
		mCurtainLeft = (ImageView) findViewById(R.id.curtain_left);
		mCurtainRight = (ImageView) findViewById(R.id.curtain_right);
		mLogoBit = (ImageView) findViewById(R.id.logo_bit);
		mFooterHosp = (ImageView) findViewById(R.id.footer_hosp);

		AnimUtil.getInstance(this).animeCurtainOut(mCurtainLeft, mCurtainRight,
				mLogoBig, mLogoBit, mFooterHosp);
	}

	/**
	 * PagerAdapter Class to handle individual page creation
	 */
	class CustomPagerAdapter extends PagerAdapter implements OnPageChangeListener {
		@Override
		public int getCount() {
			return Masks.LIST_PHOTO_SWAP.length;
		}

		// Create the given page (indicated by position)
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View page = inflater.inflate(R.layout.photo_swap, null);

			// cast img
			ImageView iView = (ImageView) page.findViewById(R.id.item);

			iView.setImageDrawable(getResources().getDrawable(
					Masks.LIST_PHOTO_SWAP[position]));

			iView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position < 1) { // block click in tutorial
						return;
					}
					goToMakePhoto(position);
				}
			});

			// Add the page to the front of the queue
			((ViewPager) container).addView(page, isFirst());

			return page;
		}

		private int isFirst() {
			int first = 0;
			if (isShowTutorial()) {
				first = 0;
			} else {
				first = 1;
			}
			return first;
		}

		private boolean isShowTutorial() {
			boolean result = true;

			// veirify properties

			return result;
		}
		
		private void goToMakePhoto(int pos) {
			Intent i = new Intent(ImageSwapActivity.this,
					MakePhotoActivity.class);
			i.putExtra("id", pos);
			startActivity(i);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// See if object from instantiateItem is related to the given view
			// required by API
			return arg0 == (View) arg1;
		}
		
		private View getView(int pos){
			return vp.getChildAt(pos);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
			object = null;
		}

		@Override
		public void onPageScrollStateChanged(int pos) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageSelected(int pos) {}
	}
}