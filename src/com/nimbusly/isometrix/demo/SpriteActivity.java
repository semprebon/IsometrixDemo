package com.nimbusly.isometrix.demo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.nimbusly.isometrix.GameRenderer;
import com.nimbusly.isometrix.Sprite;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class SpriteActivity extends Activity {
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
	 * This tests different ways of displaying a sprite on the screen and how they compare
	 */
	public class CustomRenderer extends GameRenderer {

		private static final String TAG = "CustomRenderer";
		private Sprite sprite;
		int vx = 3, vy = 2;
		
		public CustomRenderer(Context context) {
			super(context);
		}
		
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			super.onSurfaceChanged(gl, width, height);
			sprite = new Sprite(context, gl, R.drawable.walking, 12, 8);
		}
		
		private int bounce(int x, int vx, int max) {
			return (x > max || x < 0) ? -vx : vx;
		}
		
		public void onDrawFrame(GL10 gl) {
			super.onDrawFrame(gl);
			sprite.x += vx; sprite.y += vy;
			vx = bounce(sprite.x, vx, screenWidth-sprite.width);
			vy = bounce(sprite.y, vy, screenHeight-sprite.height);
			Log.i(TAG, "sprite at " + sprite.x + "," + sprite.y + " moving " + vx + "," + vy + " in " 
					+ (screenWidth-sprite.width) + "x" + (screenHeight-sprite.height));
			sprite.draw(gl);
		}

	}



}
