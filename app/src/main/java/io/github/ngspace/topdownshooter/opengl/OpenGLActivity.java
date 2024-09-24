package io.github.ngspace.topdownshooter.opengl;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class OpenGLActivity extends Activity {

    private android.opengl.GLSurfaceView gLView;

    public static int realWidth;
    public static int realHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var v = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getRealMetrics(v);
        var p = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(p);
        realHeight = p.y;//v.heightPixels;
        realWidth  = p.x;//v.widthPixels ;

        Log.i("NGSPACEly", "     " + realWidth + "  " + realHeight);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new GLSurfaceView(this);
        setContentView(gLView);
        realWidth = gLView.getWidth();
        Log.i("NGSPACEly"," "+realWidth);
    }
}