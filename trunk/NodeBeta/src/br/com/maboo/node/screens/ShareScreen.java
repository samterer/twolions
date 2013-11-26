package br.com.maboo.node.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.maboo.node.FragmentCircle;
import br.com.maboo.node.R;

public class ShareScreen extends FragmentCircle {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main_dummy,
				container, false);

		TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);

		dummyTextView.setText(Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));

		return rootView;
	}

}
