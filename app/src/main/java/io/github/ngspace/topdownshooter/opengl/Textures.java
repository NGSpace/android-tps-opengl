package io.github.ngspace.topdownshooter.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import io.github.ngspace.topdownshooter.MainActivity;
import io.github.ngspace.topdownshooter.R;

public class Textures {

    public static int length = 4;

    public static final TextureInfo SIMLEY = loadTexture(R.drawable.simley);
    public static final TextureInfo FEDORA = loadTexture(R.drawable.fedora);
    public static final TextureInfo STARSET = loadTexture(R.drawable.starset);
    public static final TextureInfo FUCKOPENGL = loadTexture(R.drawable.fuckopengl);

    public static int GetImage(@NonNull Context c, String ImageName) {
        return c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
    }
    static int[] textureHandle = new int[2];

    static int textureIndex = 0;

    public static TextureInfo loadTexture(int resourceId) {
        if (textureHandle==null) {
            textureHandle = new int[length];
            GLES30.glGenTextures(length, textureHandle, 0);
        }
        Log.i("GL ERROR",""+GLES30.glGetError());

        if (textureHandle[textureIndex] != 0)
        {
//            width = 800 * length;
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            Bitmap bmp = BitmapFactory.decodeResource(MainActivity.globalContext.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle[textureIndex]);

            // Set filtering
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bmp, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bmp.recycle();
        }

        if (textureHandle[textureIndex] == 0) {
            throw new RuntimeException("Error loading textures.");
        }

        var info = new TextureInfo(textureHandle[textureIndex]);
        Log.i("NGSPACEly",textureHandle[textureIndex] + "  "+textureIndex);
        textureIndex++;
        return info;
    }
}