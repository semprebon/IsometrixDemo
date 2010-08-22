package com.nimbusly.isometrix.demo;

import javax.microedition.khronos.opengles.GL10;

import com.nimbusly.isometrix.GameRenderer;
import com.nimbusly.isometrix.Sprite;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class TextureDrawSpriteDemo extends GameRenderer {


	private static final String TAG = "CustomRenderer";
	private Sprite sprite;
	int vx = 3, vy = 2;
	
	public TextureDrawSpriteDemo(Context context) {
		super(context);
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		sprite = new Sprite(context, gl, R.drawable.simple_sprite, 12, 8);
	}
	
	private int bounce(int x, int vx, int max) {
		return (x > max || x < 0) ? -vx : vx;
	}
	
	public void onDrawFrame(GL10 gl) {
		super.onDrawFrame(gl);
		sprite.x += vx; sprite.y += vy;
		vx = bounce(sprite.x, vx, screenWidth-sprite.width);
		vy = bounce(sprite.y, vy, screenHeight-sprite.height);
		sprite.draw(gl);
	}

}
