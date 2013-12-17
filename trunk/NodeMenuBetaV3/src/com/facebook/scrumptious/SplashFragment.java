package com.facebook.scrumptious;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Session;

public class SplashFragment extends Fragment {

	private static final String TAG = "SplashFragment";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.splash, container, false);

		// esconde a actionBar
		getActivity().getActionBar().hide();

		com.facebook.widget.LoginButton lb = (com.facebook.widget.LoginButton) view
				.findViewById(R.id.login_button);

		/*lb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getActivity().finish();
			}
		});*/

		return view;
	}
}
