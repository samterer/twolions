package br.com.maboo.imageedit.activity;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.camera.CustomSurfaceView;
import br.com.maboo.imageedit.model.Masks;
import br.com.maboo.imageedit.util.BitmapUtil;
import br.com.maboo.imageedit.util.Utils;

@SuppressLint("DrawAllocation")
public class MakePhotoActivity extends Activity implements SurfaceHolder.Callback,
		Camera.ShutterCallback, Camera.PictureCallback {

	private Camera mCamera;
	private SurfaceView mPreview;
	private boolean mPreviewing = false;
	private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

	private String mFileName;

	private int mIdMask;

	private LayoutInflater mInflater = null;

	private File mPictureFileDir;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.activity_make_photo);
		mPreview = (CustomSurfaceView) findViewById(R.id.preview);

		setIdMask(getIntent().getExtras()); //get mask id

		init(); //init camera

		mInflater = LayoutInflater.from(getBaseContext());
		View view = mInflater.inflate(R.layout.photo, null);
		addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		drawMask(); //print mask in screen

	}

	private void setIdMask(Bundle extras) {
		if (extras != null) {
			// sempre faz o calculo com "-1", pois a LIST_SWAP começa na pos 1,
			// pois o valor 0 é a imagem do tutorial
			mIdMask = Masks.LIST_PHOTO[extras.getInt("id")-1];
		}
	}

	private void drawMask() {
		Drawable d = getResources().getDrawable(mIdMask);
		ImageView img = (ImageView) findViewById(R.id.img);
		img.setBackground(d);
	}

	/**
	 * Inicializa camera
	 */
	private void init() {
		mPreview.getHolder().addCallback(this);

		try {

			mCamera = Camera.open(mCameraId);// open camera
			mCamera.setDisplayOrientation(90);

			Camera.Parameters p = mCamera.getParameters(); // recover camera
															// parameters
			p.setRotation(90); // position of cam
			p.setFocusMode("continuous-picture"); // continus focu

			mCamera.setParameters(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * Cancel pic
	 * 
	 * @param v
	 */
	public void onCancelClick(View v) {
		onDestroy();
	}

	/**
	 * Take a picture
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
	
	/**
	 * Change camera (front and back)
	 * @param v
	 */
	public void swithCamera(View v) {
		if (mCamera != null) {
			stopPreview();
			mCamera = null;
		}

		// swap the id of the camera to be used
		if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
			mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
		} else {
			mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
		}

		try {
			mCamera = Camera.open(mCameraId);

			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(mPreview.getHolder());
			mCamera.startPreview();

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
		if (mPreviewing) {
			mPreviewing = false;
		}

		if (mCamera != null) {
			mCamera.stopPreview();
			try {
				mCamera.setPreviewDisplay(mPreview.getHolder());
				mCamera.startPreview();
				mPreviewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void onDraw(Canvas canvas) {
	//	mPreview.onDraw(canvas);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i("PREVIEW", "surfaceDestroyed");
	}

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
	
	private void stopPreview() {
		if(mPreviewing) {			
			mPreviewing = false;
			mCamera.stopPreview();
			mCamera.release();
		}
	}

	@Override
	public void onPictureTaken(byte[] photoData, Camera camera) {
		if (photoData != null) {
			stopPreview();
		}

		// get dir to save
		mPictureFileDir = Utils.getDir(getApplicationContext());
		if(mPictureFileDir == null){
			onDestroy();
			return;
		}
		// get name
		String photoName = Utils.getPhotoName(); 
		// prepare name
		mFileName = mPictureFileDir.getPath() + File.separator + photoName;
		// create file
		File pictureFile = new File(mFileName);
		
		// recover img in bitmap
		Bitmap bitmap = (Bitmap) createBitmap(photoData); 
		bitmap = BitmapUtil.getInstance(this).rotateBitmap(bitmap); // rotate bitmap
		bitmap = BitmapUtil.getInstance(this).overlay(bitmap); // put overlay in bitmap
		
		// save data
		Utils.saveFile(getApplicationContext(), photoName, photoData, pictureFile, bitmap);

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
					+ mPictureFileDir.getPath() + "/" + mFileName);

			// exibe imagem
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + "sdcard/" + mFileName),
					"image/jpg");

			startActivity(intent);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("CAMERA", "onResume");
		init();
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera();
		Log.d("CAMERA", "onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseCamera();
		Log.d("CAMERA", "onDestroy");
	}

}