package br.com.maboo.imageedit.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	/**
	 * Create and recover name path in extorage
	 * 
	 * @param context
	 * @return
	 */
	public static File getDir(Context context) {
		File sdDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		sdDir.mkdirs();

		File file = new File(sdDir, "photoApp");
		if (!file.exists()) {
			Log.e("Utils", "Can't create directory to save image.");
			Toast.makeText(context, "Can't create directory to save image.",
					Toast.LENGTH_LONG).show();
			return null;
		} else {
			return new File(sdDir, "photoApp");
		}
	}

	/**
	 * Recover default name to file
	 * 
	 * @return
	 */
	public static String getPhotoName() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		return "pic_" + date + ".jpg";
	}

	public static boolean saveFile(Context context, String photoName,
			byte[] photoData, File pictureFileDir, Bitmap bitmap) {
		boolean result = false;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(pictureFileDir);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Toast.makeText(
					context,
					"New Image saved:" + photoName + " on path:"
							+ pictureFileDir.getPath(), Toast.LENGTH_LONG)
					.show();
			result = true;
		} catch (IOException e) {
			Log.d("appLog", "File" + photoName + "not saved: " + e.getMessage());
			Toast.makeText(context, "Image could not be saved.",
					Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
					result = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result;
	}

}
