package br.com.maboo.node.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.maboo.node.R;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentSettings extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmentsettings, container,
				false);

		return view;
	}
}
