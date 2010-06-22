package com.nimbusly.isometrix.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

/**
 * This tests different ways of displaying a moving background on the screen and how they compare
 */
public class TestRenderer implements Renderer {

	private static final String TAG = "TestRenderer";
	private TileTexture greenTile, greyTile;
	private Context context;
	private byte[] map = new byte[] {
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
			0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,0,
			0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,0,
			0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
			0,1,1,1,1,1,0,0,0,1,0,1,1,1,1,1,1,1,1,0,
			0,0,0,0,0,0,0,0,0,1,0,1,1,1,1,1,1,1,1,0,
			0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,
			0,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0,
			0,1,0,0,0,0,0,0,0,0,0,0,1,0,1,1,1,1,1,0,
			0,1,1,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1,1,0,
			0,1,0,1,0,1,0,1,0,0,0,0,1,0,1,1,1,1,1,0,
			0,1,0,1,1,1,0,1,0,1,1,0,0,0,1,1,1,1,1,0,
			0,1,0,0,0,0,0,1,0,0,1,0,1,1,1,1,1,1,1,0,
			0,1,0,1,1,1,0,1,1,1,1,0,1,0,1,1,1,1,1,0,
			0,1,0,1,0,1,0,0,0,0,1,0,1,0,1,1,1,1,1,0,
			0,1,0,1,0,1,1,1,1,1,1,0,1,0,0,0,1,0,0,0,
			0,1,0,1,0,0,0,0,0,0,1,0,1,0,0,0,1,0,0,0,
			0,1,0,1,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,
			0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,0,0,
			0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	};
	int mapWidth = 20;
	int mapHeight = 20;
	float glScreenWidth = 20.0f;
	float glScreenHeight = 12.5f;
	float glTileSize;
	float xView, yView;
	long startTime;
	long frameCount = 0;
	
	public TestRenderer(Context context) {
		greenTile = new TileTexture(R.drawable.green_tile2);
		greyTile = new TileTexture(R.drawable.grey_tile2);
		this.context = context;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		greenTile.loadGLTexture(gl, context);
		greyTile.loadGLTexture(gl, context);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		startTime = System.currentTimeMillis();
	}
	
	public void onDrawFrame(GL10 gl) {
		long currentTime = System.currentTimeMillis();
		long elapsed = currentTime - startTime;
		if (currentTime > startTime + 10000) {
			Log.d(TAG, "1 frame took " + (elapsed / frameCount) + "ms; FPS = " + (frameCount * 1000 / elapsed));
		
			startTime = currentTime;
			frameCount = 0;
		}
		frameCount += 1; 
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		float y0 = 0.0f;
		for (int row = 0; row < mapHeight; ++row) {
			float x0 = 0.0f;
			for (int col = 0; col < mapWidth; ++col) {
				gl.glLoadIdentity();					//Reset The Current Modelview Matrix
				gl.glTranslatef(x0, y0, 0.0f);
				//gl.glScalef(glScreenWidth/mapWidth, glScreenHeight/mapHeight, 1.0f);
				TileTexture tile = (map[row*mapWidth + col] == 0) ? greyTile : greenTile;
				tile.draw(gl);
				//x0 += glScreenWidth/mapWidth;
				x0 += 1.0f;
				
			}
			//y0 += glScreenHeight/mapHeight;
			y0 += 1.0f;
		}
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		xView = 10.0f;
		yView = 10.0f;
		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		//gl.glOrthof(0.0f, width, 0.0f, height, 0.0f, 1.0f);
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		//GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		glScreenHeight = (glScreenWidth * height) / width;
		GLU.gluOrtho2D(gl, 0.0f, 8.0f, 0.0f, 5.0f);

		gl.glScalef(1.0f, 0.5f, 0.0f);
		gl.glTranslatef(glScreenWidth/2.0f, glScreenHeight/2.0f, 0.0f);
		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(-glScreenWidth/2.0f, -glScreenHeight/2.0f, 0.0f);
		//gl.glRotatef(60.0f, 1.0f, 1.0f, 0.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
	
	public class Tile {
		
		/** The buffer holding the vertices */
		private FloatBuffer vertexBuffer;

		/** The initial vertex definition */
		private float vertices[] = { 
									-1.0f, -1.0f, 0.0f, 	//Bottom Left
									1.0f, -1.0f, 0.0f, 		//Bottom Right
									-1.0f, 1.0f, 0.0f,	 	//Top Left
									1.0f, 1.0f, 0.0f 		//Top Right
													};
		
		/**
		 * The Square constructor.
		 * 
		 * Initiate the buffers.
		 */
		public Tile() {
			//
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			vertexBuffer = byteBuf.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);
		}

		/**
		 * The object own drawing function.
		 * Called from the renderer to redraw this instance
		 * with possible changes in values.
		 * 
		 * @param gl - The GL Context
		 */
		public void draw(GL10 gl) {		
			//Set the face rotation
			gl.glFrontFace(GL10.GL_CW);
			
			//Point to our vertex buffer
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			
			//Enable vertex buffer
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
			//Draw the vertices as triangle strip
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}


}

