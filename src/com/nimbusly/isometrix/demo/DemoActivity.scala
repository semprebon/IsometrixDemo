package com.nimbusly.isometrix.demo

import com.nimbusly.isometrix.GameRenderer

import android.app.Activity
import android.content.Context
import android.opengl._
import android.os.Bundle
import android.util.Log
import android.view._

class DemoActivity extends Activity {
	protected var glView: GLSurfaceView = null
	private var renderer: GameRenderer = null
	
	private val TAG = "DemoActivity"
	
	class Demo(val demoObject: GameRenderer, val displayName: String) {
	}
	
	var DEMOS: List[Demo] = null
		
    /** Called when the activity is first created. */
    override def onCreate(savedInstanceState: Bundle) {
		DEMOS = List(
			new Demo(new TextureDrawBackgroundDemo(this), "TextureDraw Background"),
			new Demo(new TextureDrawSpriteDemo(this), "TextureDraw Sprites"),
			new Demo(new VobSpriteDemo(this), "VOB Sprites")
		)
	
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
		runDemo(DEMOS(0))
    }
    
	override protected def onResume() {
		super.onResume()
		glView.onResume()
	}

	override protected def onPause() {
		super.onPause()
		glView.onPause()
	}
	
	/* Creates the menu items */
	def addToMenu(menu: Menu, item: Demo, index: Int) {
		menu.add(0, index, 0, item.displayName)
	}
	
	override def onCreateOptionsMenu(menu: Menu) : Boolean = {
		for (i <- 0 to DEMOS.length-1) addToMenu(menu, DEMOS(i), i)
	    return true
	}

	private def runDemo(demo: Demo) {
		if (glView != null) {
			renderer.stopRendering(glView)
		}
		
		glView = new GLSurfaceView(this)
		glView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR)
		renderer = demo.demoObject
		glView.setRenderer(renderer)
		setContentView(glView)
	}
	
	/* Handles item selections */
	override def onOptionsItemSelected(item: MenuItem) : Boolean = {
		runDemo(DEMOS(item.getItemId()))
		return true
	}

}