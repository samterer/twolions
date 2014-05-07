package br.com.maboo.imageedit.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import br.com.maboo.imageedit.R;

public class MakePhoto extends Activity implements SurfaceHolder.Callback,
		Camera.ShutterCallback, Camera.PictureCallback {

	private Camera mCamera;
	private SurfaceView mPreview;

	private boolean previewing = false;

	private int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
	private String currentFileName;

	private int idDrawable;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.layout_makephoto);
		mPreview = (SurfaceView) findViewById(R.id.preview);

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
	}

	/**
	 * Inicializa camera
	 */
	private void init() {
		mPreview.getHolder().addCallback(this);

		try {
			// abre a camera
			mCamera = Camera.open();
			// gira camera
			mCamera.setDisplayOrientation(90);

			// recupera os parametros da camera
			Camera.Parameters p = mCamera.getParameters();
			// posicionamento da camera
			p.setRotation(90);
			// seta o focu continuo
			p.setFocusMode("continuous-picture");

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

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		File pictureFileDir = getDir();

		if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
			Log.d("appLog", "Can't create directory to save image.");
			Toast.makeText(this, "Can't create directory to save image.",
					Toast.LENGTH_LONG).show();
			return;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		String photoFile = "pic_" + date + ".jpg";

		currentFileName = pictureFileDir.getPath() + File.separator + photoFile;

		File pictureFile = new File(currentFileName);

		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			fos.write(data);
			fos.close();
			Toast.makeText(
					this,
					"New Image saved:" + photoFile + " on path:"
							+ getDir().getPath(), Toast.LENGTH_LONG).show();
		} catch (Exception error) {
			Log.d("appLog",
					"File" + currentFileName + "not saved: "
							+ error.getMessage());
			Toast.makeText(this, "Image could not be saved.", Toast.LENGTH_LONG)
					.show();
		}

		if (previewing) {
			mCamera.stopPreview();
			previewing = false;
		}

		// chama tela de preview
		Handler mHandler = new Handler();
		mHandler.postDelayed(timedTask, 500);

	}

	private Runnable timedTask = new Runnable() {

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
			mCamera.setPreviewDisplay(mPreview.getHolder());
			// mCamera = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		return new File(sdDir, "photoApp");
	}
}