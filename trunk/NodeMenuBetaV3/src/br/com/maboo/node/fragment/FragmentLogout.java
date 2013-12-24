package br.com.maboo.node.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.maboo.node.R;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Session;

public class FragmentLogout extends SherlockFragment {

	View view;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragmentlogout, container, false);

		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle(this.getString(R.string.app_name))
				.setMessage("Tem certeza de que deseja sair?.").create();

		dialog.setButton("Confirmar", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {

				// desloga do facebook
				Session.getActiveSession().closeAndClearTokenInformation();

				// fecha a activity
				final Activity parentActivity = getActivity();
				parentActivity.finish();

				// finish();

			}
		});

		dialog.show();

		return view;
	}

}
