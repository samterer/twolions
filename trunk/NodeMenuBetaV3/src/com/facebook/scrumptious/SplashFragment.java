package com.facebook.scrumptious;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import br.com.maboo.node.R;
import br.com.maboo.node.util.Util;

public class SplashFragment extends Fragment {

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

		// verifica o gps e a internet
		if (initCheck()) {
			lb.setVisibility(View.VISIBLE);
		} else {
			// muda o backgroud para um padrao de erro
			ImageView img = (ImageView) view.findViewById(R.id.imageView1);
			img.setVisibility(View.VISIBLE);
		}

		return view;
	}

	/**
	 * Verificação inicial gps internet
	 */
	@SuppressWarnings("deprecation")
	private boolean initCheck() {
		// verifica a internet e o gps
		// se um dos dois não estiver ok o app será fechado
		if (!Util.isVerify(getActivity())) {

			AlertDialog dialog = new AlertDialog.Builder(getActivity())
					.setTitle(this.getString(R.string.app_name))
					.setMessage(
							"Ative o gps, e verifique a conexão com a internet.")
					.create();

			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					// finish();

					// fecha a activity final
					// Activity parentActivity = getActivity();
					getActivity().finish();
				}
			});

			dialog.show();

		} else {

			return true;
			/*
			 * try { PackageInfo info = getPackageManager().getPackageInfo(
			 * "com.eatapp", PackageManager.GET_SIGNATURES); for (Signature
			 * signature : info.signatures) { MessageDigest md =
			 * MessageDigest.getInstance("SHA");
			 * md.update(signature.toByteArray()); Log.e("MY KEY HASH:",
			 * Base64.encodeToString(md.digest(), Base64.DEFAULT)); } } catch
			 * (NameNotFoundException e) {
			 * 
			 * } catch (NoSuchAlgorithmException e) {
			 * 
			 * }
			 */
		}

		// default
		return false;

	}
}