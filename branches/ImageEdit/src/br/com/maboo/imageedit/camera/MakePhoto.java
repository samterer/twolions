package br.com.maboo.imageedit.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.model.DrawableId;
import br.com.maboo.imageedit.util.Utils;

@SuppressLint("DrawAllocation")
public class MakePhoto extends Activity implements SurfaceHolder.Callback,
		Camera.ShutterCallback, Camera.PictureCallback {

	private Camera mCamera;
	private MySurfaceView mPreview;

	private boolean previewing = false;

	private int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
	private String currentFileName;

	private int idDrawable;

	LayoutInflater inflater = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.layout_makephoto);
		mPreview = (MySurfaceView) findViewById(R.id.preview);

		// monta mascara
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			idDrawable = DrawableId.LIST[extras.getInt("id")];
		}

		// coloca mascara na tela
		Drawable d = getResources().getDrawable(idDrawable);

		// init camera
		init();

		inflater = LayoutInflater.from(getBaseContext());
		View view = inflater.inflate(R.layout.item, null);
		addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		ImageView img = (ImageView) findViewById(R.id.img);
		img.setBackground(d);

	}

	/**
	 * Inicializa camera
	 */
	private void init() {
		mPreview.getHolder().addCallback(this);

		try {
			mCamera = Camera.open(currentCameraId);
			mCamera.setDisplayOrientation(90);

			// abre a camera
			mCamera = Camera.open();

			// recupera os parametros da camera
			Camera.Parameters p = mCamera.getParameters();
			// posicionamento da camera
			p.setRotation(90);
			// seta o focu continuo
			p.setFocusMode("continuous-picture");
			// seta os parametros
			mCamera.setParameters(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// init camera
		init();
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseCamera();
		Log.d("CAMERA", "Destroy");
	}

	private void releaseCamera() {
		if (mCamera != null) {
			try {
				mCamera.setPreviewCallback(null);
				mCamera.release();
				mCamera = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Cancela a foto
	 * 
	 * @param v
	 */
	public void onCancelClick(View v) {
		onDestroy();
	}

	/**
	 * Click da camera
	 * 
	 * @param v
	 */
	public void onSnapClick(View v) {
		try {
			mCamera.takePicture(null, null, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onShutter() {
		Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (previewing) {
			mCamera.stopPreview();
			previewing = false;
		}

		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(mPreview.getHolder());
				mCamera.startPreview();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// file to save
	FileOutputStream out;

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		if (data != null) {
			// clean camera
			mCamera.stopPreview();
			previewing = false;
			mCamera.release();
		}

		// get dir to save
		File pictureFileDir = Utils.getDir();
		if (!pictureFileDir.exists()) {
			Log.d("appLog", "Can't create directory to save image.");
			Toast.makeText(this, "Can't create directory to save image.",
					Toast.LENGTH_LONG).show();
			return;
		}

		// get current date
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		String photoFile = "pic_" + date + ".jpg";

		// prepare name
		currentFileName = pictureFileDir.getPath() + File.separator + photoFile;

		// create file
		File pictureFile = new File(currentFileName);
		Bitmap bitmap = (Bitmap) createBitmap(data); // recover img in bitmap

		// rotate bitmap
		bitmap = rotateBitmap(bitmap);
		// put overlay in bitmap
		bitmap = overlay(bitmap);
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(pictureFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Toast.makeText(
					this,
					"New Image saved:" + photoFile + " on path:"
							+ Utils.getDir().getPath(), Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			Log.d("appLog",
					"File" + currentFileName + "not saved: " + e.getMessage());
			Toast.makeText(this, "Image could not be saved.", Toast.LENGTH_LONG)
					.show();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		// chama tela de preview
		Handler mHandler = new Handler();
		mHandler.postDelayed(timedTask, 500);

	}

	private Bitmap createBitmap(byte[] data) {
		Bitmap bitmap = null;
		try {
			// get the bitmap from camera imageData
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			// down sample
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private Runnable timedTask = new Runnable() {

		@SuppressLint("SdCardPath")
		@Override
		public void run() {

			Log.d("appLog", "Open picture: File" + "file://" + "sdcard/"
					+ Utils.getDir().getPath() + "/" + currentFileName);

			// exibe imagem
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.parse("file://" + "sdcard/" + currentFileName),
					"image/jpg");

			startActivity(intent);
		}
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// mCamera.setPreviewDisplay(mPreview.getHolder());
			mCamera = Camera.open();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				synchronized (holder) {
					onDraw(canvas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onDraw(Canvas canvas) {
		mPreview.onDraw(canvas);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i("PREVIEW", "surfaceDestroyed");
	}

	public void swithCamera(View v) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		// swap the id of the camera to be used
		if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
		else
			currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

		try {
			mCamera = Camera.open(currentCameraId);

			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(mPreview.getHolder());
			mCamera.startPreview();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap drawTextToBitmap(int gResId, String gText) {
		Resources resources = getResources();
		float scale = resources.getDisplayMetrics().density;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		// set default bitmap config if none
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.rgb(61, 61, 61));
		// text size in pixels
		paint.setTextSize((int) (14 * scale));
		// text shadow
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

		// draw text to the Canvas center
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bitmap.getWidth() - bounds.width()) / 2;
		int y = (bitmap.getHeight() + bounds.height()) / 2;

		canvas.drawText(gText, x, y, paint);

		return bitmap;
	}

	public Bitmap rotateBitmap(Bitmap bOriginal) {
		// find the width and height of the screen:
		Display d = getWindowManager().getDefaultDisplay();
		int x = d.getWidth();
		int y = d.getHeight();

		// scale it to fit the screen, x and y swapped because my image is wider
		// than it is tall
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bOriginal, y, x, true);

		// create a matrix object
		Matrix matrix = new Matrix();
		matrix.postRotate(90); // anti-clockwise by 90 degrees

		// create a new bitmap from the original using the matrix to transform
		// the result
		Bitmap rotatedBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
						scaledBitmap.getHeight(), matrix, true);

		return rotatedBitmap;
	}

	public Bitmap overlay(Bitmap bOriginal) {
		float scale = getResources().getDisplayMetrics().density;

		Bitmap bmOverlay = Bitmap.createBitmap(bOriginal.getWidth(),
				bOriginal.getHeight(), bOriginal.getConfig());
		Bitmap bmp2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.h3_topic);

		Display d = getWindowManager().getDefaultDisplay();
		int x = (d.getWidth()/2 - bmOverlay.getWidth()/2);
		int y = (d.getHeight()/2 - bmOverlay.getHeight()/2);

		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bOriginal, new Matrix(), null);
		canvas.drawBitmap(bmp2, x, y, null);
		return bmOverlay;
	}
}