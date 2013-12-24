package br.com.maboo.node.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.maboo.node.MainActivity;
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

		// AlertDialog dialog = new
		// AlertDialog.Builder(getActivity()).setTitle(this.getString(R.string.app_name)).setMessage("Tem certeza de que deseja sair?").create();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Add the buttons
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// desloga do facebook
				Session.getActiveSession().closeAndClearTokenInformation();

				// fecha a activity final
				Activity parentActivity = getActivity();
				parentActivity.finish();
			}
		});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(getActivity(),
								MainActivity.class);

						startActivity(intent);

						getActivity().finish();
					}
				});

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.setTitle("Tem certeza de que deseja sair?");

		/*
		 * dialog.setButton("Confirmar", new DialogInterface.OnClickListener() {
		 * public void onClick(final DialogInterface dialog, final int which) {
		 * 
		 * // desloga do facebook
		 * Session.getActiveSession().closeAndClearTokenInformation();
		 * 
		 * // fecha a activity final Activity parentActivity = getActivity();
		 * parentActivity.finish(); }
		 * 
		 * });
		 * 
		 * dialog.setButton("Cancelar", new DialogInterface.OnClickListener() {
		 * public void onClick(final DialogInterface dialog, final int which) {
		 * 
		 * Intent intent = new Intent(getActivity(), MainActivity.class);
		 * 
		 * startActivity(intent);
		 * 
		 * getActivity().finish();
		 * 
		 * }
		 * 
		 * });
		 */

		dialog.show();

		return view;
	}
}
