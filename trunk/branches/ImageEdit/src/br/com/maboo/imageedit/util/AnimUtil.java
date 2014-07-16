package br.com.maboo.imageedit.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.activity.ImageSwapActivity;

public class AnimUtil implements AnimationListener {

	private static AnimUtil instance;

	public static final int MOVE_LOGO_BIT_IN = 1;
	public static final int MOVE_CURTAINS_IN = 2;
	public static final int MOVE_CURTAINS_OUT = 3;
	public static final int MOVE_LOGO_BIG_IN_OPEN_SWAP = 4;
	public static final int MOVE_LOGO_BIG_OUT = 5;
	public static final int MOVE_FOOTER_HOSP_IN = 6;

	private static int mFirstAnimId = 0;
	private static int mSecondAnimId = 0;

	private static Activity mActivity;

	private static ImageView mLogoBig, mLogoBit;
	private static ImageView mCurtainLeft, mCurtainRight;
	private static ImageView mFooterHosp;

	private Handler mHandler;

	public static AnimUtil getInstance(Activity activity) {
		if (instance == null) {
			instance = new AnimUtil();
		}
		clear();

		mActivity = activity;

		return instance;
	}

	private static void clear() {
		mLogoBig = null;
		mLogoBit = null;
		mCurtainLeft = null;
		mCurtainRight = null;
		mFooterHosp = null;
	}

	public void animeLogoBigIn(ImageView logoBig) {
		mFirstAnimId = MOVE_LOGO_BIG_IN_OPEN_SWAP;

		Animation move_logo_big_in = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_logo_big_in);
		move_logo_big_in.setAnimationListener(this);
		logoBig.startAnimation(move_logo_big_in);
	}

	public void animeLogoBigOut(ImageView logoBig) {
		mFirstAnimId = MOVE_LOGO_BIG_OUT;

		Animation move_logo_big_out = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_logo_big_out);
		move_logo_big_out.setAnimationListener(this);
		logoBig.startAnimation(move_logo_big_out);
	}

	public void animeCurtainOut(ImageView curtainLeft, ImageView curtainRight,
			ImageView logoBig, ImageView logoBit, ImageView footerHosp) {
		mSecondAnimId = MOVE_LOGO_BIT_IN;

		mLogoBit = logoBit;
		mFooterHosp = footerHosp;

		animeCurtainOut(curtainLeft, curtainRight, logoBig);
	}

	public void animeCurtainOut(ImageView curtainLeft, ImageView curtainRight,
			ImageView logoBig) {
		mFirstAnimId = MOVE_CURTAINS_OUT;

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

		animeLogoBigOut(logoBig);
	}

	public void animeCurtainIn(ImageView curtainLeft, ImageView curtainRight,
			ImageView logoBig) {
		mFirstAnimId = MOVE_CURTAINS_IN;

		mLogoBig = logoBig;
		mCurtainLeft = curtainLeft;
		mCurtainRight = curtainRight;

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
		mFirstAnimId = MOVE_LOGO_BIT_IN;

		Animation move_logo_bit = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_logo_bit_in);

		move_logo_bit.setAnimationListener(this);

		logoBit.startAnimation(move_logo_bit);
	}

	public void animeFooterHosp(ImageView footerHosp) {
		mFirstAnimId = MOVE_FOOTER_HOSP_IN;

		Animation move_footer_hosp = AnimationUtils.loadAnimation(
				mActivity.getApplicationContext(), R.anim.move_footer_hosp_up);

		move_footer_hosp.setAnimationListener(this);

		footerHosp.startAnimation(move_footer_hosp);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// first animation end
		switch (mFirstAnimId) {
		case MOVE_CURTAINS_IN: // down big logo
			if (mLogoBig != null) {
				mLogoBig.setVisibility(View.VISIBLE);
				animeLogoBigIn(mLogoBig);
			}
			break;

		case MOVE_LOGO_BIG_IN_OPEN_SWAP: // open image swap activity
			if (mActivity != null) {
				mHandler = new Handler();
				mHandler.postDelayed(openSwapTask, 2000l);
			}
			break;
		case MOVE_LOGO_BIG_OUT:
			if (mLogoBig != null)
				mLogoBig.setVisibility(View.GONE);
			if (mCurtainLeft != null)
				mCurtainLeft.setVisibility(View.GONE);
			if (mCurtainRight != null)
				mCurtainRight.setVisibility(View.GONE);
			if (mSecondAnimId == MOVE_LOGO_BIT_IN) {
				if (mLogoBit != null)
					mLogoBit.setVisibility(View.VISIBLE);
				animeLogoBitIn(mLogoBit);
				if (mFooterHosp != null)
					mFooterHosp.setVisibility(View.VISIBLE);
				animeFooterHosp(mFooterHosp);
			}
			break;
		}

	}

	private Runnable openSwapTask = new Runnable() {
		@Override
		public void run() {
			Intent intent = new Intent(mActivity, ImageSwapActivity.class);
			mActivity.startActivity(intent);
			mActivity.finish();

			mHandler.removeCallbacks(openSwapTask);
		}
	};

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

}
