package br.com.maboo.imageedit.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.maboo.imageedit.R;

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
			idDrawable = extras.getInt("id");
		}

		// coloca mascara na tela
		Drawable d = getResources().getDrawable(idDrawable);
		Log.i("appLog", "put mask d: " + d.getCurrent().toString());

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

			createBitmap(data);
		}

		// get dir to save
		File pictureFileDir = getDir();
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

		// save image
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(pictureFile);
			fos.write(data);
			fos.close();
			Toast.makeText(
					this,
					"New Image saved:" + photoFile + " on path:"
							+ getDir().getPath(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Log.d("appLog",
					"File" + currentFileName + "not saved: " + e.getMessage());
			Toast.makeText(this, "Image could not be saved.", Toast.LENGTH_LONG)
					.show();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		/*
		 * // file name String filename = "mask_1.jpg"; // save dir File sd =
		 * new File(Environment.getExternalStorageDirectory() + File.separator +
		 * "My Custom Folder"); // create dir sd.mkdirs(); // temp dir File
		 * tmpFile = new File(sd,filename); // export file to bitmap Bitmap
		 * bitmap = (Bitmap) data.getExtras().get("data"); try { out = new
		 * FileOutputStream(tmpFile); bitmap.compress(Bitmap.CompressFormat.PNG,
		 * 90, out); out.flush(); out.close(); } catch (IOException e) {
		 * e.printStackTrace(); } catch (NullPointerException e) {
		 * e.printStackTrace(); } finally { out.close(); }
		 */

		// chama tela de preview
		Handler mHandler = new Handler();
		mHandler.postDelayed(timedTask, 500);

	}

	private Bitmap createBitmap(byte[] data) {
		Bitmap bitmap = null;
		Bitmap bmpOfTheImageFromCamera = null;
		try {
			// create a bitmap image
		//	BitmapFactory.Options opts = new BitmapFactory.Options();
		//	bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		//	bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, false);

			// get the bitmap from camera imageData
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			// down sample
			bmpOfTheImageFromCamera = BitmapFactory.decodeByteArray(data, 0,
					data.length, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmpOfTheImageFromCamera;
	}

	private Runnable timedTask = new Runnable() {

		@SuppressLint("SdCardPath")
		@Override
		public void run() {

			Log.d("appLog", "Open picture: File" + "file://" + "sdcard/"
					+ getDir().getPath() + "/" + currentFileName);

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
				canvas = holder.lockCanvas(null);
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
		/*
		 * Bitmap icon = BitmapFactory.decodeResource(getResources(),
		 * R.drawable.ic_launcher); canvas.drawColor(Color.BLACK);
		 * canvas.drawBitmap(icon, 100, 100, new Paint());
		 */
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

	private File getDir() {
		File sdDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		sdDir.mkdirs();
		return new File(sdDir, "photoApp");
	}
}