package br.com.maboo.imageedit.util;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import br.com.maboo.imageedit.R;

public class AnimUtil implements AnimationListener {

	private final int MOVE_LOGO_BIT = 1;
	private final int MOVE_CURTAINS_IN = 2;
	private final int MOVE_CURTAINS_OUT = 3;
	private final int MOVE_LOGO_BIG_IN = 4;
	private final int MOVE_LOGO_BIG_OUT = 5;
	private final int MOVE_FOOTER_HOSP = 6;

	private int mAnimId = 0;

	private static Activity mActivity;
	
	private ImageView mLogoBig;

	public static AnimUtil getInstance(Activity activity) {
		if (mActivity == null) {
			mActivity = activity;
		}
		return new AnimUtil();
	}

	public void animeLogoBigIn(ImageView logoBig) {
		mAnimId = MOVE_LOGO_BIG_IN;

		Animation move_logo_big_in = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_logo_big_in);
		move_logo_big_in.setAnimationListener(this);
		logoBig.startAnimation(move_logo_big_in);
	}

	public void animeLogoBigOut(ImageView logoBig) {
		mAnimId = MOVE_LOGO_BIG_OUT;

		Animation move_logo_big_out = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_logo_big_out);
		move_logo_big_out.setAnimationListener(this);
		logoBig.startAnimation(move_logo_big_out);
	}

	public void animeCurtainOut(ImageView curtainLeft, ImageView curtainRight, ImageView logoBig) {
		mAnimId = MOVE_CURTAINS_OUT;

		Animation move_curtain_out_left = AnimationUtils
				.loadAnimation(mActivity.getApplicationContext(),
						R.anim.move_curtain_out_left);
		move_curtain_out_left.setAnimationListener(this);
		curtainLeft.startAnimation(move_curtain_out_left);

		Animation move_curtain_out_right = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(),
				R.anim.move_curtain_out_right);
		move_curtain_out_right.setAnimationListener(this);
		curtainRight.startAnimation(move_curtain_out_right);
	}

	public void animeCurtainIn(ImageView curtainLeft, ImageView curtainRight, ImageView logoBig) {
		mAnimId = MOVE_CURTAINS_IN;
		
		mLogoBig = logoBig;

		Animation move_curtain_in_left = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_curtain_in_left);
		move_curtain_in_left.setAnimationListener(this);
		curtainLeft.startAnimation(move_curtain_in_left);

		Animation move_curtain_in_right = AnimationUtils
				.loadAnimation(mActivity.getApplicationContext(),
						R.anim.move_curtain_in_right);
		move_curtain_in_right.setAnimationListener(this);
		curtainRight.startAnimation(move_curtain_in_right);
	}

	public void animeLogoBitIn(ImageView logoBit) {
		mAnimId = MOVE_LOGO_BIT;

		Animation move_logo_bit = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_logo_bit_in);

		move_logo_bit.setAnimationListener(this);

		logoBit.startAnimation(move_logo_bit);
	}

	public void animeFooterHosp(ImageView footerHosp) {
		mAnimId = MOVE_FOOTER_HOSP;

		Animation move_footer_hosp = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_footer_hosp_up);

		move_footer_hosp.setAnimationListener(this);

		footerHosp.startAnimation(move_footer_hosp);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// ao fechar cortinas desce o logo grande
		if(mAnimId == MOVE_CURTAINS_IN) {
			if(mLogoBig != null)
			mLogoBig.setVisibility(View.VISIBLE);
			animeLogoBigIn(mLogoBig);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

}
