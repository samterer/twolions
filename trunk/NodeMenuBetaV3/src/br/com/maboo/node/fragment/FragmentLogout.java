package br.com.maboo.node.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Session;

public class FragmentLogout extends SherlockFragment {

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragmentlogout, container, false);

		// desloga do facebook
		Session.getActiveSession().closeAndClearTokenInformation();

		// fecha a activity
		Activity parentActivity = getActivity();
		parentActivity.finish();
		

		return view;
	}

}
