package com.nimbusly.isometrix.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class TileTexture {
	public float x, y, z;
	public float width, height;
	
	private static final String TAG = "TileTexture";
	
	private static BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static int[] textureNameWorkspace = new int[1];
    private static int[] cropWorkspace = new int[4];
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer  indexBuffer;
	/** Our texture pointer */
	private int textureName;
	int[] crop = new int[4];

	private int resourceId;
	
	private static float vertices[] = {
		0.0f, 0.0f, 0.0f,
    	1.0f, 0.0f, 0.0f,
    	0.0f, 1.0f, 0.0f,
    	1.0f, 1.0f, 0.0f,
	};
	
    private float texture[] = {    		
    		//Mapping coordinates for the vertices
    		0.0f, 0.0f,
    		0.0f, 1.0f,
    		1.0f, 0.0f,
    		1.0f, 1.0f, 
    };
	
    private byte indices[] = {
			//Faces definition
    		0,1,3, 0,3,2,
	};

    private FloatBuffer loadBuffer(float[] values) {
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(values.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer result = byteBuf.asFloatBuffer();
		result.put(values);
		result.position(0);
		return result;
	}
	
	public TileTexture(int resourceId) {
		this.resourceId = resourceId;
		vertexBuffer = loadBuffer(vertices);
		textureBuffer = loadBuffer(texture);
		
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
		//int textureName;
	}

	/**
	 * Draws using VBO
	 * @param gl
	 */
	public void draw(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Enable the vertex and texture state
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
//        gl.glPushMatrix();
//        gl.glLoadIdentity();
//        gl.glTranslatef(x, y, z);
//        //mGrid.draw(gl, true, false);
//        gl.glPopMatrix();

        //Set the face rotation
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public void drawDirect(GL10 gl, int x, int y, int width, int height) {
		
		//Draw the vertices as triangles, based on the Index Buffer information
		crop[0] = x; crop[1] = y; crop[2] = width; crop[3] = height;
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
		((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, crop, 0);
		((GL11Ext) gl).glDrawTexiOES(0, 0, 1, width, height);
		//gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		
//        gl.glPushMatrix();
//        gl.glLoadIdentity();
//        gl.glTranslatef(x, y, z);
//        //mGrid.draw(gl, true, false);
//        gl.glPopMatrix();
	}

	public static Bitmap loadBitmapFromResource(Context context, int id) {
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(id);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}
		return bitmap;
	}
	
	public int loadGLTexture(GL10 gl, Context context) {
        Bitmap bitmap = loadBitmapFromResource(context, resourceId);
        return loadGLTexture(gl, bitmap);
    }

	public int loadGLTexture(GL10 gl, Bitmap bitmap) {
        gl.glGenTextures(1, textureNameWorkspace, 0);
        textureName = textureNameWorkspace[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
        
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        
//        cropWorkspace[0] = 0;
//        cropWorkspace[1] = bitmap.getHeight();
//        cropWorkspace[2] = bitmap.getWidth();
//        cropWorkspace[3] = -bitmap.getHeight();
        
//        bitmap.recycle();

//        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, 
//                GL11Ext.GL_TEXTURE_CROP_RECT_OES, cropWorkspace, 0);

        int error = gl.glGetError();
        if (error != GL10.GL_NO_ERROR) {
            Log.e(TAG, "Texture Load GLError: " + error);
        }
        return textureName;
    }

}