package com.nimbusly.isometrix.demo;

import javax.microedition.khronos.opengles.GL10;

import com.nimbusly.isometrix.GameRenderer;
import com.nimbusly.isometrix.Sprite;
import com.nimbusly.isometrix.VobSprite;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class VobSpriteDemo extends GameRenderer {


	private static final String TAG = "CustomRenderer";
	private VobSprite sprite;
	int vx = 3, vy = 2;
	
	public VobSpriteDemo(Context context) {
		super(context);
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		sprite = new VobSprite(context, gl, R.drawable.simple_sprite, 12, 8);
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
