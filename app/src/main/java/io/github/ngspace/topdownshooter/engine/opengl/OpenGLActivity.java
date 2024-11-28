package io.github.ngspace.topdownshooter.engine.opengl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import io.github.ngspace.topdownshooter.engine.opengl.renderer.GLRenderer;
import io.github.ngspace.topdownshooter.engine.opengl.renderer.Textures;

public abstract class OpenGLActivity extends Activity {

    public static float realWidth;
    public static float realHeight;
    protected GLRenderer renderer;

    @Override public void onCreate(Bundle savedInstanceState) {
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

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        OpenGLSurfaceView gLView = new OpenGLSurfaceView(this);
        this.renderer = gLView.getRenderer();
        setContentView(gLView);
    }
}