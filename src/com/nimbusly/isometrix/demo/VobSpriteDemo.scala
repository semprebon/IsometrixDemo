package com.nimbusly.isometrix.demo

import javax.microedition.khronos.opengles.GL10
import android.content.Context
import com.nimbusly.isometrix.Sprite
import com.nimbusly.isometrix.GameRenderer

class VobSpriteDemo(context: Context) extends GameRenderer(context) {

	private val TAG = "CustomRenderer"
	private var sprite: Sprite = null
	var vx = 3
	var vy = 2
	
	private def bounce(x: Int, vx: Int, max: Int) = if (x > max || x < 0) -vx else vx
	
	override def onSurfaceChanged(gl: GL10, width: Int, height: Int) {
		super.onSurfaceChanged(gl, width, height)
		sprite = new Sprite(context, gl, R.drawable.test_animated_texture, 100, 100)
		sprite.isVob = true
	}
	
	override def onDrawFrame(gl: GL10) {
		super.onDrawFrame(gl)
		//sprite.x += vx; sprite.y += vy;
		vx = bounce(sprite.x, vx, screenWidth-sprite.width)
		vy = bounce(sprite.y, vy, screenHeight-sprite.height)
		sprite.frame = if (sprite.frame == 3) 0 else sprite.frame + 1
		
		sprite.draw(gl)
	}
}