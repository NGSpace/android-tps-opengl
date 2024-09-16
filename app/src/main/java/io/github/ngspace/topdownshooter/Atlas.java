package io.github.ngspace.topdownshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

public class Atlas {
    public static float totalWidth = 800;
    public static float width = 800;
    public static float height = 600;

    public static int textureDataHandle;

    public static final float simley = 0;

    static String[] drawables = {"simley", "fedora", "starset"};
    public static int length = drawables.length;

    public static int GetImage(Context c, String ImageName) {
        return c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
    }

    public static void loadTextures(Context context) {
        final int[] textureHandle = new int[1];
        GLES30.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            width = 800 * length;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;   // No pre-scaling

            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            Bitmap bmp = Bitmap.createBitmap((int) width, (int) height, conf); // this creates a MUTABLE bitmap
            Canvas canvas = new Canvas(bmp);
//            bmp.setPixel(0,0,Color.RED);
//            bmp.setPixel(0,1,Color.BLUE);
//            bmp.setPixel(1,0,Color.BLUE);
//            bmp.setPixel(1,1,Color.RED);
            for (int i = 0;i<drawables.length;i++) {
                Log.i("NGSPACEly",drawables[i]+"");
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), GetImage(context,drawables[i]), options);
                canvas.drawBitmap(bitmap, null, new Rect(800*i,0, (int)800*i+800, (int) height), null);
            }
//bmp=bitmap;
            // Bind to the texture in OpenGL
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
//            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
//            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bmp, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bmp.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        textureDataHandle =  textureHandle[0];
    }
}
