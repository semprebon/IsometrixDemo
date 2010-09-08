package com.nimbusly.isometrix.demo

import javax.microedition.khronos.opengles.GL10
import android.content.Context
import com.nimbusly.isometrix.Background
import com.nimbusly.isometrix.GameRenderer

class TextureDrawBackgroundDemo(context: Context) extends GameRenderer(context) {

	private val TAG = "TextureDrawBackgroundDemo"
	private var background: Background = null
	var vx = 3
	var vy = 2
	
	private def bounce(x: Int, vx: Int, max: Int) = if (x > max || x < 0) -vx else vx
	
	override def onDrawFrame(gl: GL10) {
		super.onDrawFrame(gl)
		background.x += vx 
		background.y += vy
		vx = bounce(background.x, vx, background.textureWidth-background.viewWidth)
		vy = bounce(background.y, vy, background.textureHeight-background.viewWidth)
		background.draw(gl)
	}

	override def onSurfaceChanged(gl: GL10, width: Int, height: Int) {
		super.onSurfaceChanged(gl, width, height)
		background = new Background(context, gl, R.drawable.amelia, width, height)
	}
}