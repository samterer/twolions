package br.com.maboo.tubarao.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import br.com.maboo.tubarao.R;

public class GameSurfaceView extends SurfaceView {
       private Bitmap bmp;
       private SurfaceHolder holder;
       private GameLoopThread gameLoopThread;
       private int x = 0; 
       private int xSpeed = 1;
      
       public GameSurfaceView(Context context) {
             super(context);
             gameLoopThread = new GameLoopThread(this);
             holder = getHolder();
             holder.addCallback(new SurfaceHolder.Callback() {
 

                    public void surfaceDestroyed(SurfaceHolder holder) {
                           boolean retry = true;
                           gameLoopThread.setRunning(false);
                           while (retry) {
                                  try {
                                        gameLoopThread.join();
                                        retry = false;
                                  } catch (InterruptedException e) {
                                  }
                           }
                    }
 

                    public void surfaceCreated(SurfaceHolder holder) {
                           gameLoopThread.setRunning(true);
                           gameLoopThread.start();
                    }
 

                    public void surfaceChanged(SurfaceHolder holder, int format,
                                  int width, int height) {
                    }
             });
             bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tub125x115_right);
       }

       protected void onDraw(Canvas canvas) {
             if (x == getWidth() - bmp.getWidth()) {
                    xSpeed = -1;
             }
             if (x == 0) {
                    xSpeed = 1;
             }
             x = x + xSpeed;
             canvas.drawColor(Color.BLACK);
             canvas.drawBitmap(bmp, x , 10, null);
             
             canvas.restore();
       }
}