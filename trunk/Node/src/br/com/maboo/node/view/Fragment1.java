package br.com.maboo.node.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.maboo.node.R;

import com.actionbarsherlock.app.SherlockFragment;

public class Fragment1 extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment1, container, false);
		return rootView;
	}
	
}
