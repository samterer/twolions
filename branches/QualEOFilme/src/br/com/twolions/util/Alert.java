package br.com.twolions.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import br.com.twolions.R;

public class Alert {

	private Object object;

	public Alert(Object object) {

		this.object = object;

	}

	public void alertDialogError(final Context context, final String titulo,
			final String mensagem) {
		try {
			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(context.getString(R.string.app_name))
					.setMessage(mensagem).create();

			dialog.setIcon(R.drawable.iconerror);

			dialog.setTitle(titulo);

			dialog.setButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					if (context instanceof OnReturnAlertListener) {

						onExecute();
					}

					return;
				}
			});
			dialog.show();
		} catch (Exception e) {
			Log.e("appLog", e.getMessage(), e);
		}
	}

	public void onExecute() {
		if (object != null && object instanceof OnReturnAlertListener) {
			((OnReturnAlertListener) object).onReturn();
		}
	}

}
