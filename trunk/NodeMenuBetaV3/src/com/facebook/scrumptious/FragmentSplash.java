package com.facebook.scrumptious;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.maboo.node.R;

public class FragmentSplash extends Fragment {

	private static final String TAG = "SplashFragment";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.splash, container, false);

		// esconde a actionBar
		getActivity().getActionBar().hide();

		// recupera o bt de logon do facebook
		com.facebook.widget.LoginButton lb = (com.facebook.widget.LoginButton) view
				.findViewById(R.id.login_button);

		// exibe facebook bt
		lb.setVisibility(View.VISIBLE);

		return view;
	}

}