package io.github.ngspace.topdownshooter.renderer.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES32;
import android.opengl.GLUtils;
import android.util.Log;

import io.github.ngspace.topdownshooter.R;

public class Textures {

    public static int length = 20;

    public static TextureInfo PLAYER;
    public static TextureInfo FUEL;
    public static TextureInfo EXIT_DOOR;
    public static TextureInfo GRAYCIRCLE;
    public static TextureInfo CIRCLE;
    public static TextureInfo SHOOTING_OVERLAY;
    public static TextureInfo TEXT;
    public static TextureInfo WALL;
    public static TextureInfo TORRENT;
    public static TextureInfo BLOCK;
    public static TextureInfo GENERATING;

    static int[] textureHandle = null;

    static int textureIndex = 0;

    public static TextureInfo loadTexture(Context context, int resourceId) {
        Log.i("GL ERROR",""+GLES32.glGetError());

        if (textureHandle[textureIndex] != 0)
        {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inScaled = false;   // No pre-scaling

            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureHandle[textureIndex]);

            // Set filtering
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_NEAREST);
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_NEAREST);
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_CLAMP_TO_EDGE);
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bmp, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bmp.recycle();
        }

        if (textureHandle[textureIndex] == 0) {
            GLRenderer.checkGlError("LoadTexture2d");
            throw new RuntimeException("Error loading textures.");
        }

        var info = new TextureInfo(textureHandle[textureIndex]);
        textureIndex++;
        return info;
    }

    public static void loadTextures(Context applicationContext) {
        if (textureHandle!=null) {
            GLES32.glDeleteTextures(length, textureHandle, 0);
            textureIndex = 0;
        }

        textureHandle = new int[length];
        GLES32.glGenTextures(length, textureHandle, 0);

        PLAYER = loadTexture(applicationContext, R.drawable.player);
        FUEL = loadTexture(applicationContext, R.drawable.fuel);
        EXIT_DOOR = loadTexture(applicationContext, R.drawable.door_closed);
        GRAYCIRCLE = loadTexture(applicationContext, R.drawable.graycircle);
        CIRCLE = loadTexture(applicationContext, R.drawable.oriented_circle);
        SHOOTING_OVERLAY = loadTexture(applicationContext, R.drawable.shooting_overlay);
        TEXT = loadTexture(applicationContext, R.drawable.text);
        WALL = loadTexture(applicationContext, R.drawable.wall);
        TORRENT = loadTexture(applicationContext, R.drawable.torrent);
        BLOCK = loadTexture(applicationContext, R.drawable.block);
        GENERATING = loadTexture(applicationContext, R.drawable.generating);
    }
}
