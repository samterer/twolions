package br.com.maboo.imageedit.util;

import java.io.File;

import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.R.string;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

public class Utils {

	@SuppressWarnings("deprecation")
	public static void alertDialog(final Context context, final String mensagem) {

		try {

			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(context.getString(R.string.app_name))
					.setMessage(mensagem).create();

			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog,
						final int which) {

					return;
				}
			});

			dialog.show();

		} catch (Exception e) {
			Log.e("appLog", e.getMessage(), e);
		}
	}

	public static void alertDialog(final Context context, final int mensagem,
			final String title) {

		try {

			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(title).setMessage(mensagem).create();

			// dialog.setIcon(R.drawable.iconerror);

			dialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					// fechar o app
					return;
				}
			});

			dialog.show();

		} catch (Exception e) {
			Log.e("appLog", e.getMessage(), e);
		}
	}

	public static File getDir() {
		File sdDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		sdDir.mkdirs();
		return new File(sdDir, "photoApp");
	}

}
