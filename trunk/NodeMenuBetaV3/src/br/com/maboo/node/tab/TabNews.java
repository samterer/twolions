package br.com.maboo.node.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.maboo.node.R;

import com.actionbarsherlock.app.SherlockFragment;

public class TabNews extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.fragmentnews, container, false);

		return view;
	}
}
