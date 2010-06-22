package com.nimbusly.isometrix.demo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.nimbusly.isometrix.Background;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class BitmapBackgroundActivity extends Activity {

	private GLSurfaceView glView;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		glView = new GLSurfaceView(this);
		glView.setRenderer(new CustomRenderer(this));
		setContentView(glView);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glView.onPause();
	}
	
	/**
	 * This tests different ways of displaying a moving background on the screen and how they compare
	 */
	public class CustomRenderer implements Renderer {

		private static final String TAG = "TestRenderer";
		private Context context;
		long startTime;
		long frameCount = 0;
		StringBuffer logMessage = new StringBuffer(100);
		private Background background;
		
		int vx = 3, vy = 2;
		
		public CustomRenderer(Context context) {
			this.context = context;
		}
		
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
			gl.glShadeModel(GL10.GL_FLAT); 			//Enable Smooth Shading
			gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); 	//Black Background
			gl.glDisable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
	        gl.glDisable(GL10.GL_DITHER);
	        gl.glDisable(GL10.GL_LIGHTING);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			//mapTextureId = loadBitmap(context, gl, R.drawable.amelia);
			startTime = System.currentTimeMillis();
		}
		
		private void reportFramesPerSecond(long elapsed, long frameCount) {
			Log.d(TAG, "1 frame took " + (elapsed / frameCount) + "ms; FPS = " + (frameCount * 1000 / elapsed));
		}
		
		private int bounce(int x, int vx, int max) {
			return (x > max || x < 0) ? -vx : vx;
		}
		
		public void onDrawFrame(GL10 gl) {
			long currentTime = System.currentTimeMillis();
			long elapsed = currentTime - startTime;
			if (currentTime > startTime + 10000) {
				reportFramesPerSecond(elapsed, frameCount);
				startTime = currentTime;
				frameCount = 0;
			}
			frameCount += 1; 

			background.x += vx; background.y += vy;
			vx = bounce(background.x, vx, background.textureWidth-background.viewWidth);
			vy = bounce(background.y, vy, background.textureHeight-background.viewWidth);
			background.draw(gl);
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			if(height == 0) { 						//Prevent A Divide By Zero By
				height = 1; 						//Making Height Equal One
			}
			background = new Background(context, gl, R.drawable.amelia, width, height);
			
			gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
	        gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
			gl.glOrthof(0.0f, width, 0.0f, height, 0.0f, 1.0f);
			gl.glLoadIdentity(); 					//Reset The Projection Matrix

			gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
			gl.glLoadIdentity(); 					//Reset The Modelview Matrix
		}
		
	}


}
