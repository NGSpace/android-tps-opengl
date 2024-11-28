package io.github.ngspace.topdownshooter.engine.opengl.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES32;
import android.opengl.GLUtils;
import android.util.Log;

import io.github.ngspace.topdownshooter.MainActivity;
import io.github.ngspace.topdownshooter.R;

public class Textures {

    public static int length = 10;

    public static TextureInfo SIMLEY;// = loadTexture(R.drawable.simley);
    public static TextureInfo FEDORA;// = loadTexture(R.drawable.fedora);
    public static TextureInfo STARSET;// = loadTexture(R.drawable.starset);
    public static TextureInfo FUCKOPENGL;// = loadTexture(R.drawable.fuckopengl);
    public static TextureInfo ANCHOR;// = loadTexture(R.drawable.anchor);

    static int[] textureHandle = null;

    static int textureIndex = 0;

    public static TextureInfo loadTexture(Context context, int resourceId) {
        if (textureHandle==null) {
            textureHandle = new int[length];
            GLES32.glGenTextures(length, textureHandle, 0);
        }
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

    private static boolean hasLoaded = false;

    public static void loadTextures(Context applicationContext) {
        if (hasLoaded) return;
        hasLoaded = true;
        SIMLEY = loadTexture(applicationContext, R.drawable.simley);
        FEDORA = loadTexture(applicationContext, R.drawable.fedora);
        STARSET = loadTexture(applicationContext, R.drawable.starset);
        FUCKOPENGL = loadTexture(applicationContext, R.drawable.fuckopengl);
        ANCHOR = loadTexture(applicationContext, R.drawable.anchor);
    }
}
