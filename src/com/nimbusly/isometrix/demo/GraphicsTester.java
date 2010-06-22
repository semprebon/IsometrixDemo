package com.nimbusly.isometrix.demo;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class GraphicsTester extends Activity {
	private GLSurfaceView glView;
	
	public static final int BITMAP_BACKGROUND_DEMO = 1;
	public static final int SPRITE_DEMO = 2;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		glView = new GLSurfaceView(this);
		glView.setRenderer(new BitmapBackgroundRenderer(this));
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
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, BITMAP_BACKGROUND_DEMO, 0, "Bitmap Background Demo");
	    menu.add(0, SPRITE_DEMO, 0, "Sprite Demo");
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case BITMAP_BACKGROUND_DEMO:
	    	startActivity(new Intent(this, BitmapBackgroundActivity.class));
	        return true;
    	case SPRITE_DEMO:
    		startActivity(new Intent(this, SpriteActivity.class));
    		return true;
    	}
	    return false;
	}
}