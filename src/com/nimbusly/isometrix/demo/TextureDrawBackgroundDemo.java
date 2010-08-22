package com.nimbusly.isometrix.demo;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.nimbusly.isometrix.Background;
import com.nimbusly.isometrix.GameRenderer;

public class TextureDrawBackgroundDemo extends GameRenderer {
	

	private static final String TAG = "TestRenderer";
	private Background background;
	int vx = 3, vy = 2;
	
	public TextureDrawBackgroundDemo(Context context) {
		super(context);
	}
	
	private int bounce(int x, int vx, int max) {
		return (x > max || x < 0) ? -vx : vx;
	}
	
	public void onDrawFrame(GL10 gl) {
		super.onDrawFrame(gl);
		background.x += vx; background.y += vy;
		vx = bounce(background.x, vx, background.textureWidth-background.viewWidth);
		vy = bounce(background.y, vy, background.textureHeight-background.viewWidth);
		background.draw(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		background = new Background(context, gl, R.drawable.amelia, width, height);
	}
	
}

