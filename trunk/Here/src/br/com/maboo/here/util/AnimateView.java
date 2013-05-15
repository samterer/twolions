package br.com.maboo.here.util;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import br.com.maboo.here.R;

public class AnimateView extends View {

	public AnimateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void doSlideDown(View view) {
		RelativeLayout myView = (RelativeLayout) findViewById(R.id.view);
		// addListingView.setVisibility(myView.VISIBLE);

		Animation slideDown = setLayoutAnim_slidedown();
		myView.startAnimation(slideDown);
	}

	public void doSlideUp(View view) {
		RelativeLayout myView = (RelativeLayout) findViewById(R.id.view);

		Animation slideUp = setLayoutAnim_slideup();
		myView.startAnimation(slideUp);

	}

	public Animation setLayoutAnim_slidedown() {

		AnimationSet set = new AnimationSet(true);

		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(800);
		animation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// MapContacts.this.mapviewgroup.setVisibility(View.VISIBLE);

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Log.d("LA", "sliding down ended");

			}
		});
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);

		return animation;
	}

	public Animation setLayoutAnim_slideup() {

		AnimationSet set = new AnimationSet(true);

		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f);
		animation.setDuration(800);
		animation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				RelativeLayout bodyView = (RelativeLayout) findViewById(R.id.view_body);
				RelativeLayout myView = (RelativeLayout) findViewById(R.id.view);
				// addListingView.clearAnimation();
				bodyView.removeView(myView);
			}
		});
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);

		return animation;

	}
}
