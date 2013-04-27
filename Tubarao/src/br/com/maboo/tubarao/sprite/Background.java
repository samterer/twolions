package br.com.maboo.tubarao.sprite;

import android.graphics.Bitmap;

public class Background {

	public Background() {
		// TODO Auto-generated constructor stub
	}

	public Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {
		Bitmap resizedImage = null;
		try {
			int imageHeight = image.getHeight();

			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			int imageWidth = (imageHeight * image.getWidth())
					/ image.getHeight();

			if (imageWidth > maxWidth) {
				imageWidth = maxWidth;
				imageHeight = (imageWidth * image.getHeight())
						/ image.getWidth();
			}

			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			if (imageWidth > maxWidth)
				imageWidth = maxWidth;

			resizedImage = Bitmap.createScaledBitmap(image, imageWidth,
					imageHeight, true);
		} catch (OutOfMemoryError e) {

			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizedImage;
	}
}
