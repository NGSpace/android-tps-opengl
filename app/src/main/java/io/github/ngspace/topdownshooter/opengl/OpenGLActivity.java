package io.github.ngspace.topdownshooter.opengl;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class OpenGLActivity extends Activity {

    private android.opengl.GLSurfaceView gLView;

    public static float realWidth;
    public static float realHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// TODO Remove this when done debugging
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        var p = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(p);
        realHeight = p.y;
        realWidth  = p.x;

        Log.i("NGSPACEly", "     " + realWidth + "  " + realHeight);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new GLSurfaceView(this);
        setContentView(gLView);
        realWidth = gLView.getWidth();
        Log.i("NGSPACEly"," "+realWidth);
    }
}